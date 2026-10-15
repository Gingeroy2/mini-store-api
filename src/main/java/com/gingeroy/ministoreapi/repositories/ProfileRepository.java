package com.gingeroy.ministoreapi.repositories;

import com.gingeroy.ministoreapi.entities.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
