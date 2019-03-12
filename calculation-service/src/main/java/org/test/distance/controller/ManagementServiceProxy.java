package org.test.distance.controller;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.test.distance.dto.RouteDto;
import org.test.distance.dto.RouteIdsRequestDto;

import java.util.List;

/**
 * @author Viktar Lebedzeu
 */
@FeignClient(name = "management-service")
@RibbonClient(name = "management-service")
public interface ManagementServiceProxy {
    @PostMapping("/route/find-feign")
    public ResponseEntity<List<RouteDto>> findRoutes(@RequestBody RouteIdsRequestDto routeDto);
}
