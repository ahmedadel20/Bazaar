package org.bazaar.giza.customer.service;

import java.util.List;

import org.bazaar.giza.customer.dto.CustomerRequest;
import org.bazaar.giza.customer.dto.CustomerResponse;

public interface CustomerService {
    CustomerResponse createCustomer(CustomerRequest request);

    CustomerResponse getSingleCustomer(Long customerId);

    List<CustomerResponse> getAllCustomers();

    CustomerResponse updateCustomer(CustomerRequest request);

    String deleteCustomer(Long customerId);
}
