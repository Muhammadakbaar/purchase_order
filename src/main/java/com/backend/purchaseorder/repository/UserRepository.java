package com.backend.purchaseorder.repository;

import com.backend.purchaseorder.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}