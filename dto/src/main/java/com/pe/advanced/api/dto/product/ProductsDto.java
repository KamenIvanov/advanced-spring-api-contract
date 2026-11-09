package com.pe.advanced.api.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pe.advanced.api.dto.PageDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

/**
 * One page of products. The list is typed rather than generic so that the generated
 * specification names {@code ProductDto} instead of describing an array of objects, which is
 * the difference between a client generator producing a usable type and producing a map.
 */
@Schema(description = "A page of products.")
public class ProductsDto extends PageDto {

    @Schema(description = "The products in this page.")
    @JsonProperty(value = "products")
    private List<ProductDto> products = new ArrayList<>();

    public ProductsDto() {
        // POJO
    }

    public ProductsDto(Integer totalPages, Long totalHits, List<ProductDto> products) {
        super(totalPages, totalHits);
        this.products = products;
    }

    public List<ProductDto> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDto> products) {
        this.products = products;
    }
}
