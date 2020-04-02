package edu.application.licenses.controller;

import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "v1/tools")
public class ToolsController {

    private DiscoveryClient discoveryService;

    public ToolsController(DiscoveryClient discoveryService) {
        this.discoveryService = discoveryService;
    }

    @RequestMapping(value = "/eureka/services", method = RequestMethod.GET)
    public List<String> getEurekaServices() {
        return discoveryService.getServices();
    }
}