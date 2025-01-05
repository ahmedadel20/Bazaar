package org.bazaar.paymentGateway;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@SecurityScheme(
    name = "Authorization",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer"
)
@OpenAPIDefinition(
    info = @Info(
        title = "Bazaar - Payment Gateway", 
        description = "OpenAPI documentation for payment gateway service."
        + "\n\n Authorization is done through Jwt bearer tokens."
        + "\n\n NOTE: Admins have the authority to access any mapping.",
        version = "1.0"
    ),
    servers = @Server(url = "http://localhost:8085/", description = "Dev ENV")
)
public class OpenApiConfig {

}
