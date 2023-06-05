package com.sportsvault.controller;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.sportsvault.dto.ProductDTO;
import com.sportsvault.mapper.ProductMapper;
import com.sportsvault.model.*;
import com.sportsvault.repository.BidRepository;
import com.sportsvault.repository.ProductRepository;
import com.sportsvault.repository.UserRepository;
import com.sportsvault.repository.WonProductsRepository;
import com.sportsvault.service.UserDetailsImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bid")
public class BidController {
    private final BidRepository bidRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final WonProductsRepository wonProductsRepository;
    private final ProductMapper productMapper;

    public BidController(BidRepository bidRepository,
                         ProductRepository productRepository,
                         UserRepository userRepository,
                         WonProductsRepository wonProductsRepository, ProductMapper productMapper) {
        this.bidRepository = bidRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.wonProductsRepository = wonProductsRepository;
        this.productMapper = productMapper;
    }

    @GetMapping("/leader/{productId}")
    @PreAuthorize("hasRole('USER')")
    public String bidLeader(@PathVariable("productId") UUID productId) {
        Bid leaderBid = bidRepository.getHighestBidForProduct(productId);
        if(leaderBid == null)
            return "nobids";
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        if(userDetails.getId().toString().equals(leaderBid.getBidder().getId().toString()))
            return leaderBid.getBidAmount().toString();
        return "error";
    }

    @GetMapping("/getwatchlist")
    @PreAuthorize("hasRole('USER')")
    List<ProductDTO> getWatchlist() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return bidRepository.getWatchlist(userDetails.getId()).stream().map(productMapper::toDto).collect(Collectors.toList());
    }

    @GetMapping("/getwatchlistleaders")
    @PreAuthorize("hasRole('USER')")
    List<UUID> getWatchlistLeaders() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return bidRepository.getWatchListLeaders(userDetails.getId());
    }

    @PostMapping("/auctionbid")
    @PreAuthorize("hasRole('USER')")
    public synchronized String bidForAuction(@RequestBody Bid bid) {
        Date now = new Date();
        if(now.after(bid.getProduct().getExpirationDate()))
            return "error";

        Bid highestBid = bidRepository.getHighestBidForProduct(bid.getProduct().getId());
        if(highestBid == null) {
            bid.setBidDate(now);
            bidRepository.save(bid);
            Float currentPrice = Float.parseFloat(String.format("%.2f", bid.getProduct().getStartingPrice()));
            bid.getProduct().setCurrentPrice(currentPrice);
            productRepository.save(bid.getProduct());
            User bidder = userRepository.findById(bid.getBidder().getId()).get();
            bidder.setBalance(bidder.getBalance() - bid.getBidAmount());
            userRepository.save(bidder);
            return "ok";
        } else {
            if(bid.getBidAmount() <= highestBid.getBidAmount()) {
                bid.getProduct().setCurrentPrice(bid.getBidAmount());
                productRepository.save(bid.getProduct());
                if(bid.getBidAmount() < highestBid.getBidAmount()) {
                    bid.setBidDate(now);
                    bidRepository.save(bid);
                }
                return "notenough";
            } else {
                bid.setBidDate(now);
                bidRepository.save(bid);
                User bidder = userRepository.findById(bid.getBidder().getId()).get();
                if (highestBid.getBidder().getId().toString().equals(bid.getBidder().getId().toString())) {
                    bidder.setBalance(bidder.getBalance() - (bid.getBidAmount() - highestBid.getBidAmount()));
                    userRepository.save(bidder);
                } else {
                    Float currentPrice = Float.parseFloat(String.format("%.2f", highestBid.getBidAmount()));
                    bid.getProduct().setCurrentPrice(currentPrice);
                    productRepository.save(bid.getProduct());
                    bidder.setBalance(bidder.getBalance() - bid.getBidAmount());
                    userRepository.save(bidder);

                    User outbidded = userRepository.findById(highestBid.getBidder().getId()).get();
                    outbidded.setBalance(outbidded.getBalance() + highestBid.getBidAmount());
                    userRepository.save(outbidded);
                }
                return "ok";
            }
        }
    }

    @PostMapping("/buyitnow")
    @PreAuthorize("hasRole('USER')")
    public synchronized String buyProduct(@RequestBody Bid bid) {

        Date now = new Date();
        if(now.after(bid.getProduct().getExpirationDate()))
            return "late";

        Bid highestBid = bidRepository.getHighestBidForProduct(bid.getProduct().getId());
        if (highestBid != null)
            return "bought";

        bid.setBidDate(now);
        bidRepository.save(bid);

        bid.getProduct().setState(ProductState.SOLD);
        bid.getProduct().setExpirationDate(new Date(125, 0, 1));
        productRepository.save(bid.getProduct());

        User bidder = userRepository.findById(bid.getBidder().getId()).get();
        bidder.setBalance(bidder.getBalance() - bid.getBidAmount());
        userRepository.save(bidder);

        User owner = userRepository.findById(bid.getProduct().getOwnerId()).get();
        owner.setBalance(owner.getBalance() + bid.getBidAmount());
        userRepository.save(owner);

        wonProductsRepository.save(WonProducts.builder().product(bid.getProduct()).wonDate(now).bidder(bid.getBidder()).build());
        return "ok";
    }

}
