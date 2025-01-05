package org.bazaar.giza.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment-gateway", url = "${payment.service.url}", path = "/api/v1")
public interface PaymentGatewayClient {

    @GetMapping()
    public String makePayment(@RequestBody UserAccountRequest userAccountRequest);
}
