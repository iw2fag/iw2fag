package com.iw2fag.lab.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@Component
@RestController
@RequestMapping("/")
public interface MainController {

    @RequestMapping("/getUsers")
    @ResponseBody
    ResponseEntity<Object> getUsers();

    @RequestMapping(path = "/getUserStatus")
    @ResponseBody
    ResponseEntity<Object> getUserStatus(@RequestParam String userId);

    @RequestMapping("/getAPIVersion")
    @ResponseBody
    ResponseEntity<Object> getAPIVersion();

}
