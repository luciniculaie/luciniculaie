package com.sportsvault.dto;

import com.sportsvault.model.Category;
import com.sportsvault.model.ProductAttributeValue;
import com.sportsvault.model.ProductState;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    private UUID id;
    private String name;
    private String description;
    private List<ProductAttributeValueDTO> productAttributeValueDTOList;
    private String gender;
    private Date expirationDate;
    private String auctionType;
    private ProductState state;
    private Float startingPrice;
    private Float currentPrice;
    private Category category;
    private String mainPhotoName;
    private UUID ownerId;

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Float getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(Float currentPrice) {
        this.currentPrice = currentPrice;
    }
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getMainPhotoName() {
        return mainPhotoName;
    }

    public void setMainPhotoName(String mainPhotoName) {
        this.mainPhotoName = mainPhotoName;
    }

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

    public List<ProductAttributeValueDTO> getProductAttributeValueDTOList() {
        return productAttributeValueDTOList;
    }

    public void setProductAttributeValueDTOList(List<ProductAttributeValueDTO> productAttributeValueDTOList) {
        this.productAttributeValueDTOList = productAttributeValueDTOList;
    }

    public String getAuctionType() {
        return auctionType;
    }

    public void setAuctionType(String auctionType) {
        this.auctionType = auctionType;
    }

    public ProductState getState() {
        return state;
    }

    public void setState(ProductState state) {
        this.state = state;
    }

    public Float getStartingPrice() {
        return startingPrice;
    }

    public void setStartingPrice(Float startingPrice) {
        this.startingPrice = startingPrice;
    }

}
