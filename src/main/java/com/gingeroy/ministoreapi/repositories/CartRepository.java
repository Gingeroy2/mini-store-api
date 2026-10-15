package com.gingeroy.ministoreapi.repositories;

import com.gingeroy.ministoreapi.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {
}
