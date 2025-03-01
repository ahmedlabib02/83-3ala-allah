package com.example.MiniProject1;

import static org.junit.jupiter.api.Assertions.*;

import com.example.model.Cart;
import com.example.model.Product;
import com.example.service.CartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootTest
public class CartServiceTests {

    @Value("${spring.application.cartDataPath}")
    private String cartDataPath;

    @Autowired
    private CartService cartService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Clears the JSON file before each test so that each test starts with a clean state.
     */
    @BeforeEach
    public void setUp() {
        File file = new File(cartDataPath);
        try {
            objectMapper.writeValue(file, new ArrayList<Cart>());
        } catch (IOException e) {
            fail("Failed to clear the cart data file: " + e.getMessage());
        }
    }

    // ============================================================
    // Tests for addCart() method (3 tests)
    // ============================================================

    @Test
    public void testAddCart_HappyPath() {
        UUID cartId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Cart cart = new Cart(cartId, userId, new ArrayList<>());
        Cart returnedCart = cartService.addCart(cart);

        assertNotNull(returnedCart, "Cart should not be null after addition");
        assertEquals(cartId, returnedCart.getId(), "Returned cart ID should match the provided ID");

        // Verify that the JSON file contains this cart.
        try {
            Cart[] carts = objectMapper.readValue(new File(cartDataPath), Cart[].class);
            boolean found = false;
            for (Cart c : carts) {
                if (c.getId().equals(cartId)) {
                    found = true;
                    break;
                }
            }
            assertTrue(found, "Cart should be present in the JSON file after addition");
        } catch (IOException e) {
            fail("Error reading JSON file: " + e.getMessage());
        }
    }

    @Test
    public void testAddCart_Duplicate() {
        // Since duplicates are not handled, adding two carts with the same ID should create two entries.
        UUID cartId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Cart cart = new Cart(cartId, userId, new ArrayList<>());
        cartService.addCart(cart);

        Cart duplicate = new Cart(cartId, userId, new ArrayList<>());
        cartService.addCart(duplicate);

        try {
            Cart[] carts = objectMapper.readValue(new File(cartDataPath), Cart[].class);
            int count = 0;
            for (Cart c : carts) {
                if (c.getId().equals(cartId)) {
                    count++;
                }
            }
            assertEquals(2, count, "There should be two carts with the same ID in the JSON file");
        } catch (IOException e) {
            fail("Error reading JSON file: " + e.getMessage());
        }
    }

    @Test
    public void testAddCart_InvalidInput() {
        // Instead of throwing an exception, assume the service returns null and leaves the file unchanged.
        Cart result = cartService.addCart(null);
        assertNull(result, "addCart(null) should return null");
        try {
            Cart[] carts = objectMapper.readValue(new File(cartDataPath), Cart[].class);
            assertEquals(0, carts.length, "JSON file should remain empty when null is added");
        } catch (IOException e) {
            fail("Error reading JSON file: " + e.getMessage());
        }
    }

    // ============================================================
    // Tests for getCarts() method (3 tests)
    // ============================================================

    @Test
    public void testGetCarts_HappyPath() {
        Cart cart1 = new Cart(UUID.randomUUID(), UUID.randomUUID(), new ArrayList<>());
        Cart cart2 = new Cart(UUID.randomUUID(), UUID.randomUUID(), new ArrayList<>());
        cartService.addCart(cart1);
        cartService.addCart(cart2);

        List<Cart> carts = cartService.getCarts();
        assertEquals(2, carts.size(), "There should be 2 carts returned");
    }

    @Test
    public void testGetCarts_Empty() {
        List<Cart> carts = cartService.getCarts();
        assertTrue(carts.isEmpty(), "Cart list should be empty when no carts are added");
    }

    @Test
    public void testGetCarts_AfterDeletion() {
        Cart cart = new Cart(UUID.randomUUID(), UUID.randomUUID(), new ArrayList<>());
        cartService.addCart(cart);
        cartService.deleteCartById(cart.getId());

        List<Cart> carts = cartService.getCarts();
        assertTrue(carts.isEmpty(), "Cart list should be empty after deletion");
    }

    // ============================================================
    // Tests for getCartById() method (3 tests)
    // ============================================================

