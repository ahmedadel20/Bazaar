package org.bazaar.giza.customer;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository repository;
    private final CustomerMapper mapper;

    public String create(CustomerRequest request) {
        repository.save(mapper.toCustomer(request));
        return "Customer created";
    }

    public String update(CustomerRequest request) {
        var customer = repository.findById(request.id()).orElseThrow(); // need to add customException
        mergerCustomer(request, customer);
        repository.save(mapper.toCustomer(request));
        return "Customer updated";
    }

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

    public CustomerResponse findById(Long id) {
        if (id == null) {
            return null;
        }
        return mapper.toCustomerResponse(repository.findById(id).orElseThrow()); // replace with custom exception
    }

    public String delete(Long id) {
        repository.deleteById(id);
        return "Customer deleted";
    }

    public List<CustomerResponse> findAll() {
        return repository.findAll().stream().map(mapper::toCustomerResponse).toList();
    }
}
