package com.gingeroy.ministoreapi.controllers;

import com.gingeroy.ministoreapi.dtos.ProductDto;
import com.gingeroy.ministoreapi.entities.Category;
import com.gingeroy.ministoreapi.entities.Product;
import com.gingeroy.ministoreapi.mappers.ProductMapper;
import com.gingeroy.ministoreapi.repositories.CategoryRepository;
import com.gingeroy.ministoreapi.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    @GetMapping
    public List<ProductDto> getAllProducts
            (@RequestParam(name = "categoryId", defaultValue = "", required = false) Byte categoryId) {

        List<Product> products;
        if(categoryId != null){
            products = productRepository.findByCategoryId(categoryId);
        }else{
            products = productRepository.findAllWithCategory();
        }
        return products
                .stream()
                .map(product -> productMapper.toProductDto(product))
                .toList();
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long categoryId) {
        Product product = productRepository.findById(categoryId).orElseThrow();
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productMapper.toProductDto(product));
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(
            @RequestBody ProductDto productDto,
            UriComponentsBuilder uriBuilder){
        Category category = categoryRepository.findById(productDto.getCategoryId()).orElse(null);
        if (category == null){
            return ResponseEntity.badRequest().build();
        }

        Product product = productMapper.toEntity(productDto);
        product.setCategory(category);
        productRepository.save(product);
        productDto.setId(product.getId());

        var uri = uriBuilder.path("/products/{id}").buildAndExpand(product.getId()).toUri();
        return ResponseEntity.created(uri).body(productDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable(name = "id") Long id,
            @RequestBody ProductDto productDto){
        Category category = categoryRepository.findById(productDto.getCategoryId()).orElse(null);
        if (category == null){
            return ResponseEntity.badRequest().build();
        }

        Product product = productRepository.findById(id).orElse(null);
        if (product == null){
            return ResponseEntity.notFound().build();
        }

        product.setCategory(category);
        productMapper.update(productDto, product);
        productRepository.save(product);
        productDto.setId(product.getId());
        return ResponseEntity.ok(productDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable(name = "id") Long id){
        Product product = productRepository.findById(id).orElse(null);
        if (product == null){
            return ResponseEntity.notFound().build();
        }

        productRepository.delete(product);
        return ResponseEntity.noContent().build();
    }
}
