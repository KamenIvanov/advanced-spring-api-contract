package com.pe.advanced.api.rest.pub.product;

import com.pe.advanced.api.dto.product.ProductDescriptionDto;
import com.pe.advanced.api.dto.product.ProductsDescriptionsDto;
import com.pe.advanced.api.rest.RestUrl;
import com.pe.advanced.api.rest.search.ProductSearchParametersDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

/**
 * The catalogue, as anyone may read it.
 * <p>
 * It returns {@link ProductDescriptionDto} rather than the full product, and that is the
 * reason this interface exists separately rather than as the same operations without a
 * session. Two audiences want two different representations of one thing, and the honest way
 * to say so is two resources: this one is cacheable by anybody, the other is not, and a
 * specification can describe each of them exactly. One path whose response changes according
 * to who asked cannot be described at all, and is a hazard the first shared cache discovers.
 */
@Tag(name = "Products (public)", description = "Reading the catalogue, without a session.")
@RequestMapping(value = RestUrl.PRODUCTS, produces = MediaType.APPLICATION_JSON_VALUE)
public interface ProductsFilteringRestService {

    /**
     * Loads a page of the catalogue. Every filter is optional and they combine with AND.
     */
    @Operation(summary = "Loads products in pages, filtered and sorted.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "The requested page of products."),
            @ApiResponse(responseCode = "400", description = "Invalid paging or sorting arguments.", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping
    ProductsDescriptionsDto getProducts(@ModelAttribute ProductSearchParametersDto parameters);

    /**
     * Loads one product. The description projection, not the full product: an anonymous
     * reader has no use for an audit trail or a concurrency token and should not be handed
     * either.
     */
    @Operation(summary = "Loads the product associated with the supplied identification.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "The requested product."),
            @ApiResponse(responseCode = "400", description = "The supplied identification is malformed.", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "No product with the supplied identification.", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping(RestUrl.BY_ID)
    ProductDescriptionDto getProduct(
            @Parameter(description = "The requested product's identification.")
            @PathVariable("id") UUID id
    );
}
