package com.sportsvault.model;

import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public class RecommendProduct {
    private UUID id;
    private List<String> attributes;

    public RecommendProduct(UUID id, List<String> attributes) {
        this.id = id;
        this.attributes = attributes;
    }

    public UUID getId() {
        return id;
    }

    public List<String> getAttributes() {
        return attributes;
    }
}
