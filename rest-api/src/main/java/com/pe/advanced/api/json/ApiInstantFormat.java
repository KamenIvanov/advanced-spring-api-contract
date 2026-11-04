package com.pe.advanced.api.json;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * Every instant on this API is written in UTC, to the millisecond, with a trailing {@code Z}:
 * {@code 2026-11-03T14:15:00.000Z}.
 * <p>
 * The format is fixed rather than left to the library because the default is not a format, it
 * is a family of them: ISO-8601 drops trailing zeroes, so a whole second serializes without a
 * fractional part and anything else serializes with three, six or nine digits depending on
 * the value. Every one of those is valid, and a consumer that compares strings, slices by
 * position or caches on the rendered value behaves differently depending on which it got.
 * Pinning it costs one line and removes a class of bug that only appears in production, on
 * the request that happened to land on a round second.
 */
public final class ApiInstantFormat {

    public static final String PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(PATTERN).withZone(ZoneOffset.UTC);

    private ApiInstantFormat() {
        // Constants
    }
}
