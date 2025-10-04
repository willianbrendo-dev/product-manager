package com.wb.product_manager.service;

import com.wb.product_manager.domain.Product;
import com.wb.product_manager.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product create(Product product) {
        // Antes de salvar, poderíamos adicionar regras de negócio aqui.
        return productRepository.save(product);
    }

    
}
