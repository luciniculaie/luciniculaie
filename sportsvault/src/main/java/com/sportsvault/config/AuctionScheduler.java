package com.sportsvault.config;

import com.sportsvault.model.*;
import com.sportsvault.repository.BidRepository;
import com.sportsvault.repository.ProductRepository;
import com.sportsvault.repository.UserRepository;
import com.sportsvault.repository.WonProductsRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class AuctionScheduler {
    private final ProductRepository productRepository;
    private final BidRepository bidRepository;
    private final UserRepository userRepository;
    private final WonProductsRepository wonProductsRepository;

    public AuctionScheduler(ProductRepository productRepository,
                            BidRepository bidRepository,
                            UserRepository userRepository,
                            WonProductsRepository wonProductsRepository) {
        this.productRepository = productRepository;
        this.bidRepository = bidRepository;
        this.userRepository = userRepository;
        this.wonProductsRepository = wonProductsRepository;
    }


    @Scheduled(cron = "5 0 * * * *") // Run every hour at the beginning of the hour
    public void checkAuctionExpirations() {
        // Your logic to check auction expiration dates and determine winners
        List<Product> auctionsToHandle = productRepository.getProductListByDate();
        for(Product product : auctionsToHandle) {
            Bid highestBid = bidRepository.getHighestBidForProduct(product.getId());
            if(highestBid == null) {
                product.setState(ProductState.EXPIRED);
                productRepository.save(product);
            } else {
                product.setState(ProductState.SOLD);
                productRepository.save(product);

                User bidder = userRepository.findById(highestBid.getBidder().getId()).get();
                bidder.setBalance(bidder.getBalance() + (highestBid.getBidAmount() - product.getCurrentPrice()));
                userRepository.save(bidder);

                User owner = userRepository.findById(product.getOwnerId()).get();
                owner.setBalance(owner.getBalance() + product.getCurrentPrice());
                userRepository.save(owner);

                wonProductsRepository.save(WonProducts.builder().bidder(bidder).wonDate(new Date()).product(product).build());
            }
        }
    }
}
