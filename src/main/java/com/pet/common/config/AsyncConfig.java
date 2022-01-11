package com.pet.common.config;

import com.querydsl.core.annotations.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@Config
public class AsyncConfig {

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);  // 쓰레드풀의 디폴드 갯수 (첫 요청이 들어온 순간 생성)
        executor.setMaxPoolSize(20);  // 큐가 꽉찰 경우에 max 만큼 늘린다.
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("my thread");
        executor.initialize();
        return executor;
    }

}
