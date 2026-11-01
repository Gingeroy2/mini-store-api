package com.gingeroy.ministoreapi.services;


import com.gingeroy.ministoreapi.dtos.ProductDto;
import com.gingeroy.ministoreapi.entities.Category;
import com.gingeroy.ministoreapi.entities.Product;
import com.gingeroy.ministoreapi.exceptions.CategoryNotFoundException;
import com.gingeroy.ministoreapi.exceptions.ProductNotExistException;
import com.gingeroy.ministoreapi.mappers.ProductMapper;
import com.gingeroy.ministoreapi.repositories.CategoryRepository;
import com.gingeroy.ministoreapi.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    public List<ProductDto> getAllProducts(Byte categoryId) {
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

    public ProductDto getProduct(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            throw new ProductNotExistException();
        }
        return productMapper.toProductDto(product);
    }

    @Transactional
    public ProductDto createProduct(ProductDto productDto){
        Category category = categoryRepository.findById(productDto.getCategoryId()).orElse(null);
        if (category == null){
            throw new CategoryNotFoundException();
        }

        Product product = productMapper.toEntity(productDto);
        product.setCategory(category);
        productRepository.save(product);

        return productMapper.toProductDto(product);
    }

    @Transactional
    public ProductDto updateProduct(Long id, ProductDto productDto){
        Category category = categoryRepository.findById(productDto.getCategoryId()).orElse(null);
        if (category == null){
            throw new CategoryNotFoundException();
        }

        Product product = productRepository.findById(id).orElse(null);
        if (product == null){
            throw new ProductNotExistException();
        }

        product.setCategory(category);
        productMapper.update(productDto, product);
        productRepository.save(product);

        return productMapper.toProductDto(product);
    }

    @Transactional
    public void deleteProduct(Long id){
        Product product = productRepository.findById(id).orElse(null);
        if (product == null){
            throw new ProductNotExistException();
        }

        productRepository.delete(product);
    }
}
