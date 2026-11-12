package com.pe.advanced.api.rest.search;

import com.pe.advanced.api.dto.SortDirectionDto;
import com.pe.advanced.api.dto.product.ProductSortOptionDto;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Everything that narrows a product listing, as one argument.
 * <p>
 * It is bound with {@code @ModelAttribute}, so Spring fills it from the query string and the
 * generated document flattens it back into separate query parameters. A client sees exactly what
 * it saw when these were five parameters on the method; what changes is the signature of
 * everything that has to carry them - the interface, the controller, and the service behind it.
 * <p>
 * It lives in {@code rest-api} rather than in {@code dto} because it is never serialized. It is a
 * description of a query string, which is a fact about the HTTP surface rather than a shape that
 * travels in a body.
 * <p>
 * The defaults are here rather than in the caller. A listing with no arguments is a reasonable
 * request, and answering it with a 400 teaches clients to send boilerplate.
 */
public class ProductSearchParametersDto {

    @Parameter(description = "The requested page index. The first page is 1.", schema = @Schema(defaultValue = "1"))
    private Integer page = 1;

    @Parameter(description = "The page's size. The minimum is 1 element and the maximum is 100.", schema = @Schema(defaultValue = "20"))
    private Integer size = 20;

    @Parameter(description = "The field the page is sorted by.", schema = @Schema(defaultValue = "CREATED_AT"))
    private ProductSortOptionDto sort = ProductSortOptionDto.CREATED_AT;

    @Parameter(description = "The direction the page is sorted in.", schema = @Schema(defaultValue = "DESC"))
    private SortDirectionDto direction = SortDirectionDto.DESC;

    @Parameter(description = "Text contained in the product's name, case insensitive. Everything matches when it is absent.")
    private String query;

    public ProductSearchParametersDto() {
        // Bound from the query string
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public ProductSortOptionDto getSort() {
        return sort;
    }

    public void setSort(ProductSortOptionDto sort) {
        this.sort = sort;
    }

    public SortDirectionDto getDirection() {
        return direction;
    }

    public void setDirection(SortDirectionDto direction) {
        this.direction = direction;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
