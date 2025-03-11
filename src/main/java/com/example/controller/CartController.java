package com.example.controller;

import com.example.model.Cart;
import com.example.model.Product;
import com.example.service.CartService;
import com.example.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.UUID;

@RestController
@RequestMapping("/cart")
public class CartController {

    CartService cartService;
    ProductService productService;

    @Autowired
    public CartController(CartService cartService, ProductService productService){
        this.cartService = cartService;
        this.productService = productService;
    }

    @PostMapping("/")
    public Cart addCart(@RequestBody Cart cart){
        return cartService.addCart(cart);
    }

    @GetMapping("/")
    public ArrayList<Cart> getCarts(){
        return cartService.getCarts();
    }

    @GetMapping("/{cartId}")
    public Cart getCartById(@PathVariable UUID cartId){
        return cartService.getCartById(cartId);
    }

    @PutMapping("/addProduct/{cartId}")
    public String addProductToCart(@PathVariable UUID cartId, @RequestBody Product product){
        try {
            cartService.addProductToCart(cartId, product);
            return "Product added to cart";
        }
        catch (IllegalArgumentException e){
            return e.getMessage();
        }
    }

    @PutMapping("/deleteProductFromCart")
    public String deleteProductFromCart(@RequestParam UUID userId, @RequestParam UUID productId){
        try {
            Product product = productService.getProductById(productId);
            cartService.deleteProductFromCart(userId, product);
            return "Product deleted from cart";
        }
        catch (IllegalArgumentException e){
            return e.getMessage();
        }
    }

    @DeleteMapping("/delete/{cartId}")
    public String deleteCartById(@PathVariable UUID cartId){
        try{
            cartService.deleteCartById(cartId);
            return "Cart deleted successfully";
        }
        catch (IllegalArgumentException e){
            return e.getMessage();
        }
    }
}
