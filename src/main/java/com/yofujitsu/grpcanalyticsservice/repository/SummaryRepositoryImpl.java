package com.yofujitsu.grpcanalyticsservice.repository;

import com.yofujitsu.grpcanalyticsservice.config.redis.KeyHelper;
import com.yofujitsu.grpcanalyticsservice.config.redis.RedisSchema;
import com.yofujitsu.grpcanalyticsservice.entity.data.Data;
import com.yofujitsu.grpcanalyticsservice.entity.data.ValueType;
import com.yofujitsu.grpcanalyticsservice.entity.summary.Summary;
import com.yofujitsu.grpcanalyticsservice.entity.summary.SummaryType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Optional;
import java.util.Set;

@Repository
@RequiredArgsConstructor
@Slf4j
public class SummaryRepositoryImpl implements SummaryRepository {

    private final JedisPool jedisPool;

    @Override
    public Optional<Summary> get(Long heroId, Set<ValueType> valueTypes, Set<SummaryType> summaryTypes) {
        try (Jedis jedis = jedisPool.getResource()) {
            if(!jedis.sismember(RedisSchema.heroKeys(), String.valueOf(heroId))) {
                return Optional.empty();
            }
            if(valueTypes.isEmpty() && !summaryTypes.isEmpty()) {
                return getSummary(heroId, Set.of(ValueType.values()), summaryTypes, jedis);
            } else if(!valueTypes.isEmpty() && summaryTypes.isEmpty()) {
                return getSummary(heroId, valueTypes, Set.of(SummaryType.values()), jedis);
            } else {
                return getSummary(heroId, valueTypes, summaryTypes, jedis);
            }
        }
    }

    /**
     * Retrieves the summary for a given hero ID, value types, and summary types from Redis.
     *
     * @param heroId        the ID of the hero
     * @param valueTypes    the set of value types
     * @param summaryTypes  the set of summary types
     * @param jedis        the Jedis instance used to interact with Redis
     * @return              an optional containing the summary if found, or empty if not
     */
    private Optional<Summary> getSummary(Long heroId, Set<ValueType> valueTypes, Set<SummaryType> summaryTypes, Jedis jedis) {
        // Create a new Summary object and set the hero ID
        Summary summary = new Summary();
        summary.setHeroId(heroId);

        // Iterate over the value types and summary types
        for(ValueType valueType : valueTypes) {
            for(SummaryType summaryType : summaryTypes) {
                // Create a new SummaryEntry object and set the summary type
                Summary.SummaryEntry entry = new Summary.SummaryEntry();
                entry.setType(summaryType);

                // Retrieve the value from Redis and parse it as a double
                String value = jedis.hget(RedisSchema.summaryKey(heroId, valueType), summaryType.name().toLowerCase());
                if(value != null) {
                    entry.setValue(Double.parseDouble(value));
                }

                // Retrieve the counter from Redis and parse it as a long
                String counter = jedis.hget(RedisSchema.summaryKey(heroId, valueType), "counter");
                if(counter != null) {
                    entry.setCounter(Long.parseLong(counter));
                }

                // Add the entry to the summary
                summary.addValue(valueType, entry);
            }
        }

        // Return the summary as an optional
        return Optional.of(summary);
    }

    /**
     * This method handles the given Data object by updating the summary values in Redis.
     * It first checks if the hero ID is present in the hero keys set. If not, it adds the hero ID to the set.
     * Then it updates the minimum, maximum, and average summary values based on the given data object.
     *
     * @param data the Data object containing the hero ID, value type, and value
     */
    @Override
    public void handle(Data data) {
        // Get a Jedis resource from the Jedis pool
        try(Jedis jedis = jedisPool.getResource()) {
            // Check if the hero ID is present in the hero keys set
            if(!jedis.sismember(RedisSchema.heroKeys(), String.valueOf(data.getHeroId()))) {
                // If not, add the hero ID to the set
                jedis.sadd(RedisSchema.heroKeys(), String.valueOf(data.getHeroId()));
            }

            // Update the minimum summary value
            updateMinValue(data, jedis);

            // Update the maximum summary value
            updateMaxValue(data, jedis);

            // Update the sum and average summary values
            updateSumAndAvgValue(data, jedis);
        }
    }


    private void updateMinValue(Data data, Jedis jedis) {
        String key = RedisSchema.summaryKey(data.getHeroId(), data.getValueType());
        String value = jedis.hget(key, SummaryType.MIN.name().toLowerCase());
        if(value == null) {
            jedis.hset(key, SummaryType.MIN.name().toLowerCase(), String.valueOf(data.getValue()));
        } else if(Double.parseDouble(value) > data.getValue()) {
            jedis.hset(key, SummaryType.MIN.name().toLowerCase(), String.valueOf(data.getValue()));
        }
    }

    private void updateMaxValue(Data data, Jedis jedis) {
        String key = RedisSchema.summaryKey(data.getHeroId(), data.getValueType());
        String value = jedis.hget(key, SummaryType.MAX.name().toLowerCase());
        if(value == null) {
            jedis.hset(key, SummaryType.MAX.name().toLowerCase(), String.valueOf(data.getValue()));
        } else if(Double.parseDouble(value) < data.getValue()) {
            jedis.hset(key, SummaryType.MAX.name().toLowerCase(), String.valueOf(data.getValue()));
        }
    }

    private void updateSumAndAvgValue(Data data, Jedis jedis) {
        updateSumValue(data, jedis);
        String key = RedisSchema.summaryKey(data.getHeroId(), data.getValueType());
        String counter = jedis.hget(key, "counter");
        if(counter == null) {
            counter = String.valueOf(jedis.hset(key, "counter", String.valueOf(1)));
        } else {
            counter = String.valueOf(jedis.hincrBy(key, "counter", 1));
        }
        String sum = jedis.hget(key, SummaryType.SUM.name().toLowerCase());
        jedis.hset(key, SummaryType.AVG.name().toLowerCase(), String.valueOf(Double.parseDouble(sum) / Double.parseDouble(counter)));
    }

    private void updateSumValue(Data data, Jedis jedis) {
        String key = RedisSchema.summaryKey(data.getHeroId(), data.getValueType());
        String value = jedis.hget(key, SummaryType.SUM.name().toLowerCase());
        if(value == null) {
            jedis.hset(key, SummaryType.SUM.name().toLowerCase(), String.valueOf(data.getValue()));
        } else {
            jedis.hincrByFloat(key, SummaryType.SUM.name().toLowerCase(), data.getValue());
        }
    }
}
