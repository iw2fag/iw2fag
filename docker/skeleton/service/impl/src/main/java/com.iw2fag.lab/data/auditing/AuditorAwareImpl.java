package com.iw2fag.lab.data.auditing;

import com.iw2fag.lab.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Autowired
    private UserService userService;

    public AuditorAwareImpl() {

    }

    public String getCurrentAuditor() {
        if (userService.getCurrentUser() != null) {
            return String.valueOf(userService.getCurrentUser().getId());
        }
        return "SYSTEM";
    }
}
