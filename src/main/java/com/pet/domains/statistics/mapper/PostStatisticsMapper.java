package com.pet.domains.statistics.mapper;

import com.pet.domains.post.domain.Status;
import com.pet.domains.statistics.domain.PostStatistics;
import com.pet.domains.statistics.dto.response.PostStatisticsReadResult;
import com.pet.domains.statistics.repository.PostCountByStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PostStatisticsMapper {

    default PostStatistics toEntity(List<PostCountByStatus> postCountByStatuses, LocalDateTime now) {
        Map<Status, Long> statusCountMap = postCountByStatuses.stream()
            .collect(Collectors.toMap(PostCountByStatus::getPostStatus, PostCountByStatus::getCount));

        return PostStatistics.builder()
            .missing(statusCountMap.getOrDefault(Status.MISSING, 0L))
            .protection(statusCountMap.getOrDefault(Status.PROTECTION, 0L))
            .detection(statusCountMap.getOrDefault(Status.DETECTION, 0L))
            .completion(statusCountMap.getOrDefault(Status.COMPLETION, 0L))
            .date(now)
            .build();
    }

    PostStatisticsReadResult toReadResult(PostStatistics postStatisticsEntity);
}
