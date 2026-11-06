package com.pe.advanced.api.openapi;

import com.fasterxml.jackson.databind.JsonNode;
import com.pe.advanced.api.rest.HeaderConstants;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.Yaml;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Runs after the documentation server has stopped, against the files the build produced.
 * <p>
 * A generated document is not self-evidently correct. It is produced by a tool reading
 * annotations, and a tool reading annotations is exactly the thing that quietly produces
 * something plausible when one is missing. Every assertion here is a claim this chapter makes
 * about the published contract.
 */
class OpenApiDocumentationIT {

    private static Path output;
    private static JsonNode spec;

    @BeforeAll
    static void loadGeneratedSpecification() throws Exception {
        output = Path.of(System.getProperty("documentation.directory"));
        spec = Json.mapper().readTree(output.resolve("openapi.json").toFile());
    }

    @Test
    void publishesMatchingFormatsWithTheApiVersionAndAPortableServerUrl() throws Exception {
        assertEquals(spec, Yaml.mapper().readTree(output.resolve("openapi.yaml").toFile()));
        assertEquals(System.getProperty("documentation.version"), spec.at("/info/version").asText());
        assertEquals("/", spec.at("/servers/0/url").asText(),
                "The build server's random port must not reach the published document");
    }

    /**
     * Every operation reached the document. The number of them is deliberately not asserted: a
     * count has to be updated by hand on every API change, which teaches everybody to update it
     * without reading it.
     */
    @Test
    void documentsBothProductResources() {
        final var paths = spec.path("paths");
        assertTrue(paths.path("/v1/products").has("get"));
        assertTrue(paths.path("/v1/products/{id}").has("get"));
        assertTrue(paths.path("/v1/secured/products").has("get"));
        assertTrue(paths.path("/v1/secured/products").has("post"));
        assertTrue(paths.path("/v1/secured/products/{id}").has("get"));
        assertTrue(paths.path("/v1/secured/products/{id}").has("put"));
        assertTrue(paths.path("/v1/secured/products/{id}").has("delete"));
        assertTrue(paths.path("/v1/secured/products/{id}/status").has("post"));
    }

    /** A contract without @Tag appears as "<name>-rest-service-controller": a class nobody wrote. */
    @Test
    void groupsEveryOperationUnderADeclaredTag() {
        spec.path("paths").forEach(path -> path.forEach(operation -> operation.path("tags").forEach(tag ->
                assertFalse(tag.asText().endsWith("-controller"), "Missing @Tag: " + tag.asText()))));
    }

    @Test
    void hasUniqueOperationIds() {
        final var ids = new HashSet<String>();
        spec.path("paths").forEach(path -> path.forEach(operation -> {
            final var id = operation.path("operationId").asText();
            assertFalse(id.isBlank());
            assertTrue(ids.add(id), "Duplicate operationId: " + id);
        }));
        assertFalse(ids.isEmpty());
    }

    /**
     * The two projections stayed two. If the generator ever collapsed them into one schema, the
     * published contract would have stopped describing the API.
     */
    @Test
    void keepsThePublicAndTheOwnersProjectionApart() {
        assertEquals("#/components/schemas/ProductsDescriptionsDto", responseSchema("/v1/products", "get"));
        assertEquals("#/components/schemas/ProductDescriptionDto", responseSchema("/v1/products/{id}", "get"));
        assertEquals("#/components/schemas/ProductsDto", responseSchema("/v1/secured/products", "get"));
        assertEquals("#/components/schemas/ProductDto", responseSchema("/v1/secured/products/{id}", "get"));

        final var publicProjection = spec.at("/components/schemas/ProductDescriptionDto/properties");
        assertFalse(publicProjection.has("version"), "An anonymous reader cannot use a concurrency token");
        assertFalse(publicProjection.has("updatedAt"));
        assertTrue(spec.at("/components/schemas/ProductDto/properties").has("version"));
    }

    /**
     * The generic CrudRestService resolved to real types rather than to Object. This is the thing
     * most likely to break quietly when the interface hierarchy changes.
     */
    @Test
    void resolvesTheInheritedCrudRequestAndResponseTypes() {
        final var create = spec.path("paths").path("/v1/secured/products").path("post");
        assertEquals("#/components/schemas/NewProductDto",
                create.at("/requestBody/content/application~1json/schema/$ref").asText());
        assertTrue(create.path("requestBody").path("required").asBoolean());
        assertEquals("#/components/schemas/ProductDto", responseSchema("/v1/secured/products", "post"));

        final var update = spec.path("paths").path("/v1/secured/products/{id}").path("put");
        assertEquals("#/components/schemas/UpdateProductDto",
                update.at("/requestBody/content/application~1json/schema/$ref").asText());
    }

