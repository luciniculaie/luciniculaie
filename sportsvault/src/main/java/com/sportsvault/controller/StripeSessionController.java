package com.sportsvault.controller;

import com.sportsvault.model.StripeSession;
import com.sportsvault.model.User;
import com.sportsvault.repository.StripeSessionRepository;
import com.sportsvault.repository.UserRepository;
import com.sportsvault.service.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/stripesession")
public class StripeSessionController {
    private final StripeSessionRepository stripeSessionRepository;
    private final UserRepository userRepository;

    public StripeSessionController(StripeSessionRepository stripeSessionRepository,
                                   UserRepository userRepository) {
        this.stripeSessionRepository = stripeSessionRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/verifysession")
    boolean verifySession(@RequestBody StripeSession session) {
        boolean isValid = stripeSessionRepository.findBySessionId(session.getSession_id()) == null;
        if(isValid) {
            SecurityContext context = SecurityContextHolder.getContext();
            Authentication authentication = context.getAuthentication();
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            User user = userRepository.getById(userDetails.getId());
            user.setBalance(session.getAmount() + user.getBalance());
            userRepository.save(user);
            stripeSessionRepository.save(session);
        }
        return isValid;
    }
}
