package com.example.controller;

import com.example.model.Product;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/product")
public class ProductController {

    @PostMapping("/")
    public Product addProduct(@RequestBody Product product) {
        return null;
    }

    @GetMapping("/")
    public ArrayList<Product> getProducts() {
        return null;
    }

    @GetMapping("/{productId}")
    public Product getProductById(@PathVariable UUID productId) {
        return null;
    }

    @PutMapping("/update/{productId}")
    public Product updateProduct(@PathVariable UUID productId, @RequestBody Map<String,Object>
            body) {
        return null;
    }

    @PutMapping("/applyDiscount")
    public String applyDiscount(@RequestParam double discount,@RequestBody ArrayList<UUID>
            productIds) {
        return null;
    }

    @DeleteMapping("/delete/{productId}")
    public String deleteProductById(@PathVariable UUID productId) {
        return null;
    }

}
