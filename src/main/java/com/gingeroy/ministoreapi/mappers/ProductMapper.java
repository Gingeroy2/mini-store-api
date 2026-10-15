package com.gingeroy.ministoreapi.mappers;

import com.gingeroy.ministoreapi.dtos.ProductDto;
import com.gingeroy.ministoreapi.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
//    mapstruct 将自动实现此接口，可以声明各种映射方法

    //    将产品映射到产品DTO
    @Mapping(target = "categoryId", source = "category.id")
//    进入源对象的 category.id，映射到目标对象的 categoryId
    ProductDto toProductDto(Product product);

    Product toEntity(ProductDto productDto);

    @Mapping(target = "id",ignore = true)
    void update(ProductDto productDto, @MappingTarget Product product);
}
