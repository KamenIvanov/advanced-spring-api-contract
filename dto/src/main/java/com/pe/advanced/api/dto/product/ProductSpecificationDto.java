package com.pe.advanced.api.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pe.advanced.api.dto.AbstractEntityDto;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * The physical measurements of a product, as a thing with its own identity and its own
 * version rather than two more fields on the product. A product may exist before anyone has
 * measured it, so this is absent rather than empty when it is not known.
 */
@Schema(description = "The product's physical specification.")
public class ProductSpecificationDto extends AbstractEntityDto {

    @Schema(description = "The product's dimensions, as width x height x depth in millimetres.", example = "120x80x40", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty(value = "dimensions")
    private String dimensions;

    @Schema(description = "The product's weight in grams.", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty(value = "weight")
    private Integer weight;

    public ProductSpecificationDto() {
        // POJO
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
