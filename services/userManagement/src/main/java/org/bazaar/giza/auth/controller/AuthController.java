package org.bazaar.giza.auth.controller;

import org.bazaar.giza.auth.dto.SignInRequestDTO;
import org.bazaar.giza.auth.dto.UserRegisterationDTO;
import org.bazaar.giza.auth.service.JwtService;
import org.bazaar.giza.auth.service.UserRolesService;
import org.bazaar.giza.constant.Roles;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Tag(name = "Auth Controller", description = "Controller for handling mappings for authentication.")
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final UserRolesService userRolesService;
    private final JwtService jwtService;

    @PostMapping("/registerCustomer")
    public String registerCustomer(@RequestBody UserRegisterationDTO dto) {
        userRolesService.registerCustomer(dto);
        return jwtService.authenticateAndGetToken(dto.email(), dto.password());
    }

    @PostMapping("/registerManager")
    @PreAuthorize("hasAuthority('" + Roles.ADMIN + "') or " +
            "(hasAuthority('" + Roles.MANAGER + "')")
    public String registerManager(@RequestBody UserRegisterationDTO dto) {
        userRolesService.registerManager(dto);
        return jwtService.authenticateAndGetToken(dto.email(), dto.password());
    }

    @PostMapping("/registerAdmin")
    @PreAuthorize("hasAuthority('" + Roles.ADMIN + "')")
    public String registerAdmin(@RequestBody UserRegisterationDTO dto) {
        userRolesService.registerAdmin(dto);
        return jwtService.authenticateAndGetToken(dto.email(), dto.password());
    }

    @PostMapping("/generateToken")
    public String authenticateAndGetToken(@Valid @RequestBody SignInRequestDTO signInRequest) {
        return jwtService.authenticateAndGetToken(signInRequest.email(), signInRequest.password());
    }
}
