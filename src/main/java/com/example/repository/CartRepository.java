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

    public void addProductToCart(UUID cartId, Product product) {
        ArrayList<Cart> carts = findAll();

        for (int i = 0; i < carts.size(); i++) {
            Cart cart = carts.get(i);
            if (cart.getId().equals(cartId)) {
                cart.getProducts().add(product);
                carts.set(i, cart);
                break;
            }
        }

        overrideData(carts);
    }


    public void deleteProductFromCart(UUID cartId, Product product) {
        ArrayList<Cart> carts = findAll();

        for (int i = 0; i < carts.size(); i++) {
            Cart cart = carts.get(i);
            if (cart.getId().equals(cartId)) {
                cart.getProducts().removeIf(p -> p.getId().equals(product.getId()));
                carts.set(i, cart);
                break;
            }
        }

        overrideData(carts);
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
