package com.securebank.banking.repository;

import com.securebank.banking.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByEmail(String email);

    Optional<Customer> findByCustomerNumber(String customerNumber);

    boolean existsByEmail(String email);

    boolean existsByCustomerNumber(String customerNumber);
}
