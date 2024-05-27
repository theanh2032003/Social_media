package com.example.demo.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.ConfirmToken;

import jakarta.transaction.Transactional;

@Repository
public interface ConfirmTokenRepository extends JpaRepository<ConfirmToken, Long> {

    Optional<ConfirmToken> findByToken(String token);

    @Modifying
    @Transactional
    @Query("update ConfirmToken a set a.confirmAt = ?2 where a.token =?1")
    void updateConfirmToken(String token, LocalDateTime confirmAt);

}
