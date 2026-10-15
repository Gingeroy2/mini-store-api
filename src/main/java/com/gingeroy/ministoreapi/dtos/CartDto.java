package com.gingeroy.ministoreapi.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class CartDto {
//    DTO 是给前端展示用的数据载体，不是业务模型
//    前端购物车页面，需要有序展示（加入购物车的先后顺序）
    private UUID id;
    private List<CartItemDto> cartItems = new ArrayList<>();
    private BigDecimal totalPrice = BigDecimal.ZERO;
}
