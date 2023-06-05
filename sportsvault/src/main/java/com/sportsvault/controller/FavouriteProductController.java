package com.sportsvault.controller;

import com.sportsvault.dto.ProductDTO;
import com.sportsvault.mapper.ProductMapper;
import com.sportsvault.model.FavouriteProduct;
import com.sportsvault.model.Product;
import com.sportsvault.repository.FavouriteProductRepository;
import com.sportsvault.service.UserDetailsImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/favourite")
public class FavouriteProductController {
    private final FavouriteProductRepository favouriteProductRepository;
    private final ProductMapper productMapper;

    public FavouriteProductController(FavouriteProductRepository favouriteProductRepository, ProductMapper productMapper) {
        this.favouriteProductRepository = favouriteProductRepository;
        this.productMapper = productMapper;
    }

    @GetMapping("/getfavouriteids")
    @PreAuthorize("hasRole('USER')")
    List<UUID> getFavouriteProductIds() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return favouriteProductRepository.getFavouriteIds(userDetails.getId());
    }

    @GetMapping("/getfavourites")
    @PreAuthorize("hasRole('USER')")
    List<ProductDTO> getFavouriteProducts() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();


        return favouriteProductRepository.getFavourites(userDetails.getId()).stream().map(productMapper :: toDto).collect(Collectors.toList());
    }

    @PostMapping("/addtofavourite/{productId}")
    @PreAuthorize("hasRole('USER')")
    public void addToFavourite(@PathVariable("productId") UUID productId) {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        favouriteProductRepository.save(FavouriteProduct
                .builder()
                .product(Product.builder().id(productId).build())
                .userId(userDetails.getId()).build());
    }

    @PostMapping("/deletefromfavourite/{productId}")
    @PreAuthorize("hasRole('USER')")
    public void deleteFromFavourite(@PathVariable("productId") UUID productId) {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        favouriteProductRepository.deleteById(favouriteProductRepository.findByProductAndUser(productId, userDetails.getId()));
    }


}
