package com.gingeroy.ministoreapi.repositories;

import com.gingeroy.ministoreapi.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}