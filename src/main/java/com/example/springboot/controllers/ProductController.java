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
import org.springframework.web.bind.annotation.PostMapping;
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

    // Método para listar todos os produtos
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProducts() {
       return ResponseEntity.status(HttpStatus.OK).body(productRepository.findAll());
    }

    // Método para buscar um produto por ID
    @GetMapping("/products/{id}")
    public ResponseEntity<Object> getProduct(@PathVariable("id") UUID id) {
        Optional<Product> product = productRepository.findById(id);
    
        if (product.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
        }
    
        return ResponseEntity.status(HttpStatus.OK).body(product.get());
    }

    // Método para criar um produto (utilizando POST)
    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@RequestBody @Valid ProductDTO productDTO) {
        Product product = new Product();
        BeanUtils.copyProperties(productDTO, product);
        return ResponseEntity.status(HttpStatus.CREATED).body(productRepository.save(product));
    }

    // Método para atualizar um produto existente
    @PutMapping("/products/{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable("id") UUID id, @RequestBody @Valid ProductDTO productDTO) {
        Optional<Product> existingProduct = productRepository.findById(id);
        
        if (existingProduct.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
        }

        Product product = existingProduct.get();
        BeanUtils.copyProperties(productDTO, product, "id");
        return ResponseEntity.status(HttpStatus.OK).body(productRepository.save(product));
    }

    // Método para deletar um produto
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable("id") UUID id) {
        Optional<Product> existingProduct = productRepository.findById(id);
        
        if (existingProduct.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
        }

        productRepository.delete(existingProduct.get());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
