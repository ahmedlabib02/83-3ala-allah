package com.example.service;

import com.example.model.Cart;
import com.example.model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
@SuppressWarnings("rawtypes")
public class CartService extends MainService<Cart> {

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

    public void addProductToCart(UUID cartId, Product product) {}

    public void deleteProductFromCart(UUID cartId, Product product) {}

    public void deleteCartById(UUID cartId) {}

}
