package com.securebank.banking.dto;

import java.time.LocalDateTime;

public class CustomerResponse {

    private Long id;
    private String customerNumber;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CustomerResponse(
            Long id,
            String customerNumber,
            String firstName,
            String lastName,
            String email,
            String phoneNumber,
            String status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {

        this.id = id;
        this.customerNumber = customerNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
