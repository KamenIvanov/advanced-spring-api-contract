package com.pe.advanced.api.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pe.advanced.api.dto.AbstractDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

/**
 * What a client sends to change a product. It is not {@link ProductDto} with some fields
 * ignored: a type whose fields are sometimes read and sometimes written has to document which
 * is which in prose, and prose is not something a client can check.
 * <p>
 * The status is absent on purpose. A status change is a transition with rules, so it is an
 * operation rather than an assignment.
 */
@Schema(description = "The data required to update a product.")
public class UpdateProductDto extends AbstractDto {

    @Schema(description = "The product's name.", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty(value = "name")
    private String name;

    @Schema(description = "The product's stock keeping unit. Unique across all products.", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty(value = "sku")
    private String sku;

    @Schema(description = "The product's price, with two decimal places.", example = "19.99", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty(value = "price")
    private BigDecimal price;

    @Schema(description = "The product's dimensions, as width x height x depth in millimetres.", example = "120x80x40")
    @JsonProperty(value = "dimensions")
    private String dimensions;

    @Schema(description = "The product's weight in grams.")
    @JsonProperty(value = "weight")
    private Integer weight;

    public UpdateProductDto() {
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

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }
}
