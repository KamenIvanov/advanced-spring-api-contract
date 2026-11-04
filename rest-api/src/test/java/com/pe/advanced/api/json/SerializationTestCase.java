package com.pe.advanced.api.json;

import com.pe.advanced.api.dto.product.ProductDto;
import com.pe.advanced.api.dto.product.ProductStatusDto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * These tests are the executable half of the contract. The specification can say that a price
 * is a string and an instant is a date-time; only this configuration decides what is actually
 * written, and only these assertions prove the two agree.
 */
class SerializationTestCase {

    private static ProductDto product() {
        final var product = new ProductDto();
        product.setId(UUID.fromString("01a19b12-7a59-70bc-8fd3-b4826a1b0605"));
        product.setName("Walnut cutting board");
        product.setSku("WCB-001");
        product.setStatus(ProductStatusDto.ACTIVE);
        product.setVersion(3);
        return product;
    }

    @Test
    void writesMoneyAsAStringSoThatNoClientParsesItAsADouble() {
        final var product = product();
        product.setPrice(new BigDecimal("19.90"));

        final var json = ApiSerializer.instance.process(product);

        assertTrue(json.contains("\"price\":\"19.90\""), json);
        assertFalse(json.contains("\"price\":19.9"), json);
    }

    @Test
    void keepsTheScaleTheServerDecidedOn() {
        final var product = product();
        product.setPrice(new BigDecimal("20.00"));

        assertTrue(ApiSerializer.instance.process(product).contains("\"price\":\"20.00\""));
    }

    @Test
    void writesVerySmallAmountsWithoutLosingThem() {
        final var product = product();
        product.setPrice(new BigDecimal("0.0000000001"));

        final var json = ApiSerializer.instance.process(product);

        assertTrue(json.contains("\"price\":\"0.0000000001\"") || json.contains("\"price\":\"1E-10\""), json);
    }

    /**
     * The case the default format gets wrong: a whole second. ISO-8601 drops the fractional
     * part when it is zero, so without a fixed format this instant and the next one in this
     * class would serialize to different shapes.
     */
    @Test
    void writesAWholeSecondWithMillisecondsAndAZuluSuffix() {
        final var product = product();
        product.setCreatedAt(Instant.parse("2026-11-03T14:15:00Z"));

        assertTrue(ApiSerializer.instance.process(product).contains("\"createdAt\":\"2026-11-03T14:15:00.000Z\""));
    }

    @Test
    void writesMillisecondsAsThreeDigits() {
        final var product = product();
        product.setCreatedAt(Instant.parse("2026-11-03T14:15:00.123Z"));

        assertTrue(ApiSerializer.instance.process(product).contains("\"createdAt\":\"2026-11-03T14:15:00.123Z\""));
    }

    @Test
    void truncatesRatherThanRoundsBelowMilliseconds() {
        final var product = product();
        product.setCreatedAt(Instant.parse("2026-11-03T14:15:00.123456789Z"));

        assertTrue(ApiSerializer.instance.process(product).contains("\"createdAt\":\"2026-11-03T14:15:00.123Z\""));
    }

    @Test
    void writesEveryInstantInUtcWhateverTheDefaultZoneIs() {
        final var product = product();
        product.setCreatedAt(Instant.parse("2026-11-03T23:45:00.000Z"));

        final var json = ApiSerializer.instance.process(product);

        assertTrue(json.contains("\"createdAt\":\"2026-11-03T23:45:00.000Z\""), json);
        assertFalse(json.contains("+02:00"), json);
    }

    @Test
    void acceptsAnyIsoInstantOnTheWayInIncludingAnotherOffset() {
        final var utc = ApiDeserializer.instance.process("{\"createdAt\":\"2026-11-03T14:15:00.000Z\"}", ProductDto.class);
        final var offset = ApiDeserializer.instance.process("{\"createdAt\":\"2026-11-03T16:15:00+02:00\"}", ProductDto.class);
        final var wholeSecond = ApiDeserializer.instance.process("{\"createdAt\":\"2026-11-03T14:15:00Z\"}", ProductDto.class);

        assertEquals(Instant.parse("2026-11-03T14:15:00Z"), utc.getCreatedAt());
        assertEquals(utc.getCreatedAt(), offset.getCreatedAt());
        assertEquals(utc.getCreatedAt(), wholeSecond.getCreatedAt());
    }

    @Test
    void rejectsSomethingThatIsNotAnInstant() {
        assertThrows(Exception.class, () -> ApiDeserializer.instance.process("{\"createdAt\":\"03/11/2026\"}", ProductDto.class));
    }

    @Test
    void readsMoneyBackFromTheStringItWrote() {
        final var product = product();
        product.setPrice(new BigDecimal("19.90"));
        product.setCreatedAt(Instant.parse("2026-11-03T14:15:00.000Z"));

        final var json = ApiSerializer.instance.process(product);
        final var read = ApiDeserializer.instance.process(json, ProductDto.class);

        assertEquals(new BigDecimal("19.90"), read.getPrice());
        assertEquals(product.getCreatedAt(), read.getCreatedAt());
        assertEquals(product.getVersion(), read.getVersion());
        assertEquals(ProductStatusDto.ACTIVE, read.getStatus());
    }

    /**
     * No Java class name travels to a client. The package structure of this project is not
     * part of the published contract, and a payload that carried it would be documenting
     * something the specification never mentions.
     */
    @Test
    void writesNoTypeInformation() {
        final var json = ApiSerializer.instance.process(product());

        assertFalse(json.contains("@class"), json);
        assertFalse(json.contains("com.pe.advanced"), json);
    }

    @Test
    void escapesTheLineTerminatorsThatAreValidJsonAndBreakJavaScript() {
        final var product = product();
        product.setName("Walnut cutting board");

        final var json = ApiSerializer.instance.process(product);

        assertFalse(json.contains(" "), json);
        assertFalse(json.contains(" "), json);
        assertTrue(json.contains("\\u2028"), json);
        assertTrue(json.contains("\\u2029"), json);
    }

    @Test
    void writesNoIndentationSoThatResponsesCarryNoBytesNobodyReads() {
        assertFalse(ApiSerializer.instance.process(product()).contains("\n"));
    }

    @Test
    void acceptsAPayloadWithFieldsThisVersionDoesNotKnow() {
        final var read = ApiDeserializer.instance.process("{\"name\":\"Walnut cutting board\",\"somethingAddedLater\":true}", ProductDto.class);

        assertNotNull(read);
        assertEquals("Walnut cutting board", read.getName());
    }

    @Test
    void treatsNothingAsNothing() {
        assertNull(ApiSerializer.instance.process(null));
        assertNull(ApiDeserializer.instance.process(null, ProductDto.class));
        assertNull(ApiDeserializer.instance.process("   ", ProductDto.class));
    }
}
