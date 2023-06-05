package com.sportsvault.repository;

import com.sportsvault.model.Bid;
import com.sportsvault.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface BidRepository extends JpaRepository<Bid, UUID> {

    @Query("SELECT b FROM Bid b WHERE b.product.id = :productId" +
            " AND b.bidAmount = (SELECT MAX(b2.bidAmount) FROM Bid b2 WHERE b2.product.id = :productId)")
    Bid getHighestBidForProduct(@Param("productId") UUID productId);

    @Query("select distinct b.product from Bid b where b.bidder.id = :userId and b.product.state = 'SELLING'")
    List<Product> getWatchlist(@Param("userId") UUID userId);

    @Query("select distinct b.product.id from Bid b where b.bidder.id = :userId and b.product.state = 'SELLING'")
    List<UUID> getWatchlistIds(@Param("userId") UUID userId);

    @Query("SELECT b.bidder.id " +
            "FROM Bid b " +
            "INNER JOIN (" +
            "  SELECT b.product.id AS productId, MAX(b.bidAmount) AS maxAmount " +
            "  FROM Bid b " +
            "  GROUP BY b.product.id " +
            ") max_bids ON b.product.id = max_bids.productId AND b.bidAmount = max_bids.maxAmount " +
            "WHERE b.product.id IN (" +
            "  SELECT DISTINCT b2.product.id " +
            "  FROM Bid b2 " +
            "  WHERE b2.bidder.id = :userId " +
            "    AND b2.product.state = 'SELLING' " +
            ") " +
            "ORDER BY b.product.id")
    List<UUID> getWatchListLeaders(@Param("userId") UUID userId);

    @Query(value = "SELECT distinct b.product_id, b.bid_date FROM bids b WHERE b.bidder_id = :userId ORDER BY b.bid_date DESC LIMIT 5", nativeQuery = true)
    List<Object[]> findTop5ByUserIdOrderByCreatedDateDesc(@Param("userId") String userId);
}
