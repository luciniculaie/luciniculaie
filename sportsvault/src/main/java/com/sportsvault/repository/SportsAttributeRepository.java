package com.sportsvault.repository;

import com.sportsvault.model.SportsAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface SportsAttributeRepository extends JpaRepository<SportsAttribute, UUID> {

    @Query("Select sp from SportsAttribute sp")
    List<SportsAttribute> getAll();
}
