package com.securebank.banking.service;

import com.securebank.banking.dto.TransactionResponse;
import com.securebank.banking.entity.Transaction;
import com.securebank.banking.exception.AccountNotFoundException;
import com.securebank.banking.repository.AccountRepository;
import com.securebank.banking.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionService(
            TransactionRepository transactionRepository,
            AccountRepository accountRepository) {

        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    public List<TransactionResponse> getTransactionsByAccountId(
            Long accountId) {

        if (!accountRepository.existsById(accountId)) {
            throw new AccountNotFoundException(
                    "Account not found with id: " + accountId
            );
        }

        return transactionRepository
                .findByAccountIdOrderByCreatedAtDesc(accountId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private TransactionResponse toResponse(
            Transaction transaction) {

        Long relatedAccountId = null;

        if (transaction.getRelatedAccount() != null) {
            relatedAccountId =
                    transaction.getRelatedAccount().getId();
        }

        return new TransactionResponse(
                transaction.getId(),
                transaction.getTransactionReference(),
                transaction.getAccount().getId(),
                relatedAccountId,
                transaction.getTransactionType(),
                transaction.getAmount(),
                transaction.getBalanceAfter(),
                transaction.getStatus(),
                transaction.getDescription(),
                transaction.getCreatedAt()
        );
    }
}
