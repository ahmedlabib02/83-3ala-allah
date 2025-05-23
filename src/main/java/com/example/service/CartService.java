package com.example.service;

import com.example.model.Cart;
import com.example.model.Product;
import com.example.repository.CartRepository;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
@SuppressWarnings("rawtypes")
public class CartService extends MainService<Cart> {

    CartRepository cartRepository;
    UserRepository userRepository;

    @Autowired
    public CartService(CartRepository cartRepository, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
    }

    public Cart addCart(Cart cart){
        if(cart == null)
            throw  new IllegalArgumentException("Cart cannot be null");
        if(cartRepository.getCartById(cart.getId()) != null)
            throw new IllegalArgumentException("Cart already exists");
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

        if(userRepository.getUserById(userId) == null)
            throw new IllegalArgumentException("User not found");

        return  cartRepository.getCartByUserId(userId);

    }

    public void addProductToCart(UUID cartId, Product product){
        Cart cart = cartRepository.getCartById(cartId);
        if(cart == null)
            throw new IllegalArgumentException("Cart not found");
        cartRepository.addProductToCart(cartId, product);
    }

    public void deleteProductFromCart(UUID cartId, Product product) {
        if (cartId == null) {
            throw new IllegalArgumentException("Cart ID cannot be null");
        }
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }

        Cart cart = cartRepository.getCartById(cartId);


        if( cart==null || cart.getProducts().isEmpty() )
            throw new IllegalArgumentException("Cart is empty");

        boolean exists = cart.getProducts().stream()
                .anyMatch(p -> p.getId().equals(product.getId()));

        if (exists) {
            cartRepository.deleteProductFromCart(cartId, product);
        } else {
            throw new IllegalArgumentException("Product not found in cart");
        }
    }


    public void deleteCartById(UUID cartId){
        Cart cart = cartRepository.getCartById(cartId);
        if(cart == null)
            throw new IllegalArgumentException("Cart not found");
        cartRepository.deleteCartById(cartId);
    }
}
