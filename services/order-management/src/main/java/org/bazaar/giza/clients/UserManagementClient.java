package org.bazaar.giza.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "userManagement", url = "${userManagement.service.url}", path = "/api/v1")
public interface UserManagementClient {
    @GetMapping("/{customerId}")
    public CustomerResponse getSingleCustomer(@PathVariable Long customerId);
}
