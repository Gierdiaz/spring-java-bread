package com.example.springboot.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.springboot.DTO.ProductDTO;
import com.example.springboot.models.Product;
import com.example.springboot.repositories.ProductRepository;

import jakarta.validation.Valid;


@RestController
public class ProductController {
    @Autowired
    ProductRepository productRepository;

    @GetMapping("/products")
    public ResponseEntity<List<Product>> Index() {
       return ResponseEntity.status(HttpStatus.OK).body(productRepository.findAll());
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Object> Show(@PathVariable("id") UUID id) {
        Optional<Product> product = productRepository.findById(id);
    
        if (product.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
        }
    
        return ResponseEntity.status(HttpStatus.OK).body(product.get());
    }
    
    @GetMapping("/product")
    public ResponseEntity<Product> Store(@RequestBody @Valid ProductDTO productDTO) {
        Product product = new Product();
        BeanUtils.copyProperties(productDTO, product);
        return ResponseEntity.status(HttpStatus.CREATED).body(productRepository.save(product));
    }

    @PutMapping("/product/{id}")
    public ResponseEntity<Object> Update(@PathVariable("id") UUID id, @RequestBody @Valid ProductDTO productDTO) {
        Optional<Product> existingProduct = productRepository.findById(id);
        
        if (existingProduct.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
        }

        Product product = existingProduct.get();
        BeanUtils.copyProperties(productDTO, product, "id");
        return ResponseEntity.status(HttpStatus.OK).body(productRepository.save(product));
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<Object> Delete(@PathVariable("id") UUID id) {
        Optional<Product> existingProduct = productRepository.findById(id);
        
        if (existingProduct.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
        }

        productRepository.delete(existingProduct.get());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
