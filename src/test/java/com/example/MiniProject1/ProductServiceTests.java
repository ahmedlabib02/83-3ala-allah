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
import java.util.UUID;

@SpringBootTest
public class ProductServiceTests {

    @Value("${spring.application.productDataPath}")
    private String productDataPath;

    @Autowired
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    // Reset the product JSON file before each test
    @BeforeEach
    public void overrideAll() {
        try {
            objectMapper.writeValue(new File(productDataPath), new ArrayList<Product>());
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to JSON file", e);
        }
    }


    public ArrayList<Product> getProducts() {
        try {
            File file = new File(productDataPath);
            if (!file.exists()) {
                return new ArrayList<>();
            }
            return new ArrayList<>(Arrays.asList(objectMapper.readValue(file, Product[].class)));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read from JSON file", e);
        }
    }

    // Helper method to add a product directly to the JSON file
    public Product addProduct(Product product) {
        try {
            File file = new File(productDataPath);
            ArrayList<Product> products;
            if (!file.exists()) {
                products = new ArrayList<>();
            } else {
                products = new ArrayList<>(Arrays.asList(objectMapper.readValue(file, Product[].class)));
            }
            products.add(product);
            objectMapper.writeValue(file, products);
            return product;
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to JSON file", e);
        }
    }

    // ============================================================
    // Tests for addProduct() method (3 tests)
    // ============================================================

    @Test
    public void testAddProduct_HappyPath() {
        UUID prodId = UUID.randomUUID();
        Product product = new Product(prodId, "Test Product", 100.0);
        productService.addProduct(product);

        ArrayList<Product> products = getProducts();
        boolean found = products.stream().anyMatch(p -> p.getId().equals(prodId));
        assertTrue(found, "Product should be present in the JSON file after addition");
    }

    @Test
    public void testAddProduct_Duplicate() {
        // Duplicate handling: adding two products with the same ID should produce only one entry.
        UUID prodId = UUID.randomUUID();
        Product product1 = new Product(prodId, "Test Product", 100.0);
        Product product2 = new Product(prodId, "Test Product Duplicate", 100.0);
        productService.addProduct(product1);
        productService.addProduct(product2);

        ArrayList<Product> products = getProducts();
        long count = products.stream().filter(p -> p.getId().equals(prodId)).count();
        assertEquals(1, count, "There should be only one product with the same ID, no duplicates allowed");
    }

    @Test
    public void testAddProduct_InvalidInput() {
        // When a null product is passed, assume productService.addProduct returns null and leaves the file unchanged.
        Product result = productService.addProduct(null);
        assertNull(result, "addProduct(null) should return null");
        ArrayList<Product> products = getProducts();
        assertEquals(0, products.size(), "JSON file should remain empty when null is added");
    }

    // ============================================================
    // Tests for getProducts() method (3 tests)
    // ============================================================

    @Test
    public void testGetProducts_HappyPath() {
        Product product1 = new Product(UUID.randomUUID(), "Product 1", 50.0);
        Product product2 = new Product(UUID.randomUUID(), "Product 2", 75.0);
        addProduct(product1);
        addProduct(product2);

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
        addProduct(product);
//        productService.deleteProductById(product.getId());
        overrideAll();
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
        addProduct(product);

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
        addProduct(product);
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
        addProduct(product);

        Product updated = productService.updateProduct(prodId, "New Name", 150.0);
        assertNotNull(updated, "Updated product should not be null");
        assertEquals("New Name", updated.getName(), "Product name should be updated");
        assertEquals(150.0, updated.getPrice(), "Product price should be updated");

        ArrayList<Product> products = getProducts();
        for (Product p : products) {
            if (p.getId().equals(prodId)) {
                assertEquals("New Name", p.getName(), "JSON file: name should be updated");
                assertEquals(150.0, p.getPrice(), "JSON file: price should be updated");
            }
        }
    }

    @Test
    public void testUpdateProduct_NonExistent() {
        Product updated = productService.updateProduct(UUID.randomUUID(), "Name", 150.0);
        assertNull(updated, "Updating a non-existent product should return null");
    }

