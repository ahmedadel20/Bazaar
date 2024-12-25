package org.bazaar.giza.customer.service;

import java.util.List;
import java.util.Set;

import org.bazaar.giza.customer.dto.AddressRequest;
import org.bazaar.giza.customer.dto.CustomerRequest;
import org.bazaar.giza.customer.dto.CustomerResponse;
import org.bazaar.giza.customer.entity.Address;
import org.bazaar.giza.customer.entity.Customer;

public interface CustomerService {
    Customer createCustomer(Customer customer);

    CustomerResponse getSingleCustomer(Long customerId);

    List<CustomerResponse> getAllCustomers();

    Set<Address> addAddress(Long customerId, AddressRequest addressRequest);

    Set<Address> removeAddress(Long customerId, AddressRequest addressRequest);

    CustomerResponse updateCustomer(CustomerRequest request);

    String deleteCustomer(Long customerId);
}
