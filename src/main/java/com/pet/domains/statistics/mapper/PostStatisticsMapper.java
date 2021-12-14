package com.pet.domains.statistics.mapper;

import com.pet.domains.post.domain.Status;
import com.pet.domains.statistics.domain.PostStatistics;
import com.pet.domains.statistics.repository.PostCountByStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PostStatisticsMapper {

    default PostStatistics toEntity(List<PostCountByStatus> postCountByStatuses, LocalDateTime now) {
        Map<Status, Long> statusLongMap = postCountByStatuses.stream()
            .collect(Collectors.toMap(PostCountByStatus::getPostStatus, PostCountByStatus::getCount));

        return PostStatistics.builder()
            .missing(statusLongMap.getOrDefault(Status.MISSING, 0L))
            .protection(statusLongMap.getOrDefault(Status.PROTECTION, 0L))
            .detection(statusLongMap.getOrDefault(Status.DETECTION, 0L))
            .completion(statusLongMap.getOrDefault(Status.COMPLETION, 0L))
            .date(now)
            .build();
    }

}
