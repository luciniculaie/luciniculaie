package com.sportsvault.repository;

import com.sportsvault.model.Attribute;
import com.sportsvault.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PhotoRepository extends JpaRepository<Photo, UUID> {
    Optional<Photo> findById(UUID id);

    @Query("Select p.name from Photo p where p.productId = :productId")
    List<String> getProductPhotos(@Param("productId") UUID productId);
}
