package com.gingeroy.ministoreapi.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "date_created",insertable = false, updatable = false)
    private LocalDate dateCreated;

    @OneToMany(mappedBy = "cart",
            cascade = CascadeType.MERGE,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private Set<CartItem> cartItems = new HashSet<>();

    public BigDecimal getTotalPrice(){
        /*BigDecimal totalPrice = BigDecimal.ZERO;
        for (CartItem cartItem : cartItems) {
            totalPrice = totalPrice.add(cartItem.getTotalPrice());
        }*/

        return cartItems.stream()
                .map(cartItem -> cartItem.getTotalPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public CartItem getCartItem(Long productId) {
        return cartItems.stream()
                .filter(cartItem -> cartItem.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);
    }

    public CartItem addCartItem(Product product){
        CartItem cartItem = getCartItem(product.getId());

        if(cartItem == null){
//            无->新增
            cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(1);
            cartItem.setCart(this);
            cartItems.add(cartItem);
        }else{
            cartItem.setQuantity(cartItem.getQuantity() + 1);
        }
        return cartItem;
    }

    private void removeCartItem(Long productId){
        CartItem cartItem = getCartItem(productId);
        if(cartItem != null){
            cartItems.remove(cartItem);
            cartItem.setCart(null);
        }
    }

    public void clearCartItems() {
        cartItems.clear();
    }
}
