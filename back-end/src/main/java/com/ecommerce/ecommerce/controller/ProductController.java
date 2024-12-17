package com.ecommerce.ecommerce.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController
{
    @GetMapping("/products")
    public String getProducts() {
        return "List of products";  // Simulate returning product data
    }
}
