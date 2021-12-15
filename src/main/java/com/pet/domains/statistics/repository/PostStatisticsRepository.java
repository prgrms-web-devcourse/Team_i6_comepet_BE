package com.pet.domains.statistics.repository;

import com.pet.domains.statistics.domain.PostStatistics;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostStatisticsRepository extends JpaRepository<PostStatistics, Long> {

    @Query("SELECT mp.status AS postStatus, count(mp) AS count FROM"
        + " MissingPost AS mp WHERE mp.deleted=false GROUP BY mp.status")
    List<PostCountByStatus> findGroupByStatus();

    Optional<PostStatistics> findFirstByOrderByIdDesc();

}
