package com.pet.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.pet.domains.animal.domain.AnimalKind;
import java.time.Duration;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@EnableCaching
@RequiredArgsConstructor
@Configuration
public class RedisCachingConfig {

    private final RedisConnectionFactory redisConnectionFactory;

    private final ObjectMapper objectMapper;

    @Bean
    public CacheManager redisCacheManager() {
        CollectionType collectionType = objectMapper.getTypeFactory()
            .constructCollectionType(ArrayList.class, AnimalKind.class);

        RedisCacheConfiguration redisCachingConfiguration = RedisCacheConfiguration
            .defaultCacheConfig()
            .serializeKeysWith(
                RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer())
            )
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    new Jackson2JsonRedisSerializer<>(collectionType)
                )
            )
            .entryTtl(Duration.ofHours(1));

        return RedisCacheManager
            .RedisCacheManagerBuilder
            .fromConnectionFactory(redisConnectionFactory)
            .cacheDefaults(redisCachingConfiguration)
            .build();
    }
}
