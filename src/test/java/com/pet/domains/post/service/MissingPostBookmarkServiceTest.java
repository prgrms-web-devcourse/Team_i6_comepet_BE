package com.pet.domains.post.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import com.pet.domains.account.domain.Account;
import com.pet.domains.post.domain.MissingPost;
import com.pet.domains.post.domain.MissingPostBookmark;
import com.pet.domains.post.repository.MissingPostBookmarkRepository;
import com.pet.domains.post.repository.MissingPostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("실종/보호 게시물 북마크 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class MissingPostBookmarkServiceTest {

    @InjectMocks
    private MissingPostBookmarkService missingPostBookmarkService;

    @Mock
    private MissingPostBookmarkRepository missingPostBookmarkRepository;

    @Mock
    private MissingPostRepository missingPostRepository;

    @Test
    @DisplayName("실종/보호 게시물 북마크 생성 테스트")
    void createMissingPostBookmarkTest() {
        //given
        given(missingPostRepository.findById(anyLong())).willReturn(
            java.util.Optional.ofNullable(mock(MissingPost.class)));
        given(missingPostBookmarkRepository.save(any())).willReturn(
            mock(MissingPostBookmark.class));

        //when
        missingPostRepository.findById(1L);
        missingPostBookmarkRepository.save(mock(MissingPostBookmark.class));

        //then
        verify(missingPostRepository, times(1)).findById(anyLong());
        verify(missingPostBookmarkRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("실종/보호 게시물 북마크 삭제 테스트")
    void deleteMissingPostBookmarkTest() {
        //given
        doNothing().when(missingPostBookmarkRepository).deleteByMissingPostIdAndAccount(anyLong(), any());

        //when
        missingPostBookmarkRepository.deleteByMissingPostIdAndAccount(1L, mock(Account.class));

        //then
        verify(missingPostBookmarkRepository, times(1)).deleteByMissingPostIdAndAccount(anyLong(), any());
    }

}
