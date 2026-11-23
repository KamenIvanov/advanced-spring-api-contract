package com.pe.advanced.api.rest;

/**
 * The headers this API reads, named here for the same reason the paths are: a header spelled
 * twice is a header that will be spelled differently once.
 */
public final class HeaderConstants {

    /**
     * Who is asking. The service treats a request without it as unauthenticated rather than
     * as malformed, which is why it is declared required on every secured operation.
     */
    public static final String REQUESTER_ID = "X-Requester-Id";

    /**
     * The version of the entity the caller believes it is changing, carried in the standard
     * conditional-request header.
     * <p>
     * It is a header rather than a field in the body because it is a precondition on the
     * request, not part of the thing being sent: the body says what the product should become,
     * and this says which product state the caller was looking at when it decided that. A
     * stale value means somebody else changed the row first, and the answer is 409 rather
     * than a silent overwrite.
     */
    public static final String IF_MATCH = "If-Match";

    private HeaderConstants() {
        // Constants
    }
}
