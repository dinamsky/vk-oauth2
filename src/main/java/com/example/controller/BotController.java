package com.example.controller;


import com.example.entity.Bot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
public class BotController {
    private static final String BOT_URI = "http://localhost:8080/newbot" ;
    @Autowired
    RestTemplate restTemplate;
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @PostMapping("/newbot")
    public ResponseEntity<?> newBot(@RequestParam("name") String name, @RequestParam("token") String token){
         String resp=restBot(name,token);
        return new ResponseEntity<>(resp, HttpStatus.OK);}

    public String restBot(String name,String token) {
    HttpEntity<Bot> request = new HttpEntity<>(new Bot(token,name));
    ResponseEntity<Bot> response = restTemplate
            .exchange(BOT_URI, HttpMethod.POST, request, Bot.class);
        Bot bot = response.getBody();
    return bot.getUuid(); }



}
