package com.pe.advanced.api.dto.category.assignment;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pe.advanced.api.dto.AbstractDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

/**
 * Every outcome in one batch.
 * <p>
 * A batch assignment answers 200 even when some products were left alone, which is why the
 * outcomes are the response rather than an error. Partial success is the normal case here,
 * not an exception, and a status code cannot describe it: one request, two hundred answers.
 */
@Schema(description = "The outcomes of a batch assignment.")
public class ProductAssignmentResultsDto extends AbstractDto {

    @Schema(description = "One entry per product in the request, in the order it was sent.")
    @JsonProperty(value = "results")
    private List<ProductAssignmentResultDto> results = new ArrayList<>();

    public ProductAssignmentResultsDto() {
        // POJO
    }

    public ProductAssignmentResultsDto(List<ProductAssignmentResultDto> results) {
        this.results = results;
    }

    public List<ProductAssignmentResultDto> getResults() {
        return results;
    }

    public void setResults(List<ProductAssignmentResultDto> results) {
        this.results = results;
    }
}
