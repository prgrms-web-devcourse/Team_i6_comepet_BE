package com.pet.domains.statistics.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import com.pet.domains.statistics.domain.PostStatistics;
import com.pet.domains.statistics.mapper.PostStatisticsMapper;
import com.pet.domains.statistics.repository.PostCountByStatus;
import com.pet.domains.statistics.repository.PostStatisticsRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("통계 서비스 테스트")
class PostStatisticsServicesTest {

    @Mock
    private PostStatisticsRepository postStatisticsRepository;

    @Mock
    private PostStatisticsMapper postStatisticsMapper;

    @InjectMocks
    private PostStatisticsService postStatisticsService;

    @Test
    @DisplayName("게시글 통계 성공 테스트")
    void createPostStatisticsSuccess() {
        // given
        PostStatistics postStatistics = PostStatistics.builder()
            .missing(1L)
            .completion(2L)
            .detection(3L)
            .protection(4L)
            .date(LocalDateTime.now())
            .build();

        given(postStatisticsRepository.findGroupByStatus()).willReturn(List.of(mock(PostCountByStatus.class)));
        given(postStatisticsMapper.toEntity(anyList(), any(LocalDateTime.class))).willReturn(postStatistics);
        given(postStatisticsRepository.save(any(PostStatistics.class))).willReturn(postStatistics);

        // when
        postStatisticsService.createPostStatistics();

        // then
        ArgumentCaptor<PostStatistics> captor = ArgumentCaptor.forClass(PostStatistics.class);
        verify(postStatisticsRepository, times(1)).findGroupByStatus();
        verify(postStatisticsRepository, times(1)).save(captor.capture());
        assertThat(captor.getValue().getCompletion()).isEqualTo(postStatistics.getCompletion());
    }

}
