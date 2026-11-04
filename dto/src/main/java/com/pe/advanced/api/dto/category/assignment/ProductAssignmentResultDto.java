package com.pe.advanced.api.dto.category.assignment;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pe.advanced.api.dto.AbstractDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

/**
 * The outcome for one product in a batch assignment.
 * <p>
 * In the service layer this is a sealed interface with one record per outcome, which is the
 * right shape in Java: the compiler checks that every case is handled and each case carries
 * exactly the data it needs. It is the wrong shape on the wire. A closed hierarchy becomes
 * {@code oneOf} with a discriminator, which means a type field in every payload - the thing
 * this contract deliberately removed - and client generators turn it into a union most
 * callers then cast their way out of.
 * <p>
 * So the wire version is flat: who, what happened, and the identifier of the assignment when
 * one was made. The sealed interface stays in the service, and translating between them is
 * the transformer's job, which is the same division every other type here follows.
 */
@Schema(description = "The outcome of assigning one product to a category.")
public class ProductAssignmentResultDto extends AbstractDto {

    @Schema(description = "The product this outcome is about.")
    @JsonProperty(value = "productId")
    private UUID productId;

    @Schema(description = "What happened to it.")
    @JsonProperty(value = "outcome")
    private ProductAssignmentOutcomeDto outcome;

    @Schema(description = "The assignment that was created. Present only when the outcome is ASSIGNED.")
    @JsonProperty(value = "assignmentId")
    private UUID assignmentId;

    public ProductAssignmentResultDto() {
        // POJO
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public ProductAssignmentOutcomeDto getOutcome() {
        return outcome;
    }

    public void setOutcome(ProductAssignmentOutcomeDto outcome) {
        this.outcome = outcome;
    }

    public UUID getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(UUID assignmentId) {
        this.assignmentId = assignmentId;
    }
}
