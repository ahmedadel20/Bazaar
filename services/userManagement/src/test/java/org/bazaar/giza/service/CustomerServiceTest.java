package org.bazaar.giza.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.bazaar.giza.constant.ErrorMessage;
import org.bazaar.giza.customer.dto.AddressRequest;
import org.bazaar.giza.customer.dto.CustomerMapper;
import org.bazaar.giza.customer.dto.CustomerRequest;
import org.bazaar.giza.customer.dto.CustomerResponse;
import org.bazaar.giza.customer.entity.Address;
import org.bazaar.giza.customer.entity.Customer;
import org.bazaar.giza.customer.exception.CustomerException;
import org.bazaar.giza.customer.repo.CustomerRepository;
import org.bazaar.giza.customer.service.CustomerService;
import org.bazaar.giza.customer.service.CustomerServiceImpl;
import org.bazaar.giza.user.repo.BazaarUserRepo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class CustomerServiceTest {
    @TestConfiguration
    static class ServiceTestConfig {
        @Bean
        CustomerService service(BazaarUserRepo bazaarUserRepo, CustomerRepository customerRepo,
                CustomerMapper customerMapper) {
            return new CustomerServiceImpl(bazaarUserRepo, customerRepo, customerMapper);
        }
    }

    @MockitoBean
    private BazaarUserRepo bazaarUserRepo;

    @MockitoBean
    private CustomerRepository customerRepo;

    @MockitoBean
    private CustomerMapper customerMapper;

    @Autowired
    private CustomerService customerService;

    private static Address address1;
    private static Address address2;

    private static Set<Address> addresses;

    private static Customer customer;
    private static Customer invalidCustomer;

    private static List<Customer> customers;

    private static CustomerRequest customerRequest;
    private static CustomerRequest invalidCustomerRequest;

    private static CustomerResponse customerResponse;

    private static AddressRequest addressRequest1;
    private static AddressRequest addressRequest2;

    @BeforeAll
    public static void setUp() {
        address1 = new Address("street", "city", "zipCode");
        address2 = new Address("share3", "madeena", "zipCode");

        addresses = new HashSet<>();
        addresses.add(address1);

        customer = new Customer();
        customer.setUserId(1L);
        customer.setFirstName("Bob");
        customer.setLastName("Ferguson");
        customer.setEmail("Bob@gmail.com");
        customer.setPhoneNumber("01002993382");
        customer.setPassword("password");
        customer.setAddresses(addresses);

        invalidCustomer = new Customer();
        invalidCustomer.setUserId(2L);
        invalidCustomer.setFirstName("Bob");
        invalidCustomer.setLastName("Ferguson");
        invalidCustomer.setEmail(customer.getEmail() + "1");
        invalidCustomer.setPhoneNumber(customer.getPhoneNumber() + "1");
        invalidCustomer.setPassword("password");
        invalidCustomer.setAddresses(addresses);

        customers = List.of(customer);

        customerRequest = new CustomerRequest(customer.getUserId(),
                customer.getFirstName(), customer.getLastName(),
                customer.getEmail(), customer.getPhoneNumber());

        invalidCustomerRequest = new CustomerRequest(invalidCustomer.getUserId(),
                invalidCustomer.getFirstName(), invalidCustomer.getLastName(),
                invalidCustomer.getEmail(), invalidCustomer.getPhoneNumber());

        customerResponse = new CustomerResponse(customer.getUserId(), customer.getFirstName(), customer.getLastName(),
                customer.getEmail(), customer.getPhoneNumber(), customer.getAddresses());

        addressRequest1 = new AddressRequest(address1.getStreet(), address1.getCity(), address1.getZipCode());
        addressRequest2 = new AddressRequest(address2.getStreet(), address2.getCity(), address2.getZipCode());
    }

    @BeforeEach
    public void setUpForEach() {
        invalidCustomer.setUserId(2L);
        // BazaarUserRepo
        when(bazaarUserRepo.findByEmail(customer.getEmail())).thenReturn(Optional.empty());
        when(bazaarUserRepo.findByPhoneNumber(customer.getPhoneNumber())).thenReturn(Optional.empty());

        when(bazaarUserRepo.findByEmail(invalidCustomer.getEmail())).thenReturn(Optional.empty());
        when(bazaarUserRepo.findByPhoneNumber(invalidCustomer.getPhoneNumber())).thenReturn(Optional.empty());

        // CustomerRepo
        when(customerRepo.findByEmail(customer.getEmail())).thenReturn(Optional.empty());
        when(customerRepo.findByPhoneNumber(customer.getPhoneNumber())).thenReturn(Optional.empty());

        when(customerRepo.findByEmail(invalidCustomer.getEmail())).thenReturn(Optional.empty());
        when(customerRepo.findByPhoneNumber(invalidCustomer.getPhoneNumber())).thenReturn(Optional.empty());

        when(customerRepo.findById(customer.getUserId())).thenReturn(Optional.of(customer));
        when(customerRepo.findById(invalidCustomer.getUserId())).thenReturn(Optional.empty());

        when(customerRepo.findAll()).thenReturn(customers);

        when(customerRepo.save(customer)).thenReturn(customer);

        // CustomerMapper
        when(customerMapper.toCustomer(customerRequest)).thenReturn(customer);
        when(customerMapper.toCustomerResponse(customer)).thenReturn(customerResponse);
        when(customerMapper.toAddress(addressRequest1)).thenReturn(address1);
        when(customerMapper.toAddress(addressRequest2)).thenReturn(address2);

        when(customerMapper.toCustomer(invalidCustomerRequest)).thenReturn(invalidCustomer);
    }

    @Test
    public void createCustomer_DuplicateInUser() {
        when(bazaarUserRepo.findByEmail(invalidCustomer.getEmail())).thenReturn(Optional.of(invalidCustomer));
        when(bazaarUserRepo.findByPhoneNumber(invalidCustomer.getPhoneNumber()))
                .thenReturn(Optional.of(invalidCustomer));

        CustomerException ex = assertThrows(CustomerException.class,
                () -> {
                    customerService.createCustomer(invalidCustomer);
                });
        assertTrue(ex.getMessage().contains(ErrorMessage.DUPLICATE_EMAIL));
        assertTrue(ex.getMessage().contains(ErrorMessage.DUPLICATE_PHONE_NUMBER));
    }

    @Test
    public void createCustomer_DuplicateInCustomer() {
        when(customerRepo.findByEmail(invalidCustomer.getEmail())).thenReturn(Optional.of(invalidCustomer));
        when(customerRepo.findByPhoneNumber(invalidCustomer.getPhoneNumber())).thenReturn(Optional.of(invalidCustomer));

        CustomerException ex = assertThrows(CustomerException.class,
                () -> {
                    customerService.createCustomer(invalidCustomer);
                });
        assertTrue(ex.getMessage().contains(ErrorMessage.DUPLICATE_EMAIL));
        assertTrue(ex.getMessage().contains(ErrorMessage.DUPLICATE_PHONE_NUMBER));
    }

    @Test
    public void createCustomer_Valid() {
        assertEquals(customer, customerService.createCustomer(customer));
    }

    @Test
    public void getSingleCustomer_NonExistant() {
        CustomerException ex = assertThrows(CustomerException.class,
                () -> {
                    customerService.getSingleCustomer(invalidCustomer.getUserId());
                });
        assertEquals(ErrorMessage.CUSTOMER_ID_NOT_FOUND, ex.getMessage());
    }

    @Test
    public void getSingleCustomer_Existant() {
        assertEquals(customerResponse, customerService.getSingleCustomer(customer.getUserId()));
    }

    @Test
    public void getAllCustomers() {
        List<CustomerResponse> customerResponses = List.of(customerResponse);
        assertEquals(customerResponses, customerService.getAllCustomers());
    }

    @Test
    public void addAddress_NonExistantCustomer() {
        CustomerException ex = assertThrows(CustomerException.class,
                () -> {
                    customerService.addAddress(invalidCustomer.getUserId(), addressRequest2);
                });
        assertEquals(ErrorMessage.CUSTOMER_ID_NOT_FOUND, ex.getMessage());
    }

    @Test
    public void addAddress_ExistantCustomer() {
        assertFalse(customer.getAddresses().contains(address2));
        customerService.addAddress(customer.getUserId(), addressRequest2);
        assertTrue(customer.getAddresses().contains(address2));
    }

    @Test
    public void removeAddress_NonExistantCustomer() {
        CustomerException ex = assertThrows(CustomerException.class,
                () -> {
                    customerService.removeAddress(invalidCustomer.getUserId(), addressRequest1);
                });
        assertEquals(ErrorMessage.CUSTOMER_ID_NOT_FOUND, ex.getMessage());
    }

    @Test
    public void removeAddress_ExistantCustomer() {
        assertTrue(customer.getAddresses().contains(address1));
        customerService.removeAddress(customer.getUserId(), addressRequest1);
        assertFalse(customer.getAddresses().contains(address1));
    }

    @Test
    public void updateCustomer_DuplicateInUser() {
        when(customerRepo.findById(invalidCustomer.getUserId())).thenReturn(Optional.of(invalidCustomer));
        when(bazaarUserRepo.findByEmail(invalidCustomer.getEmail())).thenReturn(Optional.of(invalidCustomer));
        when(bazaarUserRepo.findByPhoneNumber(invalidCustomer.getPhoneNumber()))
                .thenReturn(Optional.of(invalidCustomer));

        CustomerException ex = assertThrows(CustomerException.class,
                () -> {
                    customerService.updateCustomer(invalidCustomerRequest);
                });
        assertTrue(ex.getMessage().contains(ErrorMessage.DUPLICATE_EMAIL));
        assertTrue(ex.getMessage().contains(ErrorMessage.DUPLICATE_PHONE_NUMBER));
    }

    @Test
    public void updateCustomer_DuplicateInCustomer() {
        when(customerRepo.findById(invalidCustomer.getUserId())).thenReturn(Optional.of(invalidCustomer));
        when(customerRepo.findByEmail(invalidCustomer.getEmail())).thenReturn(Optional.of(invalidCustomer));
        when(customerRepo.findByPhoneNumber(invalidCustomer.getPhoneNumber())).thenReturn(Optional.of(invalidCustomer));

        CustomerException ex = assertThrows(CustomerException.class,
                () -> {
                    customerService.updateCustomer(invalidCustomerRequest);
                });
        assertTrue(ex.getMessage().contains(ErrorMessage.DUPLICATE_EMAIL));
        assertTrue(ex.getMessage().contains(ErrorMessage.DUPLICATE_PHONE_NUMBER));
    }

    @Test
    public void updateCustomer_NonExistant() {
        CustomerException ex = assertThrows(CustomerException.class,
                () -> {
                    customerService.updateCustomer(invalidCustomerRequest);
                });
        assertEquals(ErrorMessage.CUSTOMER_ID_NOT_FOUND, ex.getMessage());
    }

    @Test
    public void updateCustomer_Valid() {
        assertEquals(customerResponse, customerService.updateCustomer(customerRequest));
    }

    @Test
    public void deleteCustomer_NonExistant() {
        CustomerException ex = assertThrows(CustomerException.class,
                () -> {
                    customerService.deleteCustomer(invalidCustomer.getUserId());
                });
        assertEquals(ErrorMessage.CUSTOMER_ID_NOT_FOUND, ex.getMessage());

        verify(customerRepo, times(0)).delete(any());
    }

    @Test
    public void deleteCustomer_Existant() {
        assertEquals("Customer deleted", customerService.deleteCustomer(customer.getUserId()));

        verify(customerRepo, times(1)).delete(customer);
    }
}
