package com.congpv.springboot_base_project.infrastructure.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableCaching
public class RedisCacheConfig {

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator
                .builder()
                .allowIfBaseType(Object.class)
                .allowIfSubType("src.main.java.com.congpv.springboot_base_project.shared.dto")
                .allowIfSubType("java.util")
                .allowIfSubType("java.lang")
                .build();
        objectMapper.activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.NON_FINAL_AND_ENUMS, JsonTypeInfo.As.WRAPPER_ARRAY);

        RedisSerializer<Object> customJsonSerializer = new RedisSerializer<>() {
            @Override
            public byte[] serialize(Object object) throws SerializationException {
                if (object == null) return new byte[0];
                try {
                    return objectMapper.writeValueAsBytes(object);
                } catch (Exception e) {
                    throw new SerializationException("Error serializing to JSON: " + e.getMessage(), e);
                }
            }

            @Override
            public Object deserialize(byte[] bytes) throws SerializationException {
                if (bytes == null || bytes.length == 0) return null;
                try {
                    return objectMapper.readValue(bytes, Object.class);
                } catch (Exception e) {
                    throw new SerializationException("Error deserializing from JSON: " + e.getMessage(), e);
                }
            }
        };

        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(60))
                .disableCachingNullValues()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(customJsonSerializer));

        Map<String, RedisCacheConfiguration> customConfigs = new HashMap<>();
        customConfigs.put("project_details", defaultConfig.entryTtl(Duration.ofMinutes(10)));
        customConfigs.put("projects_pagination", defaultConfig.entryTtl(Duration.ofMinutes(5)));

        customConfigs.put("user_detail_id", defaultConfig.entryTtl(Duration.ofDays(3)));
        customConfigs.put("user_detail_username", defaultConfig.entryTtl(Duration.ofDays(3)));
        customConfigs.put("users", defaultConfig.entryTtl(Duration.ofDays(3)));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(customConfigs)
                .build();
    }
}