package com.sportsvault.recommender;

import com.sportsvault.model.User;
import com.sportsvault.repository.*;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SlopeOneRecommendationService {
    private final ProductRepository productRepository;

    private final UserRepository userRepository;
    private static Map<SlopeOneItem, Map<SlopeOneItem, Double>> diff = new HashMap<>();
    private static Map<SlopeOneItem, Map<SlopeOneItem, Integer>> freq = new HashMap<>();
    private static Map<SlopeOneUser, HashMap<SlopeOneItem, Double>> inputData;
    private static Map<SlopeOneUser, HashMap<SlopeOneItem, Double>> outputData = new HashMap<>();

    public SlopeOneRecommendationService(ProductRepository productRepository, UserRepository userRepository,
                                         BidRepository bidRepository,
                                         FavouriteProductRepository favouriteProductRepository,
                                         VisitedProductsRepository visitedProductsRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.bidRepository = bidRepository;
        this.favouriteProductRepository = favouriteProductRepository;
        this.visitedProductsRepository = visitedProductsRepository;
    }

    public List<UUID> slopeOne(UUID userId, Integer numRecommendations, List<UUID> favouriteProducts, List<UUID> bidProducts) {
        inputData = initializeData();
        buildDifferencesMatrix(inputData);

        HashMap<SlopeOneItem, Double> ratings = predict(inputData, new SlopeOneUser(userId));
        List<Map.Entry<SlopeOneItem, Double>> sortedEntries = new ArrayList<>(ratings.entrySet());
        sortedEntries.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        return new ArrayList<>(sortedEntries.stream()
                .map(entry -> entry.getKey().getId())
                .filter(elem -> !favouriteProducts.contains(elem) && !bidProducts.contains(elem))
                .limit(numRecommendations)
                .toList());
    }

    private final BidRepository bidRepository;
    private final FavouriteProductRepository favouriteProductRepository;
    private final VisitedProductsRepository visitedProductsRepository;

    /**
     * Based on the available data, calculate the relationships between the
     * items and number of occurences
     *
     * @param data
     *            existing user data and their items' ratings
     */
    private static void buildDifferencesMatrix(Map<SlopeOneUser, HashMap<SlopeOneItem, Double>> data) {
        for (HashMap<SlopeOneItem, Double> user : data.values()) {
            for (Map.Entry<SlopeOneItem, Double> e : user.entrySet()) {
                if (!diff.containsKey(e.getKey())) {
                    diff.put(e.getKey(), new HashMap<>());
                    freq.put(e.getKey(), new HashMap<>());
                }
                for (Map.Entry<SlopeOneItem, Double> e2 : user.entrySet()) {
                    int oldCount = 0;
                    if (freq.get(e.getKey()).containsKey(e2.getKey())) {
                        oldCount = freq.get(e.getKey()).get(e2.getKey()).intValue();
                    }
                    double oldDiff = 0.0;
                    if (diff.get(e.getKey()).containsKey(e2.getKey())) {
                        oldDiff = diff.get(e.getKey()).get(e2.getKey()).doubleValue();
                    }
                    double observedDiff = e.getValue() - e2.getValue();
                    freq.get(e.getKey()).put(e2.getKey(), oldCount + 1);
                    diff.get(e.getKey()).put(e2.getKey(), oldDiff + observedDiff);
                }
            }
        }
        for (SlopeOneItem j : diff.keySet()) {
            for (SlopeOneItem i : diff.get(j).keySet()) {
                double oldValue = diff.get(j).get(i).doubleValue();
                int count = freq.get(j).get(i).intValue();
                diff.get(j).put(i, oldValue / count);
            }
        }
    }

    /**
     * Based on existing data predict all missing ratings. If prediction is not
     * possible, the value will be equal to -1
     *
     * @param data existing user data and their items' ratings
     * @return
     */
    private HashMap<SlopeOneItem, Double> predict(Map<SlopeOneUser, HashMap<SlopeOneItem, Double>> data, SlopeOneUser userToPredict) {
        HashMap<SlopeOneItem, Double> uPred = new HashMap<SlopeOneItem, Double>();
        HashMap<SlopeOneItem, Integer> uFreq = new HashMap<SlopeOneItem, Integer>();
        for (SlopeOneItem j : diff.keySet()) {
            uFreq.put(j, 0);
            uPred.put(j, 0.0);
        }
        for (Map.Entry<SlopeOneUser, HashMap<SlopeOneItem, Double>> e : data.entrySet()) {
            if(e.getKey().equals(userToPredict)) {
                for (SlopeOneItem j : e.getValue().keySet()) {
                    for (SlopeOneItem k : diff.keySet()) {
                        try {
                            double predictedValue = diff.get(k).get(j) + e.getValue().get(j);
                            double finalValue = predictedValue * freq.get(k).get(j);
                            uPred.put(k, uPred.get(k) + finalValue);
                            uFreq.put(k, uFreq.get(k) + freq.get(k).get(j));
                        } catch (NullPointerException e1) {
                        }
                    }
                }
                HashMap<SlopeOneItem, Double> clean = new HashMap<SlopeOneItem, Double>();
                for (SlopeOneItem j : uPred.keySet()) {
                    if (uFreq.get(j) > 0) {
                        clean.put(j, uPred.get(j) / uFreq.get(j));
                    }
                }
                List<SlopeOneItem> items = productRepository.getAvailableProductsIds().stream().map(SlopeOneItem::new).toList();
                for (SlopeOneItem j : items) {
                    if (e.getValue().containsKey(j)) {
                        clean.put(j, e.getValue().get(j));
                    } else if (!clean.containsKey(j)) {
                        clean.put(j, -1.0);
                    }
                }
                outputData.put(e.getKey(), clean);
            }
        }
        return outputData.get(userToPredict);
    }

    private static void printData(Map<SlopeOneUser, HashMap<SlopeOneItem, Double>> data) {
        for (SlopeOneUser user : data.keySet()) {
            System.out.println(user.getId() + ":");
            print(data.get(user));
        }
    }

    private static void print(HashMap<SlopeOneItem, Double> hashMap) {
        NumberFormat formatter = new DecimalFormat("#0.000");
        for (SlopeOneItem j : hashMap.keySet()) {
            System.out.println(" " + j.getId() + " --> " + formatter.format(hashMap.get(j).doubleValue()));
        }
    }

    public Map<SlopeOneUser, HashMap<SlopeOneItem, Double>> initializeData() {
        Map<SlopeOneUser, HashMap<SlopeOneItem, Double>> data = new HashMap<>();
        HashMap<SlopeOneItem, Double> newUser;
        Set<SlopeOneItem> bidSet;
        Set<SlopeOneItem> favouriteSet;
        List<SlopeOneItem> visitedList;
        List<User> allUsers = userRepository.findAll();
        for (User allUser : allUsers) {
            newUser = new HashMap<>();
            bidSet = bidRepository.getWatchlistIds(allUser.getId()).stream().map(SlopeOneItem::new).collect(Collectors.toSet());
            favouriteSet = favouriteProductRepository.getFavouriteIds(allUser.getId()).stream().map(SlopeOneItem::new).collect(Collectors.toSet());
            visitedList = visitedProductsRepository.getVisitedProductsByUserId(allUser.getId()).stream().map(SlopeOneItem::new).collect(Collectors.toList());
            for (SlopeOneItem item : bidSet) {
                newUser.put(item, 0.9D);
            }
            for (SlopeOneItem item : favouriteSet) {
                if (newUser.containsKey(item))
                    newUser.put(item, 1D);
                else
                    newUser.put(item, 0.7D);
            }
            for (SlopeOneItem item : visitedList) {
                if (newUser.containsKey(item)) {
                    Double value = newUser.get(item);
                    if (value < 0.5D)
                        newUser.put(item, Math.min(value + 0.15D, 0.5D));
                    else
                        newUser.put(item, Math.min((value + 0.05D), 1D));
                } else
                    newUser.put(item, 0.15D);
            }
            data.put(new SlopeOneUser(allUser.getId()), newUser);
        }
        return data;
    }


}