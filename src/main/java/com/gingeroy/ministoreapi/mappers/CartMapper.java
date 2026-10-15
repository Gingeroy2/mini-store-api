package com.gingeroy.ministoreapi.mappers;

import com.gingeroy.ministoreapi.dtos.CartDto;
import com.gingeroy.ministoreapi.dtos.CartItemDto;
import com.gingeroy.ministoreapi.dtos.CartProductDto;
import com.gingeroy.ministoreapi.entities.Cart;
import com.gingeroy.ministoreapi.entities.CartItem;
import com.gingeroy.ministoreapi.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {
    @Mapping(target = "totalPrice", expression = "java(cart.getTotalPrice())")
    CartDto toCartDto(Cart cart);

    @Mapping(target = "totalPrice", expression = "java(cartItem.getTotalPrice())")
    CartItemDto toCartItemDto(CartItem cartItem);

    CartProductDto toCartProductDto(Product Product);
}
