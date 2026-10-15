package com.gingeroy.ministoreapi.services;

import com.gingeroy.ministoreapi.dtos.CartDto;
import com.gingeroy.ministoreapi.dtos.CartItemDto;
import com.gingeroy.ministoreapi.entities.Cart;
import com.gingeroy.ministoreapi.entities.CartItem;
import com.gingeroy.ministoreapi.entities.Product;
import com.gingeroy.ministoreapi.exceptions.CartNotFoundException;
import com.gingeroy.ministoreapi.exceptions.ProductNotFoundException;
import com.gingeroy.ministoreapi.mappers.CartMapper;
import com.gingeroy.ministoreapi.repositories.CartRepository;
import com.gingeroy.ministoreapi.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class CartService {
    private CartRepository cartRepository;
    private CartMapper cartMapper;
    private ProductRepository productRepository;

    @Transactional
    public CartDto createCart(){
        Cart cart = new Cart();
        cartRepository.save(cart);
        return cartMapper.toCartDto(cart);
    }

    @Transactional
    public CartItemDto addToCart(UUID cartId, Long productId){
        // 购物车是否存在
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if(cart == null){
            throw new CartNotFoundException();
        }

        // 请求的商品是否存在
        Product product = productRepository.findById(productId).orElse(null);
        if(product == null){
            throw new ProductNotFoundException();
        }

        // 购物车中是否已有该商品 - 有->数量++，无->新增
        CartItem cartItem = cart.addCartItem(product);

        cartRepository.save(cart);
        return cartMapper.toCartItemDto(cartItem);
    }

    @Transactional
    public CartDto getCart(UUID cartId){
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null){
            throw new CartNotFoundException();
        }

        return cartMapper.toCartDto(cart);
    }

    @Transactional
    public CartItemDto updateCartItem(UUID cartId, Long productId, Integer quantity){
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null){
            throw new CartNotFoundException();
        }

        CartItem cartItem = cart.getCartItem(productId);
        if (cartItem == null){
            throw new ProductNotFoundException();
        }

        cartItem.setQuantity(quantity);
        cartRepository.save(cart);

        return cartMapper.toCartItemDto(cartItem);
    }

    @Transactional
    public void removeCartItem(UUID cartId,Long productId){
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null){
            throw new CartNotFoundException();
        }

        CartItem cartItem = cart.getCartItem(productId);
        if (cartItem == null){
            throw new ProductNotFoundException();
        }

        cart.getCartItems().remove(cartItem);
        cartRepository.save(cart);
        cartItem.setCart(null);
    }

    @Transactional
    public void clearCartItems(UUID cartId){
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null){
            throw new CartNotFoundException();
        }

        cart.clearCartItems();
        cartRepository.save(cart);
    }
}
