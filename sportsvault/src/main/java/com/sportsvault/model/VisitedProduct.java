package com.sportsvault.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "visited_products")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VisitedProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(java.sql.Types.VARCHAR)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name="user_id")
    @JdbcTypeCode(java.sql.Types.VARCHAR)
    private UUID userId;

    @Column(name = "created_date")
    private Date createdDate;

}

