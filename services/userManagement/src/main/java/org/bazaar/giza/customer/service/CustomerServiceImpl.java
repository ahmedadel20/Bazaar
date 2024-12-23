package org.bazaar.giza.customer.service;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;

import org.bazaar.giza.customer.dto.CustomerMapper;
import org.bazaar.giza.customer.dto.CustomerRequest;
import org.bazaar.giza.customer.dto.CustomerResponse;
import org.bazaar.giza.customer.entity.Customer;
import org.bazaar.giza.customer.repo.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository repository;
    private final CustomerMapper mapper;

    public CustomerResponse createCustomer(CustomerRequest request) {
        Customer customer = repository.save(mapper.toCustomer(request));
        return mapper.toCustomerResponse(customer);
    }

    public CustomerResponse getSingleCustomer(Long id) {
        if (id == null) {
            return null;
        }
        return mapper.toCustomerResponse(repository.findById(id).orElseThrow()); // replace with custom exception
    }

    public List<CustomerResponse> getAllCustomers() {
        return repository.findAll().stream().map(mapper::toCustomerResponse).toList();
    }

    public CustomerResponse updateCustomer(CustomerRequest request) {
        var customer = repository.findById(request.id()).orElseThrow(); // need to add customException
        mergerCustomer(request, customer);
        customer = repository.save(mapper.toCustomer(request));
        return mapper.toCustomerResponse(customer);
    }

    public String deleteCustomer(Long id) {
        repository.deleteById(id);
        return "Customer deleted";
    }

    // Helper Functions
    private void mergerCustomer(CustomerRequest request, Customer customer) {
        if (StringUtils.isNotEmpty(request.firstName())) {
            customer.setFirstName(request.firstName());
        }
        if (StringUtils.isNotEmpty(request.lastName())) {
            customer.setLastName(request.lastName());
        }
        if (StringUtils.isNotEmpty(request.email())) {
            customer.setEmail(request.email());
        }
        // need to link and add validation
    }
}
