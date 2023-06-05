package com.sportsvault.recommender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KNNRecommendationService {

    private final Map<String, Map<String, Double>> userRatings;

    public KNNRecommendationService(Map<String, Map<String, Double>> userRatings) {
        this.userRatings = userRatings;
    }

    public List<String> getRecommendationsForUser(String userId, int k, int numRecommendations) {
        // Step 1: Calculate similarity between the target user and other users
        Map<String, Double> similarities = calculateSimilarities(userId);

        // Step 2: Find the k most similar users
        List<String> similarUsers = findMostSimilarUsers(similarities, k);

        // Step 3: Generate recommendations based on the ratings of the similar users
        Map<String, Double> recommendations = generateRecommendations(similarUsers, userId);

        // Step 4: Sort and return the top N recommendations
        return sortAndRetrieveTopN(recommendations, numRecommendations);
    }

    private Map<String, Double> calculateSimilarities(String userId) {
        Map<String, Double> similarities = new HashMap<>();

        // Calculate similarity between the target user and other users
        for (String user : userRatings.keySet()) {
            if (!user.equals(userId)) {
                double similarity = calculateSimilarity(user, userId);
                similarities.put(user, similarity);
            }
        }

        return similarities;
    }

    private double calculateSimilarity(String user1, String user2) {
        // Calculate similarity between user1 and user2 using a similarity measure (e.g., cosine similarity, Pearson correlation)
        // Implement your similarity calculation logic here
        // Return the calculated similarity value
        return 0.0;
    }

    private List<String> findMostSimilarUsers(Map<String, Double> similarities, int k) {
        // Sort the similarities map in descending order based on similarity values
        List<String> sortedUsers = new ArrayList<>(similarities.keySet());
        sortedUsers.sort((user1, user2) -> similarities.get(user2).compareTo(similarities.get(user1)));

        // Retrieve the top k most similar users
        return sortedUsers.subList(0, Math.min(k, sortedUsers.size()));
    }

    private Map<String, Double> generateRecommendations(List<String> similarUsers, String userId) {
        Map<String, Double> recommendations = new HashMap<>();

        // Generate recommendations based on the ratings of the similar users
        for (String user : similarUsers) {
            Map<String, Double> ratings = userRatings.get(user);
            for (String item : ratings.keySet()) {
                if (!userRatings.get(user).containsKey(item)) {
                    double rating = ratings.get(item);
                    recommendations.put(item, recommendations.getOrDefault(item, 0.0) + rating);
                }
            }
        }

        return recommendations;
    }

    private List<String> sortAndRetrieveTopN(Map<String, Double> recommendations, int numRecommendations) {
        // Sort the recommendations map in descending order based on recommendation values
        List<String> sortedItems = new ArrayList<>(recommendations.keySet());
        sortedItems.sort((item1, item2) -> recommendations.get(item2).compareTo(recommendations.get(item1)));

        // Retrieve the top N recommendations
        return sortedItems.subList(0, Math.min(numRecommendations, sortedItems.size()));
    }
}
