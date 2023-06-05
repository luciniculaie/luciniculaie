package com.sportsvault.repository;

import com.sportsvault.model.WonProducts;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface WonProductsRepository extends JpaRepository<WonProducts, UUID> {

}

