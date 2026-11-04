package com.pe.advanced.api.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pe.advanced.api.dto.AbstractEntityDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

/**
 * A product as the server returns it: the caller's fields, plus everything the server owns -
 * identity, timestamps, version and status.
 */
@Schema(description = "The product's full data, as its owner sees it.")
public class ProductDto extends AbstractEntityDto {

    @Schema(description = "The product's name.")
    @JsonProperty(value = "name")
    private String name;

    @Schema(description = "The product's stock keeping unit. Unique across all products.")
    @JsonProperty(value = "sku")
    private String sku;

    @Schema(description = "The product's price, with two decimal places.", example = "19.99")
    @JsonProperty(value = "price")
    private BigDecimal price;

    @Schema(description = "The product's current status.")
    @JsonProperty(value = "status")
    private ProductStatusDto status;

    @Schema(description = "The product's physical specification. Absent while the product has not been measured.")
    @JsonProperty(value = "specification")
    private ProductSpecificationDto specification;

    public ProductDto() {
        // POJO
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public ProductStatusDto getStatus() {
        return status;
    }

    public void setStatus(ProductStatusDto status) {
        this.status = status;
    }

    public ProductSpecificationDto getSpecification() {
        return specification;
    }

    public void setSpecification(ProductSpecificationDto specification) {
        this.specification = specification;
    }
}
