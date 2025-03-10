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
import java.util.Arrays;
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
     * Resets the cart JSON file before each test to ensure a clean slate.
     */
    @BeforeEach
    public void overrideAll() {
        try {
            objectMapper.writeValue(new File(cartDataPath), new ArrayList<Cart>());
        } catch (IOException e) {
            throw new RuntimeException("Failed to clear cart JSON file", e);
        }
    }

    /**
     * Helper method to read carts directly from the JSON file.
     */
    public ArrayList<Cart> getCarts() {
        try {
            File file = new File(cartDataPath);
            if (!file.exists()) {
                return new ArrayList<>();
            }
            return new ArrayList<>(Arrays.asList(objectMapper.readValue(file, Cart[].class)));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read from cart JSON file", e);
        }
    }

    /**
     * Helper method to add a cart directly to the JSON file.
     */
    public Cart addCart(Cart cart) {
        try {
            File file = new File(cartDataPath);
            ArrayList<Cart> carts;
            if (!file.exists()) {
                carts = new ArrayList<>();
            } else {
                carts = new ArrayList<>(Arrays.asList(objectMapper.readValue(file, Cart[].class)));
            }
            carts.add(cart);
            objectMapper.writeValue(file, carts);
            return cart;
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to cart JSON file", e);
        }
    }

    // ============================================================
    // Tests for addCart() method (3 tests)
    // ============================================================

    @Test
    public void testAddCart_HappyPath() {
        UUID cartId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Cart cart = new Cart();
        cart.setId(cartId);
        cart.setUserId(userId);
        cart.setProducts(new ArrayList<>());

        cartService.addCart(cart);

        ArrayList<Cart> carts = getCarts();
        boolean found = carts.stream().anyMatch(c -> c.getId().equals(cartId));
        assertTrue(found, "Cart should be present in the JSON file after addition");
    }

    @Test
    public void testAddCart_Duplicate() {
        // For this example, duplicate handling is not implemented so two carts with the same ID are allowed.
        UUID cartId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Cart cart1 = new Cart();
        cart1.setId(cartId);
        cart1.setUserId(userId);
        cart1.setProducts(new ArrayList<>());
        cartService.addCart(cart1);

        Cart cart2 = new Cart();
        cart2.setId(cartId);
        cart2.setUserId(userId);
        cart2.setProducts(new ArrayList<>());
        cartService.addCart(cart2);

        ArrayList<Cart> carts = getCarts();
        long count = carts.stream().filter(c -> c.getId().equals(cartId)).count();
        // Here we expect two entries because duplicates are allowed.
        assertEquals(2, count, "There should be two carts with the same ID in the JSON file");
    }

    @Test
    public void testAddCart_InvalidInput() {
        // Expect an exception when a null cart is added.
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                cartService.addCart(null), "Adding null cart should throw exception");
        assertEquals(0, getCarts().size(), "No cart should be added");
    }

    // ============================================================
    // Tests for getCarts() method (3 tests)
    // ============================================================

    @Test
    public void testGetCarts_HappyPath() {
        Cart cart1 = new Cart();
        cart1.setId(UUID.randomUUID());
        cart1.setUserId(UUID.randomUUID());
        cart1.setProducts(new ArrayList<>());

        Cart cart2 = new Cart();
        cart2.setId(UUID.randomUUID());
        cart2.setUserId(UUID.randomUUID());
        cart2.setProducts(new ArrayList<>());

        addCart(cart1);
        addCart(cart2);

        List<Cart> carts = cartService.getCarts();
        assertEquals(2, carts.size(), "getCarts should return 2 carts");
    }

    @Test
    public void testGetCarts_Empty() {
        List<Cart> carts = cartService.getCarts();
        assertTrue(carts.isEmpty(), "getCarts should return an empty list when no carts are added");
    }

    @Test
    public void testGetCarts_AfterDeletion() {
        Cart cart = new Cart();
        cart.setId(UUID.randomUUID());
        cart.setUserId(UUID.randomUUID());
        cart.setProducts(new ArrayList<>());
        addCart(cart);

        overrideAll();

        List<Cart> carts = cartService.getCarts();
        assertTrue(carts.isEmpty(), "getCarts should return an empty list after deletion");
    }

    // ============================================================
    // Tests for getCartById() method (3 tests)
    // ============================================================

    @Test
    public void testGetCartById_HappyPath() {
        UUID cartId = UUID.randomUUID();
        Cart cart = new Cart();
        cart.setId(cartId);
        cart.setUserId(UUID.randomUUID());
        cart.setProducts(new ArrayList<>());
        addCart(cart);

        Cart retrieved = cartService.getCartById(cartId);
        assertNotNull(retrieved, "Cart should be retrievable by its ID");
        assertEquals(cartId, retrieved.getId(), "Retrieved cart ID should match");
    }

    // Expect an exception when retrieving a non-existent cart.
    @Test
    public void testGetCartById_NonExistent() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                        cartService.getCartById(UUID.randomUUID()),
                "Retrieving a non-existent cart ID should throw an exception");
    }

    // Expect an exception when retrieving a cart after deletion.
    @Test
    public void testGetCartById_AfterDeletion() {
        UUID cartId = UUID.randomUUID();
        Cart cart = new Cart();
        cart.setId(cartId);
        cart.setUserId(UUID.randomUUID());
        cart.setProducts(new ArrayList<>());
        addCart(cart);

        overrideAll();
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                        cartService.getCartById(cartId),
                "Cart should not be retrievable after deletion");
    }

    // ============================================================
    // Tests for getCartByUserId() method (3 tests)
    // ============================================================

    @Test
    public void testGetCartByUserId_HappyPath() {
        UUID userId = UUID.randomUUID();
        Cart cart = new Cart();
        cart.setId(UUID.randomUUID());
        cart.setUserId(userId);
        cart.setProducts(new ArrayList<>());
        addCart(cart);

        Cart retrieved = cartService.getCartByUserId(userId);
        assertNotNull(retrieved, "Cart should be retrieved by user ID");
        assertEquals(userId, retrieved.getUserId(), "User ID should match");
    }

    // Expect an exception when retrieving a cart for a non-existent user.
    @Test
    public void testGetCartByUserId_NonExistent() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                        cartService.getCartByUserId(UUID.randomUUID()),
                "Retrieving cart for a non-existent user should throw an exception");
    }

    // When multiple carts exist for a user, assume the service returns the first added cart.
    @Test
    public void testGetCartByUserId_MultipleCarts() {
        UUID userId = UUID.randomUUID();
        Cart cart1 = new Cart();
        cart1.setId(UUID.randomUUID());
        cart1.setUserId(userId);
        cart1.setProducts(new ArrayList<>());

        Cart cart2 = new Cart();
        cart2.setId(UUID.randomUUID());
        cart2.setUserId(userId);
        cart2.setProducts(new ArrayList<>());

        addCart(cart1);
        addCart(cart2);

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
        Cart cart = new Cart();
        cart.setId(cartId);
        cart.setUserId(UUID.randomUUID());
        cart.setProducts(new ArrayList<>());
        addCart(cart);

        Product product = new Product(UUID.randomUUID(), "Test Product", 15.0);
        cartService.addProductToCart(cartId, product);

        Cart updatedCart = getCarts().stream().filter(c -> c.getId().equals(cartId)).findFirst().orElse(null);
        assertNotNull(updatedCart, "Cart should exist after adding a product");
        assertFalse(updatedCart.getProducts().isEmpty(), "Cart should have at least one product");
        assertEquals(product.getId(), updatedCart.getProducts().get(0).getId(), "The added product's ID should match");
    }

    // Expect exception when adding a product to a non-existent cart.
    @Test
    public void testAddProductToCart_NonExistentCart() {
        Product product = new Product(UUID.randomUUID(), "Test Product", 15.0);
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                        cartService.addProductToCart(UUID.randomUUID(), product),
                "Adding a product to a non-existent cart should throw an exception");
    }

    @Test
    public void testAddProductToCart_MultipleProducts() {
        UUID cartId = UUID.randomUUID();
        Cart cart = new Cart();
        cart.setId(cartId);
        cart.setUserId(UUID.randomUUID());
        cart.setProducts(new ArrayList<>());
        addCart(cart);

        Product product1 = new Product(UUID.randomUUID(), "Product 1", 10.0);
        Product product2 = new Product(UUID.randomUUID(), "Product 2", 20.0);
        cartService.addProductToCart(cartId, product1);
        cartService.addProductToCart(cartId, product2);

        Cart updatedCart = getCarts().stream().filter(c -> c.getId().equals(cartId)).findFirst().orElse(null);
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
        Cart cart = new Cart();
        cart.setId(cartId);
        cart.setUserId(UUID.randomUUID());
        cart.setProducts(products);
        addCart(cart);

        cartService.deleteProductFromCart(cartId, product);
        Cart updatedCart = getCarts().stream().filter(c -> c.getId().equals(cartId)).findFirst().orElse(null);
        assertNotNull(updatedCart, "Cart should exist after deletion");
        assertTrue(updatedCart.getProducts().isEmpty(), "Product should be removed from the cart");
    }

    // When attempting to delete a product not present in the cart, expect an exception.
    @Test
    public void testDeleteProductFromCart_ProductNotPresent() {
        UUID cartId = UUID.randomUUID();
        Cart cart = new Cart();
        cart.setId(cartId);
        cart.setUserId(UUID.randomUUID());
        cart.setProducts(new ArrayList<>());
        addCart(cart);

        // Create a product that was never added.
        Product product = new Product(UUID.randomUUID(), "Non-existent Product", 15.0);
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                        cartService.deleteProductFromCart(cartId, product),
                "Deleting a product that is not present should throw an exception");
        Cart updatedCart = getCarts().stream().filter(c -> c.getId().equals(cartId)).findFirst().orElse(null);
        assertNotNull(updatedCart, "Cart should still exist");
    }

    // Expect exception when deleting a product from a non-existent cart.
    @Test
    public void testDeleteProductFromCart_NonExistentCart() {
        Product product = new Product(UUID.randomUUID(), "Test Product", 15.0);
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                        cartService.deleteProductFromCart(UUID.randomUUID(), product),
                "Deleting product from a non-existent cart should throw an exception");
    }

    // ============================================================
    // Tests for deleteCartById() method (3 tests)
    // ============================================================

    @Test
    public void testDeleteCartById_HappyPath() {
        UUID cartId = UUID.randomUUID();
        Cart cart = new Cart();
        cart.setId(cartId);
        cart.setUserId(UUID.randomUUID());
        cart.setProducts(new ArrayList<>());
        addCart(cart);

        cartService.deleteCartById(cartId);
        Cart retrieved = getCarts().stream().filter(c -> c.getId().equals(cartId)).findFirst().orElse(null);
        assertNull(retrieved, "Cart should be deleted and not retrievable");
    }

    // Expect exception when deleting a non-existent cart.
    @Test
    public void testDeleteCartById_NonExistent() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                        cartService.deleteCartById(UUID.randomUUID()),
                "Deleting a non-existent cart should throw an exception");
    }

    @Test
    public void testDeleteCartById_MultipleCarts() {
        Cart cart1 = new Cart();
        cart1.setId(UUID.randomUUID());
        cart1.setUserId(UUID.randomUUID());
        cart1.setProducts(new ArrayList<>());

        Cart cart2 = new Cart();
        cart2.setId(UUID.randomUUID());
        cart2.setUserId(UUID.randomUUID());
        cart2.setProducts(new ArrayList<>());

        addCart(cart1);
        addCart(cart2);

        cartService.deleteCartById(cart1.getId());
        List<Cart> carts = getCarts();
        assertEquals(1, carts.size(), "Only one cart should remain after deletion");
        assertEquals(cart2.getId(), carts.get(0).getId(), "The remaining cart should be the one not deleted");
    }
}
