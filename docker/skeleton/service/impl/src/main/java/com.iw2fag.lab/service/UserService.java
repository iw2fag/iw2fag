package com.iw2fag.lab.service;

import com.iw2fag.lab.model.User;

import java.util.List;


public interface UserService {

    List<User> findAll();

    User getCurrentUser();

}
