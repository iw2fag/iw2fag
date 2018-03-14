package com.iw2fag.lab.controller;

import com.iw2fag.lab.exception.InvalidUserException;
import com.iw2fag.lab.service.SecurityService;
import com.iw2fag.lab.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/")
public class MainControllerImpl implements MainController {

    private final Logger logger = LoggerFactory.getLogger(MainControllerImpl.class);

    @Autowired
    UserService userService;

    @Autowired
    SecurityService securityService;

    @RequestMapping("/getUsers")
    @ResponseBody
    public ResponseEntity<Object> getUsers() {
        if (!securityService.validateUser("token")) {
            throw new InvalidUserException();
        }
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @RequestMapping(path = "/getUserStatus/{userId}")
    @ResponseBody
    public ResponseEntity<Object> getUserStatus(@PathVariable String userId) {
        return new ResponseEntity<>("1".equals(userId) ? "registered" : "unknown", HttpStatus.OK);
    }

    @RequestMapping("/getAPIVersion")
    @ResponseBody
    public ResponseEntity<Object> getAPIVersion() {
        return new ResponseEntity<>("0.9", HttpStatus.OK);
    }

    @RequestMapping("/")
    public ResponseEntity<Object> hello() {
        logger.info("hello !!");
        return new ResponseEntity<>("Hello.", HttpStatus.OK);
    }
}
