package org.bazaar.giza.auth.service;

import java.util.HashSet;
import java.util.Set;

import org.bazaar.giza.auth.dto.UserRegisterationDTO;
import org.bazaar.giza.auth.entity.UserRoles;
import org.bazaar.giza.auth.repo.UserRolesRepo;
import org.bazaar.giza.constant.Roles;
import org.bazaar.giza.customer.entity.Customer;
import org.bazaar.giza.customer.service.CustomerService;
import org.bazaar.giza.user.entity.BazaarUser;
import org.bazaar.giza.user.service.BazaarUserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class UserRolesServiceImpl implements UserRolesService {
    private final UserRolesRepo repository;
    private final BazaarUserService bazaarUserService;
    private final CustomerService customerService;
    private final PasswordEncoder encoder;

    @Override
    public UserRoles registerCustomer(UserRegisterationDTO userRegisterationDTO) {
        Customer customer = customerService.createCustomer(Customer.builder()
                .firstName(userRegisterationDTO.firstName())
                .lastName(userRegisterationDTO.lastName())
                .email(userRegisterationDTO.email())
                .phoneNumber(userRegisterationDTO.phoneNumber())
                .password(encoder.encode(userRegisterationDTO.password()))
                .addresses(new HashSet<>())
                .build());

        Set<String> roles = new HashSet<>();
        roles.add(Roles.CUSTOMER);

        UserRoles customerRoles = new UserRoles(customer, roles);

        return repository.save(customerRoles);
    }

    @Override
    public UserRoles registerManager(UserRegisterationDTO userRegisterationDTO) {
        BazaarUser user = bazaarUserService.createUser(new BazaarUser(null, userRegisterationDTO.firstName(),
                userRegisterationDTO.lastName(), userRegisterationDTO.email(), userRegisterationDTO.phoneNumber(),
                encoder.encode(userRegisterationDTO.password())));

        Set<String> roles = new HashSet<>();
        roles.add(Roles.MANAGER);

        UserRoles managerRoles = new UserRoles(user, roles);

        return repository.save(managerRoles);
    }

    @Override
    public UserRoles registerAdmin(UserRegisterationDTO userRegisterationDTO) {
        BazaarUser user = bazaarUserService.createUser(new BazaarUser(null, userRegisterationDTO.firstName(),
                userRegisterationDTO.lastName(), userRegisterationDTO.email(), userRegisterationDTO.phoneNumber(),
                encoder.encode(userRegisterationDTO.password())));

        Set<String> roles = new HashSet<>();
        roles.add(Roles.ADMIN);

        UserRoles adminRoles = new UserRoles(user, roles);

        return repository.save(adminRoles);
    }

}
