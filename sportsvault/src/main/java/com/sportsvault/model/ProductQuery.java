package com.sportsvault.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;

import java.util.Date;
import java.util.List;
import java.util.UUID;
    @Entity
    @Table(name = "products")
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public class ProductQuery {

        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        @JdbcTypeCode(java.sql.Types.VARCHAR)
        private UUID id;

        @Column(name = "name")
        private String name;

        @Column(name = "description")
        private String description;

        @Column(name = "gender")
        private String gender;

        @Column(name = "category_id")
        @JdbcTypeCode(java.sql.Types.VARCHAR)
        private UUID categoryId;

        public UUID getId() {
            return id;
        }

        public void setId(UUID id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public UUID getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(UUID categoryId) {
            this.categoryId = categoryId;
        }
    }
