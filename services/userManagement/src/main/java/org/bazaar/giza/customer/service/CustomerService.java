package org.bazaar.giza.customer.service;

import java.util.List;

import org.bazaar.giza.customer.dto.CustomerRequest;
import org.bazaar.giza.customer.dto.CustomerResponse;
import org.bazaar.giza.customer.entity.Customer;

// TODO: Add methods to add and remove addresses
public interface CustomerService {
    Customer createCustomer(Customer customer);

    CustomerResponse getSingleCustomer(Long customerId);

    List<CustomerResponse> getAllCustomers();

    CustomerResponse updateCustomer(CustomerRequest request);

    String deleteCustomer(Long customerId);
}
