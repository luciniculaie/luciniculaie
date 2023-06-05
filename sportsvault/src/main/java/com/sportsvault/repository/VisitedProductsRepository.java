package com.sportsvault.repository;

import com.sportsvault.model.Product;
import com.sportsvault.model.VisitedProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface VisitedProductsRepository extends JpaRepository<VisitedProduct, UUID> {

    @Query(value = "SELECT pv.product_id, pv.created_date FROM visited_products pv WHERE pv.user_id = :userId ORDER BY pv.created_date DESC LIMIT 20", nativeQuery = true)
    List<Object[]> findTop20ByUserIdOrderByCreatedDateDesc(@Param("userId") String userId);

    @Query(value = "Select vp.product.id from VisitedProduct vp where vp.userId=:userId and vp.product.state='SELLING'")
    List<UUID> getVisitedProductsByUserId(@Param("userId") UUID userId);


}
