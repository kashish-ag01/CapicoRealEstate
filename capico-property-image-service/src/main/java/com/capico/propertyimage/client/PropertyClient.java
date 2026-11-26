package com.capico.propertyimage.client;
import com.capico.propertyimage.config.FeignClientConfig;
import com.capico.propertyimage.dto.response.PropertyDetailsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "CAPICO-PROPERTY-SERVICE")
public interface PropertyClient {

    @GetMapping("/api/properties/{propertyId}")
    PropertyDetailsResponse getPropertyById(
            @PathVariable("propertyId") Long propertyId
    );
}