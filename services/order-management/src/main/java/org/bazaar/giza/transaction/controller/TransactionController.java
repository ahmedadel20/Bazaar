package org.bazaar.giza.transaction.controller;

import lombok.RequiredArgsConstructor;
import org.bazaar.giza.transaction.dto.TransactionRequest;
import org.bazaar.giza.transaction.dto.TransactionResponse;
import org.bazaar.giza.transaction.mapper.TransactionMapper;
import org.bazaar.giza.transaction.service.TransactionServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transaction")
public class TransactionController {

    private final TransactionServiceImpl transactionService;
    private final TransactionMapper transactionMapper;

    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponse create(@RequestBody TransactionRequest transactionRequest) {
        return transactionMapper.toTransactionResponse(
                transactionService.create(transactionMapper.toTransaction(transactionRequest)));
    }

    @GetMapping("/{transactionId}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public TransactionResponse getById(@PathVariable Long transactionId) {
        return transactionMapper.toTransactionResponse(
                transactionService.getById(transactionId));
    }

    @PutMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public TransactionResponse update(@RequestBody TransactionRequest transactionRequest) {
        return transactionMapper.toTransactionResponse(
                transactionService.update(transactionMapper.toTransaction(transactionRequest)));
    }

    @DeleteMapping("/{transactionId}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public String delete(@PathVariable Long transactionId) {
        return transactionService.delete(transactionId);
    }

    @GetMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<TransactionResponse> getAll() {
        return transactionService.getAll()
                .stream()
                .map(transactionMapper::toTransactionResponse)
                .toList();
    }
}