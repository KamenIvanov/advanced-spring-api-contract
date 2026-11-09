package com.pe.advanced.api.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pe.advanced.api.dto.AbstractCreatableEntityDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

/**
 * A product as an anonymous reader sees it: enough to list it, price it and decide whether to
 * open it, and nothing else.
 * <p>
 */
@Schema(description = "A product, as shown to anyone browsing the catalogue.")
public class ProductDescriptionDto extends AbstractCreatableEntityDto {

    @Schema(description = "The product's name.")
    @JsonProperty(value = "name")
    private String name;

    @Schema(description = "The product's stock keeping unit.")
    @JsonProperty(value = "sku")
    private String sku;

    @Schema(description = "The product's price, as a string so that no client loses precision parsing it.", example = "19.99")
    @JsonProperty(value = "price")
    private BigDecimal price;

    @Schema(description = "The product's current status. Only an ACTIVE product can be ordered.")
    @JsonProperty(value = "status")
    private ProductStatusDto status;

    public ProductDescriptionDto() {
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
}
