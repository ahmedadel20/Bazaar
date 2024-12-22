package org.bazaar.giza.customer;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CustomerController {

    private final CustomerService service;
    @PostMapping("/customer")
    public ResponseEntity<String> create(@RequestBody @Valid CustomerRequest customerRequest) {
        return ResponseEntity.ok(service.create(customerRequest));
    }

    @PutMapping("/customer")
    public ResponseEntity<String> update(@RequestBody @Valid CustomerRequest customerRequest) {
        return ResponseEntity.ok(service.update(customerRequest));
    }

    @GetMapping("/customer")
    public ResponseEntity<CustomerResponse> findById(@RequestParam Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/customers")
    public ResponseEntity<Iterable<CustomerResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @DeleteMapping("customer")
    public ResponseEntity<String> delete(@RequestParam Long id) {
        return ResponseEntity.ok(service.delete(id));
    }
}
