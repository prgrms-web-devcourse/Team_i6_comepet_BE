package com.pet.domains.statistics.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "post_statistics")
public class PostStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "missing", columnDefinition = "BIGINT", nullable = false)
    private long missing;

    @Column(name = "detection", columnDefinition = "BIGINT", nullable = false)
    private long detection;

    @Column(name = "protection", columnDefinition = "BIGINT", nullable = false)
    private long protection;

    @Column(name = "completion", columnDefinition = "BIGINT", nullable = false)
    private long completion;

    @Column(name = "date", columnDefinition = "DATE", nullable = false)
    private LocalDateTime date;

}
