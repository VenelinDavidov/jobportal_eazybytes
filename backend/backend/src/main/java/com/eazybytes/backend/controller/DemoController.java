package com.eazybytes.backend.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

//    @GetMapping("/home")
//    @RequestMapping
    @RequestMapping(value = "/home", method = {RequestMethod.GET, RequestMethod.POST})
//    consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)

    public String sayHello() {
        return "Hello  Word!"; //Json
    }
}
