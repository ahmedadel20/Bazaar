package org.bazaar.giza.auth.controller;

import org.bazaar.giza.auth.dto.SignInRequestDTO;
import org.bazaar.giza.auth.dto.UserRegisterationDTO;
import org.bazaar.giza.auth.service.JwtService;
import org.bazaar.giza.auth.service.UserRolesService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserRolesService userRolesService;
    private final JwtService jwtService;

    @PostMapping("/registerCustomer")
    public String registerCustomer(@RequestBody UserRegisterationDTO dto) {
        userRolesService.registerCustomer(dto);
        return jwtService.authenticateAndGetToken(dto.email(), dto.password());
    }

    @PostMapping("/registerManager")
    public String registerManager(@RequestBody UserRegisterationDTO dto) {
        userRolesService.registerManager(dto);
        return jwtService.authenticateAndGetToken(dto.email(), dto.password());
    }

    @PostMapping("/registerAdmin")
    public String registerAdmin(@RequestBody UserRegisterationDTO dto) {
        userRolesService.registerAdmin(dto);
        return jwtService.authenticateAndGetToken(dto.email(), dto.password());
    }

    @PostMapping("/generateToken")
    public String authenticateAndGetToken(@Valid @RequestBody SignInRequestDTO signInRequest) {
        return jwtService.authenticateAndGetToken(signInRequest.email(), signInRequest.password());
    }
}
