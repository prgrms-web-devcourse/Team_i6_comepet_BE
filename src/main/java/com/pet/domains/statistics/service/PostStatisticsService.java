package com.pet.domains.statistics.service;

import com.pet.domains.statistics.domain.PostStatistics;
import com.pet.domains.statistics.mapper.PostStatisticsMapper;
import com.pet.domains.statistics.repository.PostCountByStatus;
import com.pet.domains.statistics.repository.PostStatisticsRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class PostStatisticsService {

    private final PostStatisticsRepository postStatisticsRepository;

    private final PostStatisticsMapper postStatisticsMapper;

    @Scheduled(cron = "0 0 4 * * *")
    @Transactional
    public void createPostStatistics() {
        LocalDateTime now = LocalDateTime.now();
        List<PostCountByStatus> groupByStatus = postStatisticsRepository.findGroupByStatus();
        PostStatistics postStatistics = postStatisticsRepository.save(
            postStatisticsMapper.toEntity(groupByStatus, now));
        log.info("{} created at {}", postStatistics, postStatistics.getDate());
    }

}
