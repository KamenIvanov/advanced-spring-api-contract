package com.pe.advanced.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The root of every type on the wire.
 * <p>
 * Writing a Java class name into the payload would make this
 * project's package structure part of the published contract: renaming a package would
 * become a breaking change to an API that had not changed, and a consumer in another
 * language would have to carry a field it can do nothing with.
 * <p>
 * Ignoring unknown properties is the other half of the same thought, pointed the other way.
 * A consumer built against version 1 of this jar keeps working when version 2 adds a field,
 * which is what makes adding a field a non-breaking change rather than a negotiation.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class AbstractDto {

    protected AbstractDto() {
        // POJO
    }
}
