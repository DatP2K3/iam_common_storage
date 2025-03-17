package com.evotek.iam.infrastructure.support;

import java.util.UUID;

public final class IdUtils {
    private IdUtils() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    public static UUID nextId() {
        return UUID.randomUUID();
    }
}
