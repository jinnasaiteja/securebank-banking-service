package com.securebank.banking.controller;

import com.securebank.banking.dto.AccountRequest;
import com.securebank.banking.dto.AccountResponse;
import com.securebank.banking.dto.AmountRequest;
import com.securebank.banking.dto.TransferRequest;
import com.securebank.banking.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(
            @Valid @RequestBody AccountRequest request) {

        AccountResponse account =
                accountService.createAccount(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(account);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccountById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                accountService.getAccountById(id)
        );
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<AccountResponse>> getAccountsByCustomer(
            @PathVariable Long customerId) {

        return ResponseEntity.ok(
                accountService.getAccountsByCustomerId(customerId)
        );
    }

    @PostMapping("/{accountId}/deposit")
    public ResponseEntity<AccountResponse> deposit(
            @PathVariable Long accountId,
            @Valid @RequestBody AmountRequest request) {

        return ResponseEntity.ok(
                accountService.deposit(accountId, request)
        );
    }

    @PostMapping("/{accountId}/withdraw")
    public ResponseEntity<AccountResponse> withdraw(
            @PathVariable Long accountId,
            @Valid @RequestBody AmountRequest request) {

        return ResponseEntity.ok(
                accountService.withdraw(accountId, request)
        );
    }

    @PostMapping("/{sourceAccountId}/transfer")
    public ResponseEntity<AccountResponse> transfer(
            @PathVariable Long sourceAccountId,
            @Valid @RequestBody TransferRequest request) {

        return ResponseEntity.ok(
                accountService.transfer(
                        sourceAccountId,
                        request
                )
        );
    }
}
