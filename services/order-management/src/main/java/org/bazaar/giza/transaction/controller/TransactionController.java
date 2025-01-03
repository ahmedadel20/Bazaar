package org.bazaar.giza.transaction.controller;

import lombok.RequiredArgsConstructor;
import org.bazaar.giza.transaction.dto.TransactionRequest;
import org.bazaar.giza.transaction.dto.TransactionResponse;
import org.bazaar.giza.transaction.service.TransactionServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transaction")
public class TransactionController {

    private final TransactionServiceImpl transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponse> create(@RequestBody TransactionRequest transactionRequest) {
        return new ResponseEntity<>(transactionService.create(transactionRequest), HttpStatus.CREATED);
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionResponse> getById(@PathVariable Long transactionId) {
        return new ResponseEntity<>(transactionService.getById(transactionId), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<TransactionResponse> update(@RequestBody TransactionRequest transactionRequest) {
        return new ResponseEntity<>(transactionService.update(transactionRequest), HttpStatus.OK);
    }

    @DeleteMapping("/{transactionId}")
    public ResponseEntity<String> delete(@PathVariable Long transactionId) {
        return new ResponseEntity<>(transactionService.delete(transactionId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getAll() {
        return new ResponseEntity<>(transactionService.getAll(), HttpStatus.OK);
    }
}