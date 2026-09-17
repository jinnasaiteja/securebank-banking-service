package com.securebank.banking.service;

import com.securebank.banking.exception.CustomerNotFoundException;
import com.securebank.banking.exception.DuplicateCustomerException;
import com.securebank.banking.dto.CustomerRequest;
import com.securebank.banking.dto.CustomerResponse;
import com.securebank.banking.entity.Customer;
import com.securebank.banking.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public CustomerResponse createCustomer(CustomerRequest request) {

        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateCustomerException("Email already exists");
        }

        Customer customer = new Customer();

        customer.setCustomerNumber(generateCustomerNumber());
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setEmail(request.getEmail());
        customer.setPhoneNumber(request.getPhoneNumber());
        customer.setStatus("ACTIVE");

        Customer savedCustomer =
                customerRepository.save(customer);

        return toResponse(savedCustomer);
    }

    public CustomerResponse getCustomerById(Long id) {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() ->
                        new CustomerNotFoundException("Customer not found with id: " + id));

        return toResponse(customer);
    }

    public List<CustomerResponse> getAllCustomers() {

        return customerRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private String generateCustomerNumber() {

        return "CUS-" +
                UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase();
    }

    private CustomerResponse toResponse(Customer customer) {

        return new CustomerResponse(
                customer.getId(),
                customer.getCustomerNumber(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getPhoneNumber(),
                customer.getStatus(),
                customer.getCreatedAt(),
                customer.getUpdatedAt()
        );
    }
}
