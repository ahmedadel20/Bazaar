package org.bazaar.giza.customer.service;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;

import org.bazaar.giza.constant.ErrorMessage;
import org.bazaar.giza.customer.dto.AddressRequest;
import org.bazaar.giza.customer.dto.CustomerMapper;
import org.bazaar.giza.customer.dto.CustomerRequest;
import org.bazaar.giza.customer.dto.CustomerResponse;
import org.bazaar.giza.customer.entity.Address;
import org.bazaar.giza.customer.entity.Customer;
import org.bazaar.giza.customer.exception.CustomerException;
import org.bazaar.giza.customer.repo.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository repository;
    private final CustomerMapper mapper;

    public Customer createCustomer(Customer customer) {
        // FIXME: Add Validation
        return repository.save(customer);
    }

    public CustomerResponse getSingleCustomer(Long customerId) {
        if (customerId == null) {
            throw new CustomerException(ErrorMessage.ID_CANNOT_BE_NULL);
        }

        return mapper.toCustomerResponse(searchId(customerId));
    }

    public List<CustomerResponse> getAllCustomers() {
        return repository.findAll().stream().map(mapper::toCustomerResponse).toList();
    }

    @Override
    public Set<Address> addAddress(Long customerId, AddressRequest addressRequest) {
        Customer customer = searchId(customerId);
        customer.addAddress(mapper.toAddress(addressRequest));
        customer = repository.save(customer);
        return customer.getAddresses();
    }

    @Override
    public Set<Address> removeAddress(Long customerId, AddressRequest addressRequest) {
        Customer customer = searchId(customerId);
        customer.removeAddress(mapper.toAddress(addressRequest));
        customer = repository.save(customer);
        return customer.getAddresses();
    }

    public CustomerResponse updateCustomer(CustomerRequest request) {
        var customer = searchId(request.id());
        mergerCustomer(request, customer);
        customer = repository.save(mapper.toCustomer(request));
        return mapper.toCustomerResponse(customer);
    }

    public String deleteCustomer(Long customerId) {
        if (customerId == null) {
            throw new CustomerException(ErrorMessage.ID_CANNOT_BE_NULL);
        }

        repository.delete(searchId(customerId));
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
        // FIXME: need to link and add validation
    }

    private Customer searchId(Long id) {
        Optional<Customer> customerOptional = repository.findById(id);
        if (customerOptional.isEmpty()) {
            throw new CustomerException(ErrorMessage.CUSTOMER_ID_NOT_FOUND);
        }

        return customerOptional.get();
    }
}
