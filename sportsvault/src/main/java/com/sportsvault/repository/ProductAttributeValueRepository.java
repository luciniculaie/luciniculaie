package com.sportsvault.repository;

import com.sportsvault.model.ProductAttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProductAttributeValueRepository extends JpaRepository<ProductAttributeValue, UUID> {
    Optional<ProductAttributeValue> findById(UUID id);
}
