package com.sportsvault.recommender;

import com.sportsvault.model.Product;
import com.sportsvault.model.ProductAttributeValue;
import com.sportsvault.model.RecommendProduct;
import com.sportsvault.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    private List<RecommendProduct> products = new ArrayList<>();
    private final ProductRepository productRepository;

    public RecommendationService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    public void addProduct(RecommendProduct product) {
        products.add(product);
    }



    private Map<RecommendProduct, Map<String, Double>> calculateTfIdf(List<RecommendProduct> productList) {
        Map<String, Integer> documentFrequency = new HashMap<>();
        Map<RecommendProduct, Map<String, Integer>> termFrequency = new HashMap<>();

        // Calculate term frequency and document frequency
        for (RecommendProduct product : productList) {
            Map<String, Integer> productTermFrequency = new HashMap<>();
            Set<String> seenWords = new HashSet<>();
            for (String attribute : product.getAttributes()) {
                productTermFrequency.put(attribute, productTermFrequency.getOrDefault(attribute, 0) + 1);
                if (!seenWords.contains(attribute)) {
                    documentFrequency.put(attribute, documentFrequency.getOrDefault(attribute, 0) + 1);
                    seenWords.add(attribute);
                }
            }
            termFrequency.put(product, productTermFrequency);
        }

        // Calculate TF-IDF
        Map<RecommendProduct, Map<String, Double>> tfidf = new HashMap<>();
        for (RecommendProduct product : productList) {
            Map<String, Double> productTfidf = new HashMap<>();
            for (String attribute : product.getAttributes()) {
                double tf = termFrequency.get(product).get(attribute);
                double idf = Math.log((double) productList.size() / documentFrequency.get(attribute));
                productTfidf.put(attribute, tf * idf);
            }
            tfidf.put(product, productTfidf);
        }

        return tfidf;
    }

    private double cosineSimilarity(Map<String, Double> a, Map<String, Double> b) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (String key : a.keySet()) {
            dotProduct += a.get(key) * b.getOrDefault(key, 0.0);
            normA += Math.pow(a.get(key), 2);
        }
        for (double value : b.values()) {
            normB += Math.pow(value, 2);
        }
        if (normA == 0.0 || normB == 0.0) {
            return 0.0;
        } else {
            return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
        }
    }


    public List<UUID> recommendProducts(List<String> userPreferences, int numRecommendations, List<UUID> favouriteProducts, List<UUID> bidProducts) {
        products = new ArrayList<>();
        List<Product> availableProducts = productRepository.getAvailableProducts();
        for(Product product : availableProducts) {
            List<String> attributes = new ArrayList<>();
            attributes.add(product.getCategory().getName());
            attributes.add(product.getCategory().getSport().getName());
            attributes.add(product.getGender());
            attributes.addAll(product.getAttributeValues().stream().map(ProductAttributeValue::getValue).toList());
            products.add(RecommendProduct.builder()
                            .id(product.getId())
                            .attributes(attributes)
                            .build());
        }
        // Create a pseudo product that represents the user's preferences
        RecommendProduct userProduct = new RecommendProduct(UUID.randomUUID(), userPreferences);

        // Add the pseudo product to the product list
        List<RecommendProduct> allProducts = new ArrayList<>(products);
        allProducts.add(userProduct);

        // Calculate the TF-IDF for each product, including the pseudo product
        Map<RecommendProduct, Map<String, Double>> productTfIdf = calculateTfIdf(allProducts);

        // Extract the TF-IDF for the pseudo product
        Map<String, Double> userProductTfIdf = productTfIdf.get(userProduct);

        // Calculate the cosine similarity between the user and each product
        Map<RecommendProduct, Double> productSimilarity = new HashMap<>();
        for (RecommendProduct product : products) {
            productSimilarity.put(product, cosineSimilarity(userProductTfIdf, productTfIdf.get(product)));
        }


        // Sort products by similarity and return the top numRecommendations products
        return productSimilarity.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(entry -> entry.getKey().getId())
                .filter(elem -> !favouriteProducts.contains(elem) && !bidProducts.contains(elem))
                .limit(numRecommendations)
                .collect(Collectors.toList());
    }

    // The rest of the class (calculateTfIdf and cosineSimilarity methods) would be the same as the TF-IDF example from earlier
}