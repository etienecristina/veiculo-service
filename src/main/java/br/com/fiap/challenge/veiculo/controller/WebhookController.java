package br.com.fiap.challenge.veiculo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebhookController {
    @GetMapping("/webhook")
    @ResponseBody
    public String response(){
        return "Welcome to Spring Boot";
    }
}
