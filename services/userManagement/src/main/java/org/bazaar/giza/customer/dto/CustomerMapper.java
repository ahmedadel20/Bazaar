package org.bazaar.giza.customer.dto;

import java.util.HashSet;

import org.bazaar.giza.customer.entity.Address;
import org.bazaar.giza.customer.entity.Customer;
import org.springframework.stereotype.Service;

@Service
public class CustomerMapper {
    public Customer toCustomer(CustomerRequest customerRequest) {
        return Customer.builder()
                .userId(customerRequest.id())
                .firstName(customerRequest.firstName())
                .lastName(customerRequest.lastName())
                .email(customerRequest.email())
                .phoneNumber(customerRequest.phoneNumber())
                .addresses(new HashSet<>())
                .build();
    }

    public Address toAddress(AddressRequest addressRequest) {
        return Address.builder()
                .street(addressRequest.street())
                .city(addressRequest.city())
                .zipCode(addressRequest.zipCode())
                .build();
    }

    public CustomerResponse toCustomerResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getUserId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getEmail())
                .phoneNumber(customer.getPhoneNumber())
                .addresses(customer.getAddresses())
                .build();
    }
}
