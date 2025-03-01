package com.example.repository;

import com.example.model.Cart;
import com.example.model.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.UUID;

@Repository
@SuppressWarnings("rawtypes")
public class CartRepository extends MainRepository<Cart> {

    public CartRepository(){
    }

    @Override
    protected String getDataPath() {
        return "src/main/java/com/example/data/carts.json";
    }

    @Override
    protected Class getArrayType() {
        return Cart[].class;
    }

    public Cart addCart(Cart cart){
        save(cart);
        return cart;
    }

    public ArrayList<Cart> getCarts(){
        return findAll();
    }

    public Cart getCartById(UUID cartId){
        return findAll().stream()
                .filter(cart -> cart.getId().equals(cartId))
                .findFirst()
                .orElse(null);
    }

    public Cart getCartByUserId(UUID userId){
        return findAll().stream()
                .filter(cart -> cart.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }

    public void addProductToCart(UUID cartId, Product product){
        Cart cart = getCartById(cartId);
        cart.getProducts().add(product);
        save(cart);
    }

    public void deleteProductFromCart(UUID cartId, Product product){
        Cart cart = getCartById(cartId);
        cart.getProducts().remove(product);
        save(cart);
    }

    public void deleteCartById(UUID cartId){
        ArrayList<Cart> carts = getCarts();
        carts.removeIf(cart -> cart.getId().equals(cartId));
        overrideData(carts);
    }

    public void updateProductQuantity() {
        System.out.println("Product quantity updated");
    }

    public void getCartDetails() {
        System.out.println("Cart details fetched");
    }


}
