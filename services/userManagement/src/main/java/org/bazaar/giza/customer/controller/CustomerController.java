package org.bazaar.giza.customer.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.Set;

import org.bazaar.giza.customer.dto.AddressRequest;
import org.bazaar.giza.customer.dto.CustomerRequest;
import org.bazaar.giza.customer.dto.CustomerResponse;
import org.bazaar.giza.customer.entity.Address;
import org.bazaar.giza.customer.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customer")
public class CustomerController {

    private final CustomerService service;

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerResponse> getSingleCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(service.getSingleCustomer(customerId));
    }

    @GetMapping()
    public ResponseEntity<Iterable<CustomerResponse>> findAllCustomers() {
        return ResponseEntity.ok(service.getAllCustomers());
    }

    @PutMapping()
    public ResponseEntity<CustomerResponse> updateSingleCustomer(@RequestBody @Valid CustomerRequest customerRequest) {
        return ResponseEntity.ok(service.updateCustomer(customerRequest));
    }

    @PutMapping("/{customerId}/addAdress")
    public ResponseEntity<Set<Address>> addAddress(@PathVariable Long customerId,
            @RequestBody @Valid AddressRequest addressRequest) {
        return ResponseEntity.ok(service.addAddress(customerId, addressRequest));
    }

    @PutMapping("/{customerId}/removeAddress")
    public ResponseEntity<Set<Address>> removeAddress(@PathVariable Long customerId,
            @RequestBody @Valid AddressRequest addressRequest) {
        return ResponseEntity.ok(service.removeAddress(customerId, addressRequest));
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<String> deleteSingleCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(service.deleteCustomer(customerId));
    }
}
