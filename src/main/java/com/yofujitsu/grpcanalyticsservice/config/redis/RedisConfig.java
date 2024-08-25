package com.yofujitsu.grpcanalyticsservice.config.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;


    /**
     * Creates and configures a JedisPool bean.
     * The JedisPool bean is used by the application to connect to the Redis server.
     *
     * @return the configured JedisPool bean
     */
    @Bean
    public JedisPool jedisPool() {
        // Create a new JedisPoolConfig
        JedisPoolConfig config = new JedisPoolConfig();
        // Disable JMX monitoring
        config.setJmxEnabled(false);
        // Return a new JedisPool instance configured with the specified host and port
        return new JedisPool(config, host, port);
    }
}