    @Test
    public void testGetCartById_HappyPath() {
        UUID cartId = UUID.randomUUID();
        Cart cart = new Cart(cartId, UUID.randomUUID(), new ArrayList<>());
        cartService.addCart(cart);

        Cart retrieved = cartService.getCartById(cartId);
        assertNotNull(retrieved, "Cart should be retrievable by its ID");
        assertEquals(cartId, retrieved.getId(), "Retrieved cart ID should match");
    }

    @Test
    public void testGetCartById_NonExistent() {
        Cart retrieved = cartService.getCartById(UUID.randomUUID());
        assertNull(retrieved, "Non-existent cart ID should return null");
    }

    @Test
    public void testGetCartById_AfterDeletion() {
        UUID cartId = UUID.randomUUID();
        Cart cart = new Cart(cartId, UUID.randomUUID(), new ArrayList<>());
        cartService.addCart(cart);
        cartService.deleteCartById(cartId);

        Cart retrieved = cartService.getCartById(cartId);
        assertNull(retrieved, "Cart should not be retrievable after deletion");
    }

    // ============================================================
    // Tests for getCartByUserId() method (3 tests)
    // ============================================================

    @Test
    public void testGetCartByUserId_HappyPath() {
        UUID userId = UUID.randomUUID();
        Cart cart = new Cart(UUID.randomUUID(), userId, new ArrayList<>());
        cartService.addCart(cart);

        Cart retrieved = cartService.getCartByUserId(userId);
        assertNotNull(retrieved, "Cart should be retrieved by user ID");
        assertEquals(userId, retrieved.getUserId(), "User ID should match");
    }

    @Test
    public void testGetCartByUserId_NonExistent() {
        Cart retrieved = cartService.getCartByUserId(UUID.randomUUID());
        assertNull(retrieved, "Non-existent user ID should return null");
    }

    @Test
    public void testGetCartByUserId_MultipleCarts() {
        UUID userId = UUID.randomUUID();
        Cart cart1 = new Cart(UUID.randomUUID(), userId, new ArrayList<>());
        Cart cart2 = new Cart(UUID.randomUUID(), userId, new ArrayList<>());
        cartService.addCart(cart1);
        cartService.addCart(cart2);

        Cart retrieved = cartService.getCartByUserId(userId);
        assertNotNull(retrieved, "A cart should be returned for the user");
        // Assuming the service returns the first added cart.
        assertEquals(cart1.getId(), retrieved.getId(), "The first added cart should be returned");
    }

    // ============================================================
    // Tests for addProductToCart() method (3 tests)
    // ============================================================

    @Test
    public void testAddProductToCart_HappyPath() {
        UUID cartId = UUID.randomUUID();
        Cart cart = new Cart(cartId, UUID.randomUUID(), new ArrayList<>());
        cartService.addCart(cart);

        Product product = new Product(UUID.randomUUID(), "Test Product", 15.0);
        cartService.addProductToCart(cartId, product);

        Cart updatedCart = cartService.getCartById(cartId);
        assertNotNull(updatedCart, "Cart should exist after adding a product");
        assertFalse(updatedCart.getProducts().isEmpty(), "Cart should have at least one product");
        assertEquals(product.getId(), updatedCart.getProducts().get(0).getId(), "The added product's ID should match");
    }

    @Test
    public void testAddProductToCart_NonExistentCart() {
        // Since exceptions aren't used, adding a product to a non-existent cart should simply do nothing.
        Product product = new Product(UUID.randomUUID(), "Test Product", 15.0);
        cartService.addProductToCart(UUID.randomUUID(), product);
        // Verify that the JSON file remains unchanged (i.e. empty)
        try {
            Cart[] carts = objectMapper.readValue(new File(cartDataPath), Cart[].class);
            assertEquals(0, carts.length, "No cart should be modified if the cart doesn't exist");
        } catch (IOException e) {
            fail("Error reading JSON file: " + e.getMessage());
        }
    }

