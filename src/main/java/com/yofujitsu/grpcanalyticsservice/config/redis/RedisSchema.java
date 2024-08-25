package com.yofujitsu.grpcanalyticsservice.config.redis;

import com.yofujitsu.grpcanalyticsservice.entity.data.ValueType;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisSchema {

    //set
    public static String heroKeys() {
        return KeyHelper.getKey("heroes");
    }

    /**
     * Constructs and returns the Redis key for the given hero ID and value type.
     * The key is of the format "summary:{heroId}:{valueType}".
     *
     * @param heroId    the ID of the hero
     * @param valueType the type of the value
     * @return the Redis key for the given hero ID and value type
     */
    public static String summaryKey(long heroId, ValueType valueType) {
        // Construct the Redis key for the given hero ID and value type
        return KeyHelper.getKey("summary:" + heroId + ":" + valueType.name().toLowerCase());
    }
}
