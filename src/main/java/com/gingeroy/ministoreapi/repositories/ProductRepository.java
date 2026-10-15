package com.gingeroy.ministoreapi.repositories;

import com.gingeroy.ministoreapi.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryId(Byte categoryId);

//    自定义查询
    @Query(value = "select p.* from products p join categories on p.category_id = categories.id", nativeQuery = true)
    List<Product> findAllWithCategory();

}