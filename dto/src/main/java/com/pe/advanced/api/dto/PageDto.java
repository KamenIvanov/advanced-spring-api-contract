package com.pe.advanced.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * What a page knows about the result it came from, without the items themselves. Concrete
 * pages add a typed list rather than a generic one, so that the generated specification
 * names the element type instead of describing an array of objects.
 */
@Schema(description = "Holds the pagination data.")
public abstract class PageDto extends AbstractDto {

    @Schema(description = "The total number of pages which can be fetched using the criteria that obtained this page.")
    @JsonProperty(value = "totalPages")
    private Integer totalPages;

    @Schema(description = "The total number of items which can be fetched using the criteria that obtained this page.")
    @JsonProperty(value = "totalHits")
    private Long totalHits;

    protected PageDto() {
        // POJO
    }

    protected PageDto(Integer totalPages, Long totalHits) {
        this.totalPages = totalPages;
        this.totalHits = totalHits;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Long getTotalHits() {
        return totalHits;
    }

    public void setTotalHits(Long totalHits) {
        this.totalHits = totalHits;
    }
}
