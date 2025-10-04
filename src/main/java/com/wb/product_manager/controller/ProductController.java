package com.wb.product_manager.controller;

import com.wb.product_manager.domain.Product;
import com.wb.product_manager.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @RestController: Esta é uma anotação especializada do Spring que combina duas outras:
 * - @Controller: Marca a classe como um controlador do padrão MVC (Model-View-Controller).
 * - @ResponseBody: Indica que o valor de retorno dos métodos deve ser escrito diretamente
 * no corpo da resposta HTTP. O Spring automaticamente converte o objeto Java (Product)
 * para o formato JSON.
 */
@RestController

/**
 * @RequestMapping: Mapeia as requisições para os métodos do controlador.
 * Quando aplicada a nível de classe, ela define um "prefixo" para todos os
 * endpoints dentro deste controlador. Ou seja, todos os endpoints aqui
 * começarão com "/api/products".
 */
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Product> create(@RequestBody Product product) {
        Product createdProduct = productService.create(product);

        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Product>> findAll() {
        List<Product> products = productService.findAll();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> findById(@PathVariable Long id) {
        Product product = productService.findById(id);
        return ResponseEntity.ok(product);
    }
}
