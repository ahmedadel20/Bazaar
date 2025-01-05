package org.bazaar.giza.auth.service;

import org.bazaar.giza.auth.dto.UserRegisterationDTO;
import org.bazaar.giza.auth.entity.UserRoles;

public interface UserRolesService {
    UserRoles registerCustomer(UserRegisterationDTO userRegisterationDTO);

    UserRoles registerManager(UserRegisterationDTO userRegisterationDTO);

    UserRoles registerAdmin(UserRegisterationDTO userRegisterationDTO);
}
