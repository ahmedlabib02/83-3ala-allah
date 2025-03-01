package com.example.repository;

import com.example.model.Cart;
import com.example.model.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.UUID;

@Repository
@SuppressWarnings("rawtypes")
public class CartRepository {

    public CartRepository() {

    }

    public Cart addCart(Cart cart) {
        return null;
    }

    public ArrayList<Cart> getCarts() {
        return null;
    }

    public Cart getCartById(UUID cartId) {
        return null;
    }

    public Cart getCartByUserId(UUID userId) {
        return null;
    }

    public void addProductToCart(UUID cartId, Product product) {

    }

    public void deleteProductFromCart(UUID cartId, Product product) {

    }

    public void deleteCartById(UUID cartId) {

    }

}
