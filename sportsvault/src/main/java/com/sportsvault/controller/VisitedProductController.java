package com.sportsvault.controller;

import com.sportsvault.model.Product;
import com.sportsvault.model.VisitedProduct;
import com.sportsvault.repository.VisitedProductsRepository;
import com.sportsvault.service.UserDetailsImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/api/visitedproduct")
public class VisitedProductController {
    private final VisitedProductsRepository visitedProductsRepository;

    public VisitedProductController(VisitedProductsRepository visitedProductsRepository) {
        this.visitedProductsRepository = visitedProductsRepository;
    }

    @PostMapping("/addtovisited/{productId}")
    @PreAuthorize("hasRole('USER')")
    public void addToVisited(@PathVariable("productId") UUID productId) {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        System.out.println(userDetails.getId());
        visitedProductsRepository.save(VisitedProduct.builder()
                .product(Product.builder().id(productId).build())
                .userId(userDetails.getId())
                .createdDate(new Date()).build());
    }
}
