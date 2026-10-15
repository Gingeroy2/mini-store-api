package com.gingeroy.ministoreapi.controllers;

import com.gingeroy.ministoreapi.dtos.AddItemToCartRequest;
import com.gingeroy.ministoreapi.dtos.CartDto;
import com.gingeroy.ministoreapi.dtos.CartItemDto;
import com.gingeroy.ministoreapi.dtos.UpdateCartItemRequest;
import com.gingeroy.ministoreapi.services.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/carts")
@Tag(name = "Carts")
public class CartController {
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<CartDto> createCart(UriComponentsBuilder uriBuilder){
        CartDto cartDto = cartService.createCart();

        var uri = uriBuilder.path("/carts/{id}").buildAndExpand(cartDto.getId()).toUri();
        return ResponseEntity.created(uri).body(cartDto);
//        return new ResponseEntity<>(cartDto, HttpStatus.CREATED);
    }

    @PostMapping("/{cartId}/items")
    @Operation(summary = "Add a product to the cart")
    public ResponseEntity<CartItemDto> addToCart(
            @Parameter(description = "The ID of the cart")
            @PathVariable UUID cartId,
            @RequestBody AddItemToCartRequest request){

        CartItemDto cartItemDto = cartService.addToCart(cartId, request.getProductId());
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
    }

    @GetMapping("/{cartId}")
    public CartDto getCart(@PathVariable UUID cartId){
        /*CartDto cartDto = cartService.getCart(cartId);
        return ResponseEntity.ok(cartDto);*/
        return cartService.getCart(cartId);
    }

    @PutMapping("/{cartId}/items/{productId}")
    public CartItemDto updateCartItem(
            @PathVariable("cartId") UUID cartId,
            @PathVariable("productId") Long productId,
            @Valid @RequestBody UpdateCartItemRequest request){
        /*Cart cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Cart not found"));
        }

        CartItem cartItem = cart.getCartItem(productId);
        *//*CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);*//*
        if (cartItem == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Product was not found in the cart"));
        }

        cartItem.setQuantity(request.getQuantity());
        cartRepository.save(cart);*/

        /*CartItemDto cartItemDto = cartService.updateCartItem(cartId, productId, request.getQuantity());
        return ResponseEntity.ok(cartItemDto);*/
        return cartService.updateCartItem(cartId, productId, request.getQuantity());
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> removeCartItem(
            @PathVariable("cartId") UUID cartId,
            @PathVariable("productId") Long productId){

        cartService.removeCartItem(cartId, productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<Void> clearCartItems(@PathVariable("cartId") UUID cartId){

        cartService.clearCartItems(cartId);
        return ResponseEntity.noContent().build();
    }
}
