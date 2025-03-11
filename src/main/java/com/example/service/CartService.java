package com.example.service;

import com.example.model.Cart;
import com.example.model.Product;
import com.example.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
@SuppressWarnings("rawtypes")
public class CartService extends MainService<Cart> {

    CartRepository cartRepository;

    @Autowired
    public CartService(CartRepository cartRepository){
        this.cartRepository = cartRepository;
    }

    public Cart addCart(Cart cart){
        if(cart == null)
            throw  new IllegalArgumentException("Cart cannot be null");
        if(cart.getId() == null)
            cart.setId(UUID.randomUUID());
        return cartRepository.addCart(cart);
    }

    public ArrayList<Cart> getCarts(){
        return cartRepository.getCarts();
    }

    public Cart getCartById(UUID cartId){
        Cart cart = cartRepository.getCartById(cartId);
        if(cart == null)
            throw new IllegalArgumentException("Cart not found");
        return cart;
    }

    public Cart getCartByUserId(UUID userId){
        Cart cart = cartRepository.getCartByUserId(userId);
        if(cart == null)
            throw new IllegalArgumentException("Cart not found");
        return cart;
    }

    public void addProductToCart(UUID cartId, Product product){
        Cart cart = cartRepository.getCartById(cartId);
        if(cart == null)
            throw new IllegalArgumentException("Cart not found");
        cartRepository.addProductToCart(cartId, product);
    }

    public void deleteProductFromCart(UUID cartId, Product product){
        Cart cart = cartRepository.getCartById(cartId);
        if(cart == null)
            throw new IllegalArgumentException("Cart not found");
        if(product == null)
            throw new IllegalArgumentException("Product cannot be null");
        cartRepository.deleteProductFromCart(cartId, product);
    }

    public void deleteCartById(UUID cartId){
        Cart cart = cartRepository.getCartById(cartId);
        if(cart == null)
            throw new IllegalArgumentException("Cart not found");
        cartRepository.deleteCartById(cartId);
    }
}
