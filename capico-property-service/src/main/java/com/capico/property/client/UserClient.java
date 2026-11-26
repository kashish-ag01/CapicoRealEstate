package com.capico.property.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "CAPICO-USER-SERVICE")
public interface UserClient {

    @GetMapping("/api/users/internal/{id}")
    UserSummaryResponse getUserById(
            @PathVariable("id") Long id
    );
}