package com.example.repository;

import com.example.model.Product;
import com.example.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.UUID;

@Repository
@SuppressWarnings("rawtypes")
public class ProductRepository extends MainRepository<Product> {

    public ProductRepository() {

    }
    @Override
    protected String getDataPath() {
        return "src/main/java/com/example/data/products.json";
    }

    @Override
    protected Class<Product[]> getArrayType() {
        return Product[].class;
    }

    public Product addProduct(Product product) {
        if (getProductById(product.getId()) != null) {
            System.out.print("Product Already Exists!");
            return null;
        } else {
            System.out.print("Product Added!");
            save(product);

        }
        return product;
    }

    public ArrayList<Product> getProducts() {
        return findAll();
    }

    public Product getProductById(UUID productId) {
        for(Product product : findAll()){
            if(product.getId().equals(productId)){
                System.out.print("Product Found!");
                return product;
            }
        }
        System.out.print("No Product Found");
        return null;
    }

    public Product updateProduct(UUID productId, String newName, double newPrice) {
        ArrayList<Product> products = findAll();

        for (Product product : products) {
            if (product.getId().equals(productId)) {
                product.setName(newName);
                product.setPrice(newPrice);
                overrideData(products);
                System.out.println("Product updated successfully!");
                return product;
            }
        }
        System.out.println("Product does not exist!");
        return null;
    }

    public void applyDiscount(double discount, ArrayList<UUID> productIds) {
        ArrayList<Product> products = findAll();

        for (Product product : products) {
            if (productIds.contains(product.getId())) {
                double newPrice = product.getPrice() - (product.getPrice() * discount / 100);
                product.setPrice(newPrice);
            }
        }
        overrideData(products);
        System.out.println("Discount applied successfully!");
    }


    public void deleteProductById(UUID productId) {
        ArrayList<Product> products = findAll();

        if (products.removeIf(product -> product.getId().equals(productId))) {
            overrideData(products);
            System.out.println("Product deleted successfully!");
        } else {
            System.out.println("Product does not exist!");
        }
    }

}
