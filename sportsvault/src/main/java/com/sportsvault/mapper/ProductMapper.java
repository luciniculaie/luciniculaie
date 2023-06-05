package com.sportsvault.mapper;

import com.sportsvault.dto.ProductAttributeValueDTO;
import com.sportsvault.dto.ProductDTO;
import com.sportsvault.model.Attribute;
import com.sportsvault.model.Product;
import com.sportsvault.model.ProductAttributeValue;
import com.sportsvault.repository.ProductAttributeValueRepository;
import com.sportsvault.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ProductMapper {
    private final ProductRepository productRepository;
    private final ProductAttributeValueRepository productAttributeValueRepository;

    public ProductMapper(ProductRepository productRepository,
                         ProductAttributeValueRepository productAttributeValueRepository) {
        this.productRepository = productRepository;
        this.productAttributeValueRepository = productAttributeValueRepository;
    }

    public Product toEntity(ProductDTO productDTO) {
        return Product.builder()
                .id(productDTO.getId())
                .name(productDTO.getName())
                .description(productDTO.getDescription())
                .state(productDTO.getState())
                .expirationDate(productDTO.getExpirationDate())
                .gender(productDTO.getGender())
                .auctionType(productDTO.getAuctionType())
                .startingPrice(productDTO.getStartingPrice())
                .currentPrice(productDTO.getCurrentPrice())
                .ownerId(productDTO.getOwnerId())
                .category(productDTO.getCategory())
                .mainPhotoName(productDTO.getMainPhotoName())
                .attributeValues(productDTO.getProductAttributeValueDTOList().stream().map(productAttributeValueDTO ->
                        ProductAttributeValue.builder()
                                .product(Product.builder()
                                        .id(productDTO.getId())
                                        .build())
                                .value(productAttributeValueDTO.getAttributeValue())
                                .attribute(Attribute.builder()
                                        .id(productAttributeValueDTO.getAttributeId())
                                        .name(productAttributeValueDTO.getAttributeName())
                                        .build())
                                .build()).collect(Collectors.toList()))
                .build();
    }

    public ProductDTO toDto(Product product) {

        return ProductDTO.builder()
                .description(product.getDescription())
                .id(product.getId())
                .gender(product.getGender())
                .expirationDate(product.getExpirationDate())
                .ownerId(product.getOwnerId())
                .state(product.getState())
                .startingPrice(product.getStartingPrice())
                .currentPrice(product.getCurrentPrice())
                .auctionType(product.getAuctionType())
                .name(product.getName())
                .category(product.getCategory())
                .mainPhotoName(product.getMainPhotoName())
                .productAttributeValueDTOList(product.getAttributeValues().stream().map(productAttributeValue -> ProductAttributeValueDTO.builder()
                        .attributeId(productAttributeValue.getAttribute().getId())
                        .attributeName(productAttributeValue.getAttribute().getName())
                        .attributeValue(productAttributeValue.getValue())
                        .id(productAttributeValue.getId())
                        .build()).collect(Collectors.toList()))
                .build();
    }
}