    @Test
    public void testAddProductToCart_MultipleProducts() {
        UUID cartId = UUID.randomUUID();
        Cart cart = new Cart(cartId, UUID.randomUUID(), new ArrayList<>());
        cartService.addCart(cart);

        Product product1 = new Product(UUID.randomUUID(), "Product 1", 10.0);
        Product product2 = new Product(UUID.randomUUID(), "Product 2", 20.0);
        cartService.addProductToCart(cartId, product1);
        cartService.addProductToCart(cartId, product2);

        Cart updatedCart = cartService.getCartById(cartId);
        assertNotNull(updatedCart, "Cart should exist");
        assertEquals(2, updatedCart.getProducts().size(), "Cart should have 2 products added");
    }

    // ============================================================
    // Tests for deleteProductFromCart() method (3 tests)
    // ============================================================

    @Test
    public void testDeleteProductFromCart_HappyPath() {
        UUID cartId = UUID.randomUUID();
        Product product = new Product(UUID.randomUUID(), "Test Product", 15.0);
        ArrayList<Product> products = new ArrayList<>();
        products.add(product);
        Cart cart = new Cart(cartId, UUID.randomUUID(), products);
        cartService.addCart(cart);

        cartService.deleteProductFromCart(cartId, product);
        Cart updatedCart = cartService.getCartById(cartId);
        assertNotNull(updatedCart, "Cart should exist after deletion");
        assertTrue(updatedCart.getProducts().isEmpty(), "Product should be removed from the cart");
    }

    @Test
    public void testDeleteProductFromCart_ProductNotPresent() {
        UUID cartId = UUID.randomUUID();
        Cart cart = new Cart(cartId, UUID.randomUUID(), new ArrayList<>());
        cartService.addCart(cart);

        // Create a product that was never added.
        Product product = new Product(UUID.randomUUID(), "Non-existent Product", 15.0);
        cartService.deleteProductFromCart(cartId, product);
        Cart updatedCart = cartService.getCartById(cartId);
        assertNotNull(updatedCart, "Cart should still exist");
        assertTrue(updatedCart.getProducts().isEmpty(), "Cart should remain empty since product was not present");
    }

    @Test
    public void testDeleteProductFromCart_NonExistentCart() {
        // Calling deleteProductFromCart with a random cart ID should not alter any data.
        Product product = new Product(UUID.randomUUID(), "Test Product", 15.0);
        cartService.deleteProductFromCart(UUID.randomUUID(), product);
        try {
            Cart[] carts = objectMapper.readValue(new File(cartDataPath), Cart[].class);
            assertEquals(0, carts.length, "JSON file should remain unchanged if cart doesn't exist");
        } catch (IOException e) {
            fail("Error reading JSON file: " + e.getMessage());
        }
    }

    // ============================================================
    // Tests for deleteCartById() method (3 tests)
    // ============================================================

    @Test
    public void testDeleteCartById_HappyPath() {
        UUID cartId = UUID.randomUUID();
        Cart cart = new Cart(cartId, UUID.randomUUID(), new ArrayList<>());
        cartService.addCart(cart);
        cartService.deleteCartById(cartId);

        Cart retrieved = cartService.getCartById(cartId);
        assertNull(retrieved, "Cart should be deleted and not retrievable");
    }

    @Test
    public void testDeleteCartById_NonExistent() {
        // Deleting a non-existent cart should leave the JSON file unchanged.
        cartService.deleteCartById(UUID.randomUUID());
        try {
            Cart[] carts = objectMapper.readValue(new File(cartDataPath), Cart[].class);
            assertEquals(0, carts.length, "JSON file should remain empty when non-existent cart is deleted");
        } catch (IOException e) {
            fail("Error reading JSON file: " + e.getMessage());
        }
    }

    @Test
    public void testDeleteCartById_MultipleCarts() {
        Cart cart1 = new Cart(UUID.randomUUID(), UUID.randomUUID(), new ArrayList<>());
        Cart cart2 = new Cart(UUID.randomUUID(), UUID.randomUUID(), new ArrayList<>());
        cartService.addCart(cart1);
        cartService.addCart(cart2);

        cartService.deleteCartById(cart1.getId());
        List<Cart> carts = cartService.getCarts();
        assertEquals(1, carts.size(), "Only one cart should remain after deletion");
        assertEquals(cart2.getId(), carts.get(0).getId(), "The remaining cart should be the one not deleted");
    }
}
