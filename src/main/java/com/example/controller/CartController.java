package com.example.controller;

import com.example.model.Cart;
import com.example.model.Product;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.UUID;

@RestController
@RequestMapping("/cart")
public class CartController {

    @PostMapping("/")
    public Cart addCart(@RequestBody Cart cart) {
        return null;
    }

    @GetMapping("/")
    public ArrayList<Cart> getCarts() {
        return null;
    }

    @GetMapping("/{cartId}")
    public Cart getCartById(@PathVariable UUID cartId) {
        return null;
    }

    @PutMapping("/addProduct/{cartId}")
    public String addProductToCart(@PathVariable UUID cartId, @RequestBody Product product) {
        return null;
    }

}
