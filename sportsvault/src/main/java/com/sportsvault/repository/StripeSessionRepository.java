package com.sportsvault.repository;

import com.sportsvault.model.StripeSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface StripeSessionRepository extends JpaRepository<StripeSession, UUID> {

    @Query("select ss from StripeSession ss where ss.session_id = :sessionId")
    StripeSession findBySessionId(@Param("sessionId") String sessionId);
}
