package com.pe.advanced.api.dto.category.assignment;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pe.advanced.api.dto.AbstractCreatableEntityDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

/**
 * One product placed in one category.
 * <p>
 * It extends the creatable base and nothing more, because an assignment is never updated: it
 * exists or it does not. There is no version here for the same reason there is no version
 * column on its table - the concept arrives with the operation that needs it, and nothing
 * updates this.
 */
@Schema(description = "A product's assignment to a category.")
public class CategoryAssignmentDto extends AbstractCreatableEntityDto {

    @Schema(description = "The assigned product's identification.")
    @JsonProperty(value = "productId")
    private UUID productId;

    @Schema(description = "The category the product was assigned to.")
    @JsonProperty(value = "categoryId")
    private UUID categoryId;

    @Schema(description = "Who made the assignment.")
    @JsonProperty(value = "assignedById")
    private UUID assignedById;

    public CategoryAssignmentDto() {
        // POJO
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(UUID categoryId) {
        this.categoryId = categoryId;
    }

    public UUID getAssignedById() {
        return assignedById;
    }

    public void setAssignedById(UUID assignedById) {
        this.assignedById = assignedById;
    }
}
