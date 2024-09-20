package com.example.security.controller;

import com.example.security.dto.VPNMessageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(value = "/api/vpn")
@CrossOrigin
public class VPNController {
    Logger logger = LoggerFactory.getLogger(VPNController.class);


    @GetMapping("/get-message")
    public ResponseEntity<VPNMessageDto> getMessage(){
        logger.info("getMessage method in VPNController started.");

        RestTemplate restTemplate=new RestTemplate();
        String response=restTemplate.getForObject("http://10.13.13.1:3000/",String.class);
        return ResponseEntity.ok(new VPNMessageDto(response));
    }
}
