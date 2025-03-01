package com.example.MiniProject1;
import static org.junit.jupiter.api.Assertions.*;

import com.example.model.Product;
import com.example.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

    @SpringBootTest
    public class ProductServiceTests {

        @Value("${spring.application.productDataPath}")
        private String productDataPath;

        @Autowired
        private ProductService productService;

        @Autowired
        private ObjectMapper objectMapper;

        /**
         * Clears the product JSON file before each test.
         */
        @BeforeEach
        public void setUp() {
            File file = new File(productDataPath);
            try {
                objectMapper.writeValue(file, new ArrayList<Product>());
            } catch (IOException e) {
                fail("Failed to clear the product data file: " + e.getMessage());
            }
        }

        // ============================================================
        // Tests for addProduct() method (3 tests)
        // ============================================================

        @Test
        public void testAddProduct_HappyPath() {
            UUID prodId = UUID.randomUUID();
            Product product = new Product(prodId, "Test Product", 100.0);
            Product returnedProduct = productService.addProduct(product);

            assertNotNull(returnedProduct, "Product should not be null after addition");
            assertEquals(prodId, returnedProduct.getId(), "Returned product ID should match");

            // Verify JSON file persistence
            try {
                Product[] products = objectMapper.readValue(new File(productDataPath), Product[].class);
                boolean found = false;
                for (Product p : products) {
                    if (p.getId().equals(prodId)) {
                        found = true;
                        break;
                    }
                }
                assertTrue(found, "Product should be present in the JSON file");
            } catch (IOException e) {
                fail("Error reading product JSON file: " + e.getMessage());
            }
        }

        @Test
        public void testAddProduct_Duplicate() {
            // Since duplicate handling is not implemented, duplicates are allowed.
            UUID prodId = UUID.randomUUID();
            Product product1 = new Product(prodId, "Test Product 1", 100.0);
            Product product2 = new Product(prodId, "Test Product 1 Duplicate", 100.0);
            productService.addProduct(product1);
            productService.addProduct(product2);

            try {
                Product[] products = objectMapper.readValue(new File(productDataPath), Product[].class);
                int count = 0;
                for (Product p : products) {
                    if (p.getId().equals(prodId)) {
                        count++;
                    }
                }
                assertEquals(2, count, "There should be two products with the same ID in the JSON file");
            } catch (IOException e) {
                fail("Error reading product JSON file: " + e.getMessage());
            }
        }

        @Test
        public void testAddProduct_InvalidInput() {
            // When a null product is passed, assume productService.addProduct returns null and leaves the file unchanged.
            Product result = productService.addProduct(null);
            assertNull(result, "addProduct(null) should return null");
            try {
                Product[] products = objectMapper.readValue(new File(productDataPath), Product[].class);
                assertEquals(0, products.length, "JSON file should remain empty when null is added");
            } catch (IOException e) {
                fail("Error reading product JSON file: " + e.getMessage());
            }
        }

        // ============================================================
        // Tests for getProducts() method (3 tests)
        // ============================================================

        @Test
        public void testGetProducts_HappyPath() {
            Product product1 = new Product(UUID.randomUUID(), "Product 1", 50.0);
            Product product2 = new Product(UUID.randomUUID(), "Product 2", 75.0);
            productService.addProduct(product1);
            productService.addProduct(product2);

            ArrayList<Product> products = productService.getProducts();
            assertEquals(2, products.size(), "getProducts should return 2 products");
        }

        @Test
        public void testGetProducts_Empty() {
            ArrayList<Product> products = productService.getProducts();
            assertTrue(products.isEmpty(), "getProducts should return an empty list when no products are added");
        }

        @Test
        public void testGetProducts_AfterDeletion() {
            Product product = new Product(UUID.randomUUID(), "Product to Delete", 60.0);
            productService.addProduct(product);
            productService.deleteProductById(product.getId());

            ArrayList<Product> products = productService.getProducts();
            assertTrue(products.isEmpty(), "getProducts should return an empty list after deletion");
        }

        // ============================================================
        // Tests for getProductById() method (3 tests)
        // ============================================================

        @Test
        public void testGetProductById_HappyPath() {
            UUID prodId = UUID.randomUUID();
            Product product = new Product(prodId, "Test Product", 100.0);
            productService.addProduct(product);

            Product retrieved = productService.getProductById(prodId);
            assertNotNull(retrieved, "Product should be retrievable by its ID");
            assertEquals(prodId, retrieved.getId(), "Retrieved product ID should match");
        }

        @Test
        public void testGetProductById_NonExistent() {
            Product retrieved = productService.getProductById(UUID.randomUUID());
            assertNull(retrieved, "Non-existent product ID should return null");
        }

        @Test
        public void testGetProductById_AfterDeletion() {
            UUID prodId = UUID.randomUUID();
            Product product = new Product(prodId, "Test Product", 100.0);
            productService.addProduct(product);
            productService.deleteProductById(prodId);

            Product retrieved = productService.getProductById(prodId);
            assertNull(retrieved, "Product should not be retrievable after deletion");
        }

        // ============================================================
        // Tests for updateProduct() method (3 tests)
        // ============================================================

        @Test
        public void testUpdateProduct_HappyPath() {
            UUID prodId = UUID.randomUUID();
            Product product = new Product(prodId, "Old Name", 100.0);
            productService.addProduct(product);

            Product updated = productService.updateProduct(prodId, "New Name", 150.0);
            assertNotNull(updated, "Updated product should not be null");
            assertEquals("New Name", updated.getName(), "Product name should be updated");
            assertEquals(150.0, updated.getPrice(), "Product price should be updated");

            // Verify JSON file reflects the update.
            try {
                Product[] products = objectMapper.readValue(new File(productDataPath), Product[].class);
                for (Product p : products) {
                    if (p.getId().equals(prodId)) {
                        assertEquals("New Name", p.getName(), "JSON file: name should be updated");
                        assertEquals(150.0, p.getPrice(), "JSON file: price should be updated");
                    }
                }
            } catch (IOException e) {
                fail("Error reading product JSON file: " + e.getMessage());
            }
        }

        @Test
        public void testUpdateProduct_NonExistent() {
            // Updating a product that doesn't exist should return null (or do nothing).
            Product updated = productService.updateProduct(UUID.randomUUID(), "Name", 150.0);
            assertNull(updated, "Updating a non-existent product should return null");
        }

        @Test
        public void testUpdateProduct_InvalidInput() {
            // For example, updating with an empty name or negative price might return null.
            UUID prodId = UUID.randomUUID();
            Product product = new Product(prodId, "Valid Name", 100.0);
            productService.addProduct(product);

            Product updated = productService.updateProduct(prodId, "", -50.0);
            // Assuming your implementation returns null for invalid update inputs.
            assertNull(updated, "Updating with invalid inputs should return null");

            // Verify the product remains unchanged in the JSON file.
            Product retrieved = productService.getProductById(prodId);
            assertNotNull(retrieved, "Original product should remain unchanged");
            assertEquals("Valid Name", retrieved.getName(), "Product name should remain unchanged");
            assertEquals(100.0, retrieved.getPrice(), "Product price should remain unchanged");
        }

        // ============================================================
        // Tests for applyDiscount() method (3 tests)
        // ============================================================
        // Assume discount is a percentage. For instance, a discount of 10.0 means 10% off.

        @Test
        public void testApplyDiscount_HappyPath() {
            UUID prodId = UUID.randomUUID();
            Product product = new Product(prodId, "Discounted Product", 200.0);
            productService.addProduct(product);

            ArrayList<UUID> productIds = new ArrayList<>();
            productIds.add(prodId);
            productService.applyDiscount(10.0, productIds);

            Product updated = productService.getProductById(prodId);
            assertNotNull(updated, "Product should still exist after discount");
            // New price should be 200 - 10% of 200 = 180.0
            assertEquals(180.0, updated.getPrice(), "Product price should be updated after applying discount");
        }

        @Test
        public void testApplyDiscount_NonExistentProducts() {
            // Applying discount to non-existent product IDs should not affect the JSON file.
            ArrayList<UUID> productIds = new ArrayList<>();
            productIds.add(UUID.randomUUID());
            productService.applyDiscount(10.0, productIds);

            ArrayList<Product> products = productService.getProducts();
            assertTrue(products.isEmpty(), "No products should be updated if none exist");
        }

        @Test
        public void testApplyDiscount_MultipleProducts() {
            UUID prodId1 = UUID.randomUUID();
            UUID prodId2 = UUID.randomUUID();
            Product product1 = new Product(prodId1, "Product 1", 100.0);
            Product product2 = new Product(prodId2, "Product 2", 300.0);
            productService.addProduct(product1);
            productService.addProduct(product2);

            ArrayList<UUID> productIds = new ArrayList<>(Arrays.asList(prodId1, prodId2));
            productService.applyDiscount(20.0, productIds);
            // For product1: new price 100 - 20% = 80.0; for product2: 300 - 20% = 240.0

            Product updated1 = productService.getProductById(prodId1);
            Product updated2 = productService.getProductById(prodId2);
            assertNotNull(updated1, "Product1 should exist after discount");
            assertNotNull(updated2, "Product2 should exist after discount");
            assertEquals(80.0, updated1.getPrice(), "Product1 price should be 80.0 after 20% discount");
            assertEquals(240.0, updated2.getPrice(), "Product2 price should be 240.0 after 20% discount");
        }

        // ============================================================
        // Tests for deleteProductById() method (3 tests)
        // ============================================================

        @Test
        public void testDeleteProductById_HappyPath() {
            UUID prodId = UUID.randomUUID();
            Product product = new Product(prodId, "Product to Delete", 150.0);
            productService.addProduct(product);
            productService.deleteProductById(prodId);

            Product retrieved = productService.getProductById(prodId);
            assertNull(retrieved, "Product should be deleted and not retrievable");
        }

        @Test
        public void testDeleteProductById_NonExistent() {
            // Deleting a non-existent product should leave the JSON file unchanged.
            productService.deleteProductById(UUID.randomUUID());
            ArrayList<Product> products = productService.getProducts();
            assertTrue(products.isEmpty(), "JSON file should remain empty when deleting non-existent product");
        }

        @Test
        public void testDeleteProductById_MultipleProducts() {
            Product product1 = new Product(UUID.randomUUID(), "Product 1", 100.0);
            Product product2 = new Product(UUID.randomUUID(), "Product 2", 200.0);
            productService.addProduct(product1);
            productService.addProduct(product2);
            productService.deleteProductById(product1.getId());

            ArrayList<Product> products = productService.getProducts();
            assertEquals(1, products.size(), "Only one product should remain after deletion");
            assertEquals(product2.getId(), products.get(0).getId(), "The remaining product should be product2");
        }
    }


