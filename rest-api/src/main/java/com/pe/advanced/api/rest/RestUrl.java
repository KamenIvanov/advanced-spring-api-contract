package com.pe.advanced.api.rest;

/**
 * Every path this API answers on, in one place.
 * <p>
 * The constants exist so that a path is written once and read by everything that needs it:
 * the interface that declares the operation, the specification generated from it, and the
 * service that implements it. A path typed twice is a path that will differ once.
 * <p>
 * The {@code /secured} prefix is a convention for the reader, not a security mechanism. What
 * actually decides access is the rule in the service, and that rule denies by default and
 * lists what is public - because a prefix that grants access by its absence means a constant
 * in the wrong place silently publishes an endpoint, with nothing failing to say so.
 */
public final class RestUrl {

    public static final String APP_VERSION = "/v1";

    /**
     * Everything a caller may reach without identifying itself. These are the paths the
     * security rule names explicitly.
     */
    public static final String PRODUCTS = APP_VERSION + "/products";

    /**
     * Everything that requires a caller we can name. The prefix makes that visible to a
     * reader and to whatever sits in front of the service; it does not enforce it.
     */
    public static final String SECURED = APP_VERSION + "/secured";

    public static final String SECURED_PRODUCTS = SECURED + "/products";

    /** Appended to a product path: the operation that moves it through its lifecycle. */
    public static final String PRODUCT_STATUS = "/{id}/status";

    /** Appended to a product path: the single product, by its identifier. */
    public static final String BY_ID = "/{id}";

    private RestUrl() {
        // Constants
    }
}
