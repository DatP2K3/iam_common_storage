package com.evotek.storage.infrastructure.support;

import java.util.UUID;

public class IdUtils {
    public static UUID nextId() {
        return UUID.randomUUID();
    }
}
