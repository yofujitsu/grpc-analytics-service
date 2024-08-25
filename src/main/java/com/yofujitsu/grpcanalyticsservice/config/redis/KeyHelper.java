package com.yofujitsu.grpcanalyticsservice.config.redis;

import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class KeyHelper {

    private final static String defaultPrefix = "app";

    private static String prefix = null;

    public static void setPrefix(String prefix) {
        KeyHelper.prefix = prefix;
    }

    /**
     * Returns the prefix used for Redis keys. If the prefix has not been set,
     * it defaults to the default prefix.
     *
     * @return the prefix used for Redis keys
     */
    public static String getPrefix() {
        // If the prefix has been set, use that. If not, use the default prefix
        return Objects.requireNonNullElse(prefix, defaultPrefix);
    }

    /**
     * Returns the full Redis key by concatenating the prefix and the key provided.
     * If the prefix has not been set, it defaults to the default prefix.
     *
     * @param key the key to concatenate with the prefix
     * @return the full Redis key
     */
    public static String getKey(String key) {
        // Construct the full key by concatenating the prefix and the provided key
        return getPrefix() + ":" + key;
    }
}
