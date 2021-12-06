package com.pet.domains.statistics.dto.response;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class PostStatisticsReadResult {

    private final Long missing;

    private final Long detection;

    private final Long protection;

    private final Long completion;

    private final LocalDateTime date;

    private PostStatisticsReadResult(Long missing, Long detection, Long protection, Long completion,
        LocalDateTime date) {
        this.missing = missing;
        this.detection = detection;
        this.protection = protection;
        this.completion = completion;
        this.date = date;
    }

    public static PostStatisticsReadResult of(Long missing, Long detection, Long protection, Long completion,
        LocalDateTime date) {
        return new PostStatisticsReadResult(missing, detection, protection, completion, date);
    }
}
