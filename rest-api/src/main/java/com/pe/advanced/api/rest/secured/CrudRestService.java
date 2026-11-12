package com.pe.advanced.api.rest.secured;

import com.pe.advanced.api.dto.AbstractDto;
import com.pe.advanced.api.dto.AbstractEntityDto;
import com.pe.advanced.api.rest.HeaderConstants;
import com.pe.advanced.api.rest.RestUrl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.UUID;

/**
 * The four operations every owned entity has, declared once.
 * <p>
 * The type parameters are what make this worth having: three types rather than one, because
 * creating, updating and reading are three different shapes. A caller does not supply an
 * identifier or a version when creating, supplies a version but not an identifier when
 * updating, and receives both when reading. Collapsing them into one type would mean
 * documenting in prose which field applies when, and prose is not something a generated
 * client can check.
 * <p>
 * The mappings are here rather than on the implementations, which is what the
 * {@code @RequestMapping} javadoc asks for when an interface carries them: all of them on the
 * interface, none split across the class. It is also what lets a specification be generated
 * from this module without a running service.
 *
 * @param <NewDto>    what a caller sends to create one
 * @param <UpdateDto> what a caller sends to change one
 * @param <Dto>       what the service returns
 */
public interface CrudRestService<NewDto extends AbstractDto, UpdateDto extends AbstractDto, Dto extends AbstractEntityDto> {

    @Operation(summary = "Creates a new entity.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "The newly created entity."),
            @ApiResponse(responseCode = "400", description = "Invalid data is supplied.", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "401", description = "The caller did not identify itself.", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "409", description = "An entity with the same unique data already exists.", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    Dto create(
            @Parameter(description = "The entity's data.")
            @RequestBody NewDto newDto,

            @Parameter(description = "The caller's identification.")
            @RequestHeader(HeaderConstants.REQUESTER_ID) UUID requesterId
    );

    /**
     * Updates an entity, if nobody has changed it since the caller read it.
     * <p>
     * The 409 is the one response worth reading twice. It does not mean the request was wrong;
     * it means the state moved underneath it, and retrying after a fresh read is a reasonable
     * thing for a caller to do. Returning 500 for it - which is what happens until the
     * handler learns about the optimistic lock - tells the caller the server is broken, and a
     * caller believing that does not retry.
     */
    @Operation(summary = "Updates an existing entity.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "The updated entity."),
            @ApiResponse(responseCode = "400", description = "Invalid data is supplied.", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "401", description = "The caller did not identify itself.", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "403", description = "The caller does not own this entity.", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "No entity with the supplied identification.", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "409", description = "The entity was changed by somebody else since it was read. Read it again and retry.", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PutMapping(value = RestUrl.BY_ID, consumes = MediaType.APPLICATION_JSON_VALUE)
    Dto update(
            @Parameter(description = "The identification of the entity being updated.")
            @PathVariable("id") UUID id,

            @Parameter(description = "The entity's new data.")
            @RequestBody UpdateDto updateDto,

            @Parameter(description = "The version of the entity the caller read, as returned in its 'version' field.")
            @RequestHeader(HeaderConstants.IF_MATCH) Integer expectedVersion,

            @Parameter(description = "The caller's identification.")
            @RequestHeader(HeaderConstants.REQUESTER_ID) UUID requesterId
    );

    @Operation(summary = "Loads the entity associated with the supplied identification.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "The requested entity."),
            @ApiResponse(responseCode = "401", description = "The caller did not identify itself.", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "403", description = "The caller does not own this entity.", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "No entity with the supplied identification.", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping(RestUrl.BY_ID)
    Dto loadById(
            @Parameter(description = "The requested entity's identification.")
            @PathVariable("id") UUID id,

            @Parameter(description = "The caller's identification.")
            @RequestHeader(HeaderConstants.REQUESTER_ID) UUID requesterId
    );

    /**
     * Deletes an entity. Deleting something that is not there is not an error, because the
     * caller asked for a state and that state already holds.
     */
    @Operation(summary = "Deletes the entity associated with the supplied identification.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "The entity no longer exists."),
            @ApiResponse(responseCode = "401", description = "The caller did not identify itself.", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "403", description = "The caller does not own this entity.", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "409", description = "The entity was changed by somebody else since it was read.", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class)))
    })
    @DeleteMapping(RestUrl.BY_ID)
    void delete(
            @Parameter(description = "The identification of the entity being deleted.")
            @PathVariable("id") UUID id,

            @Parameter(description = "The caller's identification.")
            @RequestHeader(HeaderConstants.REQUESTER_ID) UUID requesterId
    );
}
