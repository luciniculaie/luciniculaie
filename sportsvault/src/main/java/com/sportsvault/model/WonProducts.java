package com.sportsvault.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import java.util.Date;

import java.util.UUID;

@Entity
@Table(name = "won_products")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WonProducts {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(java.sql.Types.VARCHAR)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User bidder;

    @Column(name = "won_date")
    private Date wonDate;

    public Date getWonDate() {
        return wonDate;
    }

    public void setWonDate(Date wonDate) {
        this.wonDate = wonDate;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public User getBidder() {
        return bidder;
    }

    public void setBidder(User bidder) {
        this.bidder = bidder;
    }
}
