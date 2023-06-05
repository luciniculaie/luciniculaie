package com.sportsvault.repository;

import com.sportsvault.model.FavouriteProduct;
import com.sportsvault.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface FavouriteProductRepository extends JpaRepository<FavouriteProduct, UUID> {

    @Query("select fp.product.id from FavouriteProduct fp where fp.userId = :userId and fp.product.state = 'SELLING'")
    List<UUID> getFavouriteIds(@Param("userId") UUID userId);

    @Query("select fp.product from FavouriteProduct fp where fp.userId = :userId and fp.product.state = 'SELLING'")
    List<Product> getFavourites(@Param("userId") UUID userId);

    @Query("select fp.id from FavouriteProduct fp where fp.product.id = :productId and fp.userId = :userId")
    UUID findByProductAndUser(@Param("productId") UUID productId, @Param("userId") UUID userId);

}
