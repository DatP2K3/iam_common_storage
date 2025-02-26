package com.evotek.iam.infrastructure.support;

import java.util.UUID;

public class IdUtils {
    public static UUID nextId() {
        return UUID.randomUUID();
    }
}
