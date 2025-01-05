package org.bazaar.giza.customer.service;

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
import org.bazaar.giza.user.repo.BazaarUserRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final BazaarUserRepo bazaarUserRepo;
    private final CustomerRepository customerRepo;
    private final CustomerMapper mapper;

    public Customer createCustomer(Customer customer) {
        customer.setUserId(null);
        validateCustomer(customer);

        return customerRepo.save(customer);
    }

    public CustomerResponse getSingleCustomer(Long customerId) {
        return mapper.toCustomerResponse(searchId(customerId));
    }

    public List<CustomerResponse> getAllCustomers() {
        return customerRepo.findAll().stream().map(mapper::toCustomerResponse).toList();
    }

    @Override
    public Set<Address> addAddress(Long customerId, AddressRequest addressRequest) {
        Customer customer = searchId(customerId);
        customer.addAddress(mapper.toAddress(addressRequest));
        customer = customerRepo.save(customer);
        return customer.getAddresses();
    }

    @Override
    public Set<Address> removeAddress(Long customerId, AddressRequest addressRequest) {
        Customer customer = searchId(customerId);
        customer.removeAddress(mapper.toAddress(addressRequest));
        customer = customerRepo.save(customer);
        return customer.getAddresses();
    }

    public CustomerResponse updateCustomer(CustomerRequest request) {
        var customer = searchId(request.id());
        validateCustomer(customer);

        customer = customerRepo.save(mapper.toCustomer(request));
        return mapper.toCustomerResponse(customer);
    }

    public String deleteCustomer(Long customerId) {
        customerRepo.delete(searchId(customerId));
        return "Customer deleted";
    }

    // Helper Functions
    private Customer searchId(Long id) {
        if (id == null) {
            throw new CustomerException(ErrorMessage.ID_CANNOT_BE_NULL);
        }
        Optional<Customer> customerOptional = customerRepo.findById(id);
        if (customerOptional.isEmpty()) {
            throw new CustomerException(ErrorMessage.CUSTOMER_ID_NOT_FOUND);
        }

        return customerOptional.get();
    }

    private void validateCustomer(Customer customer) {
        boolean emailExists = bazaarUserRepo.findByEmail(customer.getEmail()).isPresent()
                || customerRepo.findByEmail(customer.getEmail()).isPresent();
        boolean phoneExists = bazaarUserRepo.findByPhoneNumber(customer.getPhoneNumber()).isPresent()
                || customerRepo.findByPhoneNumber(customer.getPhoneNumber()).isPresent();

        // Duplicate email or phone number are not allowed
        if (emailExists && phoneExists) {
            throw new CustomerException(
                    String.format("%s\n%s", ErrorMessage.DUPLICATE_EMAIL, ErrorMessage.DUPLICATE_PHONE_NUMBER));
        } else if (emailExists) {
            throw new CustomerException(ErrorMessage.DUPLICATE_EMAIL);
        } else if (phoneExists) {
            throw new CustomerException(ErrorMessage.DUPLICATE_PHONE_NUMBER);
        }
    }
}