    /** Errors are problem+json, which is what the service actually returns. */
    @Test
    void describesFailuresAsProblemDetail() {
        final var responses = spec.path("paths").path("/v1/secured/products/{id}").path("put").path("responses");
        for (var status : Set.of("400", "401", "403", "404", "409")) {
            assertTrue(responses.has(status), "Missing response " + status);
            assertEquals("#/components/schemas/ProblemDetail",
                    responses.path(status).at("/content/application~1problem+json/schema/$ref").asText(), status);
        }
        assertTrue(responses.path("409").path("description").asText().contains("changed by somebody else"));
    }

    /**
     * Where a parameter lives and whether it is required were never written down twice: the Spring
     * bindings say it and the Swagger annotations only describe it. This checks the generator read
     * the bindings rather than guessing.
     */
    @Test
    void infersParameterLocationsAndRequirednessFromTheSpringBindings() {
        final var update = spec.path("paths").path("/v1/secured/products/{id}").path("put");

        final var id = parameter(update, "id");
        assertEquals("path", id.path("in").asText());
        assertTrue(id.path("required").asBoolean());

        final var requester = parameter(update, HeaderConstants.REQUESTER_ID);
        assertEquals("header", requester.path("in").asText());
        assertTrue(requester.path("required").asBoolean());

        final var ifMatch = parameter(update, HeaderConstants.IF_MATCH);
        assertEquals("header", ifMatch.path("in").asText());
        assertTrue(ifMatch.path("required").asBoolean());

        final var catalogue = spec.path("paths").path("/v1/products").path("get");
        assertTrue(parameter(catalogue, "page").path("required").asBoolean());
        assertFalse(parameter(catalogue, "query").path("required").asBoolean(),
                "An optional filter must not be documented as required");
    }

    /**
     * Money is a string on the wire, because the mapper in rest-api writes BigDecimal with
     * ToStringSerializer so that no client parses a price into a double. The specification has to
     * say so, and this is the check that it does.
     * <p>
     * The rule names no field. A genuine floating point value carries a format, while a decimal
     * that reached the schema unconverted is a "number" with no format at all, so the invariant is
     * that a formatless number does not exist. That catches the next decimal field somebody adds
     * without anyone having to remember this rule.
     */
    @Test
    void describesEveryDecimalAsAStringRatherThanANumber() {
        assertEquals("string", spec.at("/components/schemas/ProductDto/properties/price/type").asText());
        assertFormatlessNumbersAbsent(spec.path("components").path("schemas"), "components.schemas");
    }

    @Test
    void preparesSwaggerUiAroundTheGeneratedSpecification() throws Exception {
        for (var file : Set.of("index.html", "swagger-ui.css", "swagger-ui-bundle.js",
                "swagger-ui-standalone-preset.js", "swagger-initializer.js", "openapi.json", "openapi.yaml")) {
            assertTrue(Files.isRegularFile(output.resolve(file)), "Missing Swagger UI file: " + file);
        }
        final var initializer = Files.readString(output.resolve("swagger-initializer.js"));
        assertTrue(initializer.contains("url: \"./openapi.json\""));
        assertFalse(initializer.contains("petstore"), "The webjar's demo specification must be replaced");
    }

    private static String responseSchema(String path, String method) {
        return spec.path("paths").path(path).path(method)
                .at("/responses/200/content/application~1json/schema/$ref").asText();
    }

    private static JsonNode parameter(JsonNode operation, String name) {
        for (var parameter : operation.path("parameters")) {
            if (name.equals(parameter.path("name").asText())) {
                return parameter;
            }
        }
        return fail("Missing parameter: " + name);
    }

    private static void assertFormatlessNumbersAbsent(JsonNode node, String path) {
        if (node.isObject()) {
            if ("number".equals(node.path("type").asText()) && !node.has("format")) {
                fail("A number with no format is a decimal that was not converted to a string: " + path);
            }
            node.properties().forEach(field ->
                    assertFormatlessNumbersAbsent(field.getValue(), path + "." + field.getKey()));
        } else if (node.isArray()) {
            for (var index = 0; index < node.size(); index++) {
                assertFormatlessNumbersAbsent(node.get(index), path + "[" + index + "]");
            }
        }
    }
}
