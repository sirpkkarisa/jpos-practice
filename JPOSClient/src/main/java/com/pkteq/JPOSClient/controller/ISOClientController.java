package com.pkteq.JPOSClient.controller;

import com.pkteq.JPOSClient.service.ISOClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ISOClientController {
    @Autowired
    ISOClientService isoClientService;
    @GetMapping("send/{option}")
    public String handleSend(@PathVariable Integer option ) {
        try {
            isoClientService.sendISOMessage(option);
            return "Message sent";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "Could not send message";
        }
    }
}
