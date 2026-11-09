package com.pe.advanced.api.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pe.advanced.api.dto.PageDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

/**
 * One page of the public projection. A separate page type from {@link ProductsDto} rather
 * than a generic one, so that the generated specification names the element type and a client
 * generator produces a usable class instead of a map.
 */
@Schema(description = "A page of products, as shown to anyone browsing the catalogue.")
public class ProductsDescriptionsDto extends PageDto {

    @Schema(description = "The products in this page.")
    @JsonProperty(value = "products")
    private List<ProductDescriptionDto> products = new ArrayList<>();

    public ProductsDescriptionsDto() {
        // POJO
    }

    public ProductsDescriptionsDto(Integer totalPages, Long totalHits, List<ProductDescriptionDto> products) {
        super(totalPages, totalHits);
        this.products = products;
    }

    public List<ProductDescriptionDto> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDescriptionDto> products) {
        this.products = products;
    }
}
