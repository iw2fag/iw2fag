package com.iw2fag.lab.repository;

import com.iw2fag.lab.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
}
