package com.sportsvault.repository;

import com.sportsvault.dto.ProductDTO;
import com.sportsvault.model.Product;
import com.sportsvault.model.ProductQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    @Query("select p from Product p where p.id in :ids and p.state = 'SELLING'")
    List<Product> findProductsByIds(@Param("ids") List<UUID> ids);

    @Query("select p from Product p where p.state = 'SELLING'")
    List<Product> getAvailableProducts();

    @Query("select p.id from Product p where p.state = 'SELLING'")
    List<UUID> getAvailableProductsIds();
    Optional<Product> findById(UUID ids);

    @Query("SELECT p FROM Product p WHERE DAY(p.expirationDate) = DAY(NOW()) AND MONTH(p.expirationDate) = MONTH(NOW()) " +
            "AND HOUR(p.expirationDate) = HOUR(NOW()) AND YEAR(p.expirationDate) = YEAR(NOW()) and p.state = 'SELLING'")
    List<Product> getProductListByDate();

    @Query("Select wp.product from WonProducts wp where wp.bidder.id = :userId")
    List<Product> getWonProducts(@Param("userId") UUID userId);

    @Query("select p from Product p where p.ownerId = :userId")
    List<Product> getMyProducts(@Param("userId") UUID userId);

    @Query("Select p.id from Product p")
    List<UUID> getAllIds();

    @Query("Select p from Product p where p.state = 'SELLING'")
    Optional<List<Product>> getProductList();

    @Query("Select p from Product p where p.state = 'SELLING' and (p.gender = :gender or p.gender = 'UNISEX')")
    Optional<List<Product>> getProductListByGender(@Param("gender") String gender);

    @Query("Select p from Product p where p.state = 'SELLING' and p.category.sport.name = :sport and (p.gender = :gender or p.gender='UNISEX')")
    Optional<List<Product>> getProductListByGenderAndSport(@Param("gender") String gender, @Param("sport") String sport);

    @Query("Select p from Product p where p.state = 'SELLING' and p.category.name = :category and (p.gender = :gender or p.gender='UNISEX')")
    Optional<List<Product>> getProductListByGenderAndCategory(@Param("gender") String gender, @Param("category") String category);

    @Query(value = "SELECT id FROM products WHERE state = 'SELLING' AND MATCH(name, description) AGAINST (:search IN BOOLEAN MODE)", nativeQuery = true)
    Optional<List<UUID>> searchProducts(@Param("search") String search);
}
