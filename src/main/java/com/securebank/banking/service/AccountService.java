package com.securebank.banking.service;

import com.securebank.banking.dto.AccountRequest;
import com.securebank.banking.dto.AccountResponse;
import com.securebank.banking.dto.AmountRequest;
import com.securebank.banking.dto.TransferRequest;
import com.securebank.banking.entity.Account;
import com.securebank.banking.entity.Customer;
import com.securebank.banking.entity.Transaction;
import com.securebank.banking.enums.AccountStatus;
import com.securebank.banking.enums.TransactionStatus;
import com.securebank.banking.enums.TransactionType;
import com.securebank.banking.exception.AccountNotFoundException;
import com.securebank.banking.exception.CustomerNotFoundException;
import com.securebank.banking.exception.InsufficientFundsException;
import com.securebank.banking.exception.InvalidTransferException;
import com.securebank.banking.repository.AccountRepository;
import com.securebank.banking.repository.CustomerRepository;
import com.securebank.banking.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;

    public AccountService(
            AccountRepository accountRepository,
            CustomerRepository customerRepository,
            TransactionRepository transactionRepository) {

        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.transactionRepository = transactionRepository;
    }

    public AccountResponse createAccount(AccountRequest request) {

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() ->
                        new CustomerNotFoundException(
                                "Customer not found with id: "
                                        + request.getCustomerId()
                        ));

        Account account = new Account();

        account.setCustomer(customer);
        account.setAccountNumber(generateAccountNumber());
        account.setAccountType(request.getAccountType());
        account.setBalance(BigDecimal.ZERO);
        account.setCurrency("USD");
        account.setStatus(AccountStatus.ACTIVE);

        Account savedAccount = accountRepository.save(account);

        return toResponse(savedAccount);
    }

    public AccountResponse getAccountById(Long id) {

        Account account = accountRepository.findById(id)
                .orElseThrow(() ->
                        new AccountNotFoundException(
                                "Account not found with id: " + id
                        ));

        return toResponse(account);
    }

    public List<AccountResponse> getAccountsByCustomerId(Long customerId) {

        if (!customerRepository.existsById(customerId)) {
            throw new CustomerNotFoundException(
                    "Customer not found with id: " + customerId
            );
        }

        return accountRepository.findByCustomerId(customerId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public AccountResponse deposit(
            Long accountId,
            AmountRequest request) {

        Account account = getAccountEntity(accountId);

        BigDecimal newBalance =
                account.getBalance().add(request.getAmount());

        account.setBalance(newBalance);

        Account savedAccount = accountRepository.save(account);

        saveTransaction(
                savedAccount,
                null,
                null,
                TransactionType.DEPOSIT,
                request.getAmount(),
                newBalance,
                "Cash deposit"
        );

        return toResponse(savedAccount);
    }

    @Transactional
    public AccountResponse withdraw(
            Long accountId,
            AmountRequest request) {

        Account account = getAccountEntity(accountId);

        BigDecimal withdrawalAmount = request.getAmount();

        if (account.getBalance().compareTo(withdrawalAmount) < 0) {
            throw new InsufficientFundsException(
                    "Insufficient funds. Available balance: "
                            + account.getBalance()
            );
        }

        BigDecimal newBalance =
                account.getBalance().subtract(withdrawalAmount);

        account.setBalance(newBalance);

        Account savedAccount = accountRepository.save(account);

        saveTransaction(
                savedAccount,
                null,
                null,
                TransactionType.WITHDRAWAL,
                withdrawalAmount,
                newBalance,
                "Cash withdrawal"
        );

        return toResponse(savedAccount);
    }

@Transactional
public AccountResponse transfer(
        Long sourceAccountId,
        TransferRequest request) {

    Long destinationAccountId =
            request.getDestinationAccountId();

    if (sourceAccountId.equals(destinationAccountId)) {
        throw new InvalidTransferException(
                "Source and destination accounts cannot be the same"
        );
    }

    Long firstAccountId =
            Math.min(sourceAccountId, destinationAccountId);

    Long secondAccountId =
            Math.max(sourceAccountId, destinationAccountId);

    Account firstAccount =
            getAccountEntityForUpdate(firstAccountId);

    Account secondAccount =
            getAccountEntityForUpdate(secondAccountId);

    Account sourceAccount;
    Account destinationAccount;

    if (firstAccount.getId().equals(sourceAccountId)) {

        sourceAccount = firstAccount;
        destinationAccount = secondAccount;

    } else {

        sourceAccount = secondAccount;
        destinationAccount = firstAccount;
    }

    if (sourceAccount.getStatus() != AccountStatus.ACTIVE) {
        throw new InvalidTransferException(
                "Source account is not active"
        );
    }

    if (destinationAccount.getStatus() != AccountStatus.ACTIVE) {
        throw new InvalidTransferException(
                "Destination account is not active"
        );
    }

    if (!sourceAccount.getCurrency()
            .equals(destinationAccount.getCurrency())) {

        throw new InvalidTransferException(
                "Accounts must use the same currency"
        );
    }

    BigDecimal transferAmount =
            request.getAmount();

    if (sourceAccount.getBalance()
            .compareTo(transferAmount) < 0) {

        throw new InsufficientFundsException(
                "Insufficient funds. Available balance: "
                        + sourceAccount.getBalance()
        );
    }

    BigDecimal sourceNewBalance =
            sourceAccount.getBalance()
                    .subtract(transferAmount);

    BigDecimal destinationNewBalance =
            destinationAccount.getBalance()
                    .add(transferAmount);

    sourceAccount.setBalance(sourceNewBalance);
    destinationAccount.setBalance(destinationNewBalance);

    Account savedSource =
            accountRepository.save(sourceAccount);

    Account savedDestination =
            accountRepository.save(destinationAccount);

    String transferReference =
            generateTransferReference();

    saveTransaction(
            savedSource,
            savedDestination,
            transferReference,
            TransactionType.TRANSFER_OUT,
            transferAmount,
            sourceNewBalance,
            "Transfer to account "
                    + savedDestination.getAccountNumber()
    );

    saveTransaction(
            savedDestination,
            savedSource,
            transferReference,
            TransactionType.TRANSFER_IN,
            transferAmount,
            destinationNewBalance,
            "Transfer from account "
                    + savedSource.getAccountNumber()
    );

    return toResponse(savedSource);
}
    private Account getAccountEntity(Long accountId) {

        return accountRepository.findById(accountId)
                .orElseThrow(() ->
                        new AccountNotFoundException(
                                "Account not found with id: " + accountId
                        ));
    }
    private Account getAccountEntityForUpdate(Long accountId) {

        return accountRepository.findByIdForUpdate(accountId)
            .orElseThrow(() ->
                    new AccountNotFoundException(
                            "Account not found with id: " + accountId
                    ));
    }

    private void saveTransaction(
            Account account,
            Account relatedAccount,
            String transferReference,
            TransactionType transactionType,
            BigDecimal amount,
            BigDecimal balanceAfter,
            String description) {

        Transaction transaction = new Transaction();

        transaction.setTransactionReference(
                generateTransactionReference()
        );

        transaction.setTransferReference(transferReference);
        transaction.setAccount(account);
        transaction.setRelatedAccount(relatedAccount);
        transaction.setTransactionType(transactionType);
        transaction.setAmount(amount);
        transaction.setBalanceAfter(balanceAfter);
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setDescription(description);

        transactionRepository.save(transaction);
    }

    private String generateAccountNumber() {

        return "ACC-" +
                UUID.randomUUID()
                        .toString()
                        .substring(0, 10)
                        .toUpperCase();
    }

    private String generateTransactionReference() {

        return "TXN-" +
                UUID.randomUUID()
                        .toString()
                        .substring(0, 12)
                        .toUpperCase();
    }

    private String generateTransferReference() {

        return "TRF-" +
                UUID.randomUUID()
                        .toString()
                        .substring(0, 12)
                        .toUpperCase();
    }

    private AccountResponse toResponse(Account account) {

        return new AccountResponse(
                account.getId(),
                account.getCustomer().getId(),
                account.getAccountNumber(),
                account.getAccountType(),
                account.getBalance(),
                account.getCurrency(),
                account.getStatus(),
                account.getCreatedAt(),
                account.getUpdatedAt()
        );
    }
}



