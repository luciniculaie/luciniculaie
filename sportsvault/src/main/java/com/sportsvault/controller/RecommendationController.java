package com.sportsvault.controller;

import com.sportsvault.dto.ProductDTO;
import com.sportsvault.mapper.ProductMapper;
import com.sportsvault.model.Product;
import com.sportsvault.model.ProductAttributeValue;
import com.sportsvault.model.RecommendProduct;
import com.sportsvault.recommender.RecommendationService;
import com.sportsvault.recommender.SlopeOneItem;
import com.sportsvault.recommender.SlopeOneRecommendationService;
import com.sportsvault.recommender.SlopeOneUser;
import com.sportsvault.repository.*;
import com.sportsvault.service.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/recommend")
public class RecommendationController {

    private final RecommendationService recommendationService;
    private final SlopeOneRecommendationService slopeOneRecommendationService;
    private final FavouriteProductRepository favouriteProductRepository;
    private final VisitedProductsRepository visitedProductsRepository;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final BidRepository bidRepository;
    private final UserRepository userRepository;

    public RecommendationController(RecommendationService recommendationService, FavouriteProductRepository favouriteProductRepository, VisitedProductsRepository visitedProductsRepository, ProductRepository productRepository, ProductMapper productMapper, BidRepository bidRepository, CategoryRepository categoryRepository, SlopeOneRecommendationService slopeOneRecommendationService,
                                    UserRepository userRepository) {
        this.recommendationService = recommendationService;
        this.favouriteProductRepository = favouriteProductRepository;
        this.visitedProductsRepository = visitedProductsRepository;
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.bidRepository = bidRepository;
        this.slopeOneRecommendationService = slopeOneRecommendationService;
        this.userRepository = userRepository;
    }

    @GetMapping("/recommendations")
    public List<ProductDTO> recommendProducts() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<UUID> bidList = bidRepository.getWatchlistIds(userDetails.getId());
        List<UUID> favouriteList = favouriteProductRepository.getFavouriteIds(userDetails.getId());

        /* RECOMMENDATIONS MADE USING SLOPE ONE ALGORITHM */

//        List<UUID> topProducts = slopeOneRecommendationService.slopeOne(userDetails.getId(), 12, favouriteList, bidList);
//
//        return productRepository.findProductsByIds(topProducts)
//              .stream().map(productMapper::toDto).collect(Collectors.toList());

        /* RECOMMENDATIONS MADE USING TF-IDF ALGORITHM */

        List<UUID> topProducts = recommendationService.recommendProducts(getInput(userDetails.getId()), 12, favouriteList, bidList);

