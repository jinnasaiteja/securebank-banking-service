package com.securebank.banking.controller;

import com.securebank.banking.dto.TransactionResponse;
import com.securebank.banking.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(
            TransactionService transactionService) {

        this.transactionService = transactionService;
    }

    @GetMapping("/{accountId}/transactions")
    public ResponseEntity<List<TransactionResponse>>
    getAccountTransactions(
            @PathVariable Long accountId) {

        return ResponseEntity.ok(
                transactionService
                        .getTransactionsByAccountId(accountId)
        );
    }
}
