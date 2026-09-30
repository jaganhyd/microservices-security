package com.example.accounts_1.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.example.accounts_1.entity.Accounts;

import jakarta.transaction.Transactional;

@Repository
public interface AccountsRepository extends JpaRepository<Accounts, Long> {

    Optional<Accounts> findByCustomerId(Long customerId);


    @Transactional
    @Modifying
    void deleteByCustomerId(Long customerId);
}
