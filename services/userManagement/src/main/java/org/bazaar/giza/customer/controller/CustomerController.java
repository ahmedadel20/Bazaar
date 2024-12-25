package org.bazaar.giza.customer.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.Set;

import org.bazaar.giza.constant.Roles;
import org.bazaar.giza.customer.dto.AddressRequest;
import org.bazaar.giza.customer.dto.CustomerRequest;
import org.bazaar.giza.customer.dto.CustomerResponse;
import org.bazaar.giza.customer.entity.Address;
import org.bazaar.giza.customer.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customer")
public class CustomerController {

    private final CustomerService service;

    @GetMapping("/{customerId}")
    @PreAuthorize("hasAuthority('" + Roles.ADMIN + "') or " +
            "(hasAuthority('" + Roles.CUSTOMER + "') and #customerId == authentication.principal.id)")
    public ResponseEntity<CustomerResponse> getSingleCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(service.getSingleCustomer(customerId));
    }

    @GetMapping()
    @PreAuthorize("hasAuthority('" + Roles.ADMIN + "')")
    public ResponseEntity<Iterable<CustomerResponse>> findAllCustomers() {
        return ResponseEntity.ok(service.getAllCustomers());
    }

    @PutMapping()
    @PreAuthorize("hasAuthority('" + Roles.ADMIN + "') or " +
            "(hasAuthority('" + Roles.CUSTOMER + "') and #customerRequest.id == authentication.principal.id)")
    public ResponseEntity<CustomerResponse> updateSingleCustomer(@RequestBody @Valid CustomerRequest customerRequest) {
        return ResponseEntity.ok(service.updateCustomer(customerRequest));
    }

    @PutMapping("/{customerId}/addAddress")
    @PreAuthorize("hasAuthority('" + Roles.ADMIN + "') or " +
            "(hasAuthority('" + Roles.CUSTOMER + "') and #customerId == authentication.principal.id)")
    public ResponseEntity<Set<Address>> addAddress(@PathVariable Long customerId,
            @RequestBody @Valid AddressRequest addressRequest) {
        return ResponseEntity.ok(service.addAddress(customerId, addressRequest));
    }

    @PutMapping("/{customerId}/removeAddress")
    @PreAuthorize("hasAuthority('" + Roles.ADMIN + "') or " +
            "(hasAuthority('" + Roles.CUSTOMER + "') and #customerId == authentication.principal.id)")
    public ResponseEntity<Set<Address>> removeAddress(@PathVariable Long customerId,
            @RequestBody @Valid AddressRequest addressRequest) {
        return ResponseEntity.ok(service.removeAddress(customerId, addressRequest));
    }

    @DeleteMapping("/{customerId}")
    @PreAuthorize("hasAuthority('" + Roles.ADMIN + "') or " +
            "(hasAuthority('" + Roles.CUSTOMER + "') and #customerId == authentication.principal.id)")
    public ResponseEntity<String> deleteSingleCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(service.deleteCustomer(customerId));
    }
}
