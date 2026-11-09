package com.pe.advanced.api.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pe.advanced.api.dto.AbstractDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

/**
 * What a client sends to create a product, which is a different type from what it gets back.
 * There is no id, no timestamps and no version here, because none of them are a caller's to
 * supply - and no status, because a product starts as a draft by a rule the server owns.
 * <p>
 * The measurements are flat rather than a nested specification. The specification is a thing
 * with a life of its own once it exists, but it does not exist yet, and asking a caller to
 * post an object with an empty id to create one would be describing the storage rather than
 * the request.
 */
@Schema(description = "The data required to create a product.")
public class NewProductDto extends AbstractDto {

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

    public NewProductDto() {
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
