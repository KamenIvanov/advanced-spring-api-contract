package com.pe.advanced.api.rest.secured.product;

import com.pe.advanced.api.dto.product.*;
import com.pe.advanced.api.rest.RestUrl;
import com.pe.advanced.api.rest.search.ProductSearchParametersDto;
import com.pe.advanced.api.rest.secured.CrudRestService;
import com.pe.advanced.api.rest.secured.SecuredRestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.pe.advanced.api.rest.HeaderConstants.IF_MATCH;

/**
 * A product as its owner works with it: the four CRUD operations on the full representation,
 * plus the one thing that is not a field.
 */
@Tag(name = "Products", description = "Managing one's own products.")
@RequestMapping(value = RestUrl.SECURED_PRODUCTS, produces = MediaType.APPLICATION_JSON_VALUE)
public interface ProductsRestService extends CrudRestService<NewProductDto, UpdateProductDto, ProductDto>, SecuredRestService {

    @Operation(summary = "Loads the caller's own products in pages.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "The requested page of products."),
            @ApiResponse(responseCode = "400", description = "Invalid paging or sorting arguments.", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "401", description = "The caller did not identify itself.", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping
    ProductsDto getOwnProducts(@ModelAttribute ProductSearchParametersDto parameters);

    /**
     * Moves a product through its lifecycle.
     * <p>
     * It is an operation rather than a field on the update type because not every value is
     * reachable from every other one: an archived product does not become a draft again. A
     * settable {@code status} would describe the column; this describes the transition, and
     * an illegal one is a 409 rather than a 400 - the request was well formed, the product
     * was simply not in a state it could leave.
     */
    @Parameter(name = IF_MATCH, in = ParameterIn.HEADER, required = true, description = "The version of the product the caller read, as returned in its 'version' field.", schema = @Schema(type = "integer", format = "int32"))
    @Operation(summary = "Moves the product to another status.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "The product in its new status."),
            @ApiResponse(
                    responseCode = "401",
                    description = "The caller did not identify itself.",
                    content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "The caller does not own this product.",
                    content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No product with the supplied identification.",
                    content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "The product cannot move from its current status to the requested one, or it was changed by somebody else since it was read.",
                    content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @PostMapping(RestUrl.PRODUCT_STATUS)
    ProductDto changeStatus(
            @Parameter(description = "The product's identification.")
            @PathVariable("id") UUID id,

            @Parameter(description = "The status the product should move to.")
            @RequestParam("status") ProductStatusDto status
    );
}