        return productRepository.findProductsByIds(topProducts)
                .stream().map(productMapper::toDto).collect(Collectors.toList());
    }

    @GetMapping("/getinput/{userId}")
    public List<String> getInput(@PathVariable("userId") UUID userId) {
        Map<String, Double> userMap = new HashMap<>();
        List<Product> favouriteProducts = favouriteProductRepository.getFavourites(userId);
        List<Product> lastVisitedProducts = productRepository.findAllById(visitedProductsRepository.findTop20ByUserIdOrderByCreatedDateDesc(userId.toString()).stream().map(pv -> UUID.fromString((String) pv[0])).collect(Collectors.toList()));
        List<Product> lastBiddedProducts = productRepository.findAllById(bidRepository.findTop5ByUserIdOrderByCreatedDateDesc(userId.toString()).stream().map(pv -> UUID.fromString((String) pv[0])).collect(Collectors.toList()));

        double raiseCoefficientFavourite = 1.0 / (Math.min(5, favouriteProducts.size()));
        double raiseCoefficientBidded = 1.0 / (Math.min(5, lastBiddedProducts.size()));
        double raiseCoefficientVisited = 1.0 / (Math.min(20, lastVisitedProducts.size()));



        for(Product favouriteProduct : favouriteProducts) {
            String sport = favouriteProduct.getCategory().getSport().getName();
            String category = favouriteProduct.getCategory().getName();
            String gender = favouriteProduct.getGender();
            List<String> attributeValues = favouriteProduct.getAttributeValues().stream().map(ProductAttributeValue::getValue).toList();

            if(!userMap.containsKey(sport))
                userMap.put(sport, raiseCoefficientFavourite);
            else {
                userMap.put(sport, userMap.get(sport) + raiseCoefficientFavourite);
            }

            if(!userMap.containsKey(category))
                userMap.put(category, raiseCoefficientFavourite);
            else {
                userMap.put(category, userMap.get(category) + raiseCoefficientFavourite);
            }

            if(!userMap.containsKey(gender))
                userMap.put(gender, raiseCoefficientFavourite);
            else {
                userMap.put(gender, userMap.get(gender) + raiseCoefficientFavourite);
            }

            for(String attributeValue : attributeValues) {
                if(!userMap.containsKey(attributeValue))
                    userMap.put(attributeValue, raiseCoefficientFavourite);
                else {
                    userMap.put(attributeValue, userMap.get(attributeValue) + raiseCoefficientFavourite);
                }
            }
        }

        for(Product biddedProduct : lastBiddedProducts) {
            String sport = biddedProduct.getCategory().getSport().getName();
            String category = biddedProduct.getCategory().getName();
            String gender = biddedProduct.getGender();
            List<String> attributeValues = biddedProduct.getAttributeValues().stream().map(ProductAttributeValue::getValue).toList();

            if(!userMap.containsKey(sport))
                userMap.put(sport, raiseCoefficientBidded);
            else {
                userMap.put(sport, userMap.get(sport) + raiseCoefficientBidded);
            }

            if(!userMap.containsKey(category))
                userMap.put(category, raiseCoefficientBidded);
            else {
                userMap.put(category, userMap.get(category) + raiseCoefficientBidded);
            }

            if(!userMap.containsKey(gender))
                userMap.put(gender, raiseCoefficientBidded);
            else {
                userMap.put(gender, userMap.get(gender) + raiseCoefficientBidded);
            }

            for(String attributeValue : attributeValues) {
                if(!userMap.containsKey(attributeValue))
                    userMap.put(attributeValue, raiseCoefficientBidded);
                else {
                    userMap.put(attributeValue, userMap.get(attributeValue) + raiseCoefficientBidded);
                }
            }
        }

        for(Product visitedProduct : lastVisitedProducts) {
            String sport = visitedProduct.getCategory().getSport().getName();
            String category = visitedProduct.getCategory().getName();
            String gender = visitedProduct.getGender();
            List<String> attributeValues = visitedProduct.getAttributeValues().stream().map(ProductAttributeValue::getValue).toList();

            if(!userMap.containsKey(sport))
                userMap.put(sport, raiseCoefficientVisited);
            else {
                userMap.put(sport, userMap.get(sport) + raiseCoefficientVisited);
            }

            if(!userMap.containsKey(category))
                userMap.put(category, raiseCoefficientVisited);
            else {
                userMap.put(category, userMap.get(category) + raiseCoefficientVisited);
            }

            if(!userMap.containsKey(gender))
                userMap.put(gender, raiseCoefficientVisited);
            else {
                userMap.put(gender, userMap.get(gender) + raiseCoefficientVisited);
            }

            for(String attributeValue : attributeValues) {
                if(!userMap.containsKey(attributeValue))
                    userMap.put(attributeValue, raiseCoefficientVisited);
                else {
                    userMap.put(attributeValue, userMap.get(attributeValue) + raiseCoefficientVisited);
                }
            }
        }

        for (Map.Entry<String, Double> entry : userMap.entrySet()) {
            double value = entry.getValue();
            if (value >= 2.5) {
                userMap.put(entry.getKey(), 3.0);
            } else if (value >= 1.25) {
                userMap.put(entry.getKey(), 2.0);
            } else if (value >= 0.25) {
                userMap.put(entry.getKey(), 1.0);
            } else {
                userMap.put(entry.getKey(), 0.0);
            }
        }

        List<String> repeatedKeysList = new ArrayList<>();
        for (Map.Entry<String, Double> entry : userMap.entrySet()) {
            int repetitions = entry.getValue().intValue();
            for (int i = 0; i < repetitions; i++) {
                repeatedKeysList.add(entry.getKey());
            }
        }

        return repeatedKeysList;
    }
}

