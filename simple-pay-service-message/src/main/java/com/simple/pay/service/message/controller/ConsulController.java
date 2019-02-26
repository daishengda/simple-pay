package com.simple.pay.service.message.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/consul")
public class ConsulController {

	@RequestMapping("/health")  
    public Object home() {  
        return "OK";  
    } 
}