    @Test
    public void testUpdateProduct_InvalidInput() {
        UUID prodId = UUID.randomUUID();
        Product product = new Product(prodId, "Valid Name", 100.0);
        addProduct(product);

        Product updated = productService.updateProduct(prodId, "", -50.0);
        assertNull(updated, "Updating with invalid inputs should return null");

        Product retrieved = getProducts().stream().filter(p -> p.getId().equals(prodId)).findFirst().orElse(null);
        assertNotNull(retrieved, "Original product should remain unchanged");
        assertEquals("Valid Name", retrieved.getName(), "Product name should remain unchanged");
        assertEquals(100.0, retrieved.getPrice(), "Product price should remain unchanged");
    }

    // ============================================================
    // Tests for applyDiscount() method (3 tests)
    // ============================================================

    @Test
    public void testApplyDiscount_HappyPath() {
        UUID prodId = UUID.randomUUID();
        Product product = new Product(prodId, "Discounted Product", 200.0);
        addProduct(product);

        ArrayList<UUID> productIds = new ArrayList<>();
        productIds.add(prodId);
        productService.applyDiscount(10.0, productIds);

        Product updated = getProducts().stream().filter(p -> p.getId().equals(prodId)).findFirst().orElse(null);
        assertNotNull(updated, "Product should exist after discount");
        // New price should be 200 - 10% of 200 = 180.0
        assertEquals(180.0, updated.getPrice(), "Product price should be updated after applying discount");
    }

    @Test
    public void testApplyDiscount_NonExistentProducts() {
        ArrayList<UUID> productIds = new ArrayList<>();
        productIds.add(UUID.randomUUID());
        productService.applyDiscount(10.0, productIds);

        ArrayList<Product> products = getProducts();
        assertTrue(products.isEmpty(), "No products should be updated if none exist");
    }

    @Test
    public void testApplyDiscount_MultipleProducts() {
        UUID prodId1 = UUID.randomUUID();
        UUID prodId2 = UUID.randomUUID();
        Product product1 = new Product(prodId1, "Product 1", 100.0);
        Product product2 = new Product(prodId2, "Product 2", 300.0);
        addProduct(product1);
        addProduct(product2);

        ArrayList<UUID> productIds = new ArrayList<>();
        productIds.add(prodId1);
        productIds.add(prodId2);
        productService.applyDiscount(20.0, productIds);

        Product updated1 = getProducts().stream().filter(p -> p.getId().equals(prodId1)).findFirst().orElse(null);
        Product updated2 = getProducts().stream().filter(p -> p.getId().equals(prodId2)).findFirst().orElse(null);
        assertNotNull(updated1, "Product1 should exist after discount");
        assertNotNull(updated2, "Product2 should exist after discount");
        // For product1: 100 - 20% = 80.0; for product2: 300 - 20% = 240.0
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
        addProduct(product);
        productService.deleteProductById(prodId);

        Product retrieved =getProducts().stream().filter(p -> p.getId().equals(prodId)).findFirst().orElse(null);
        assertNull(retrieved, "Product should be deleted and not retrievable");
    }

    @Test
    public void testDeleteProductById_NonExistent() {
        productService.deleteProductById(UUID.randomUUID());
        ArrayList<Product> products = getProducts();
        assertTrue(products.isEmpty(), "JSON file should remain empty when deleting non-existent product");
    }

    @Test
    public void testDeleteProductById_MultipleProducts() {
        Product product1 = new Product(UUID.randomUUID(), "Product 1", 100.0);
        Product product2 = new Product(UUID.randomUUID(), "Product 2", 200.0);
        addProduct(product1);
        addProduct(product2);
        productService.deleteProductById(product1.getId());

        ArrayList<Product> products = getProducts();
        assertEquals(1, products.size(), "Only one product should remain after deletion");
        assertEquals(product2.getId(), products.get(0).getId(), "The remaining product should be product2");
    }
}
