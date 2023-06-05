package com.sportsvault.controller;

import com.sportsvault.model.SportsAttribute;
import com.sportsvault.repository.SportsAttributeRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/sportsattributes")
public class SportsAttributeController {
    private final SportsAttributeRepository sportsAttributeRepository;

    public SportsAttributeController(SportsAttributeRepository sportsAttributeRepository) {
        this.sportsAttributeRepository = sportsAttributeRepository;
    }

    @GetMapping("/all")
    List<SportsAttribute> getAll() {
        return sportsAttributeRepository.getAll();
    }
}
