package com.pet.domains.post.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import com.pet.common.exception.ExceptionMessage;
import com.pet.common.exception.httpexception.NotFoundException;
import com.pet.domains.account.domain.Account;
import com.pet.domains.animal.domain.Animal;
import com.pet.domains.animal.domain.AnimalKind;
import com.pet.domains.area.domain.City;
import com.pet.domains.area.domain.Town;
import com.pet.domains.post.domain.ShelterPost;
import com.pet.domains.post.domain.ShelterPostBookmark;
import com.pet.domains.post.repository.ShelterPostBookmarkRepository;
import com.pet.domains.post.repository.ShelterPostRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("보호소 게시글 북마크 서비스 테스트")
class ShelterPostBookmarkServiceTest {

    @Mock
    private ShelterPostBookmarkRepository shelterPostBookmarkRepository;

    @Mock
    private ShelterPostRepository shelterPostRepository;

    @InjectMocks
    private ShelterPostBookmarkService shelterPostBookmarkService;

    @Test
    @DisplayName("사용자의 보호소 게시글 북마크 등록 성공 테스트")
    void createPostBookmarkSuccessTest() {
        // given
        ShelterPost spyShelterPost = spy(
            ShelterPost.builder()
                .animalKind(AnimalKind.builder()
                    .animal(Animal.builder()
                        .code("1111")
                        .name("animal")
                        .build())
                    .name("animalKind")
                    .build())
                .town(Town.builder()
                    .city(City.builder()
                        .code("11111")
                        .name("city")
                        .build())
                    .name("town")
                    .build())
                .bookmarkCount(10)
                .build());
        given(shelterPostRepository.findById(anyLong())).willReturn(Optional.of(spyShelterPost));

        // when
        shelterPostBookmarkService.createPostBookmark(1L, mock(Account.class));

        // then
        ArgumentCaptor<ShelterPostBookmark> captor = ArgumentCaptor.forClass(ShelterPostBookmark.class);
        verify(shelterPostBookmarkRepository, times(1)).save(captor.capture());
        verify(spyShelterPost).increaseBookMarkCount();
        assertThat(spyShelterPost.getBookmarkCount()).isEqualTo(11);
    }

    @Test
    @DisplayName("사용자의 보호소 게시글 북마크 등록 실패 테스트 - 게시글 정보 찾을 수 없음")
    void createPostBookmarkFailureTest() {
        // given
        RuntimeException exception = ExceptionMessage.NOT_FOUND_SHELTER_POST.getException();
        willThrow(exception)
            .given(shelterPostRepository).findById(anyLong());

        // when, then
        assertThatThrownBy(() -> shelterPostBookmarkService.createPostBookmark(1L, mock(Account.class)))
            .isInstanceOf(NotFoundException.class)
            .hasMessageContaining(exception.getMessage());
        verify(shelterPostBookmarkRepository, never()).save(any(ShelterPostBookmark.class));
    }

    @Test
    @DisplayName("사용자의 보호소 게시글 북마크 삭제 성공 테스트")
    void deletePostBookmarkSuccessTest() {
        // given
        ShelterPost spyShelterPost = spy(
            ShelterPost.builder()
                .animalKind(AnimalKind.builder()
                    .animal(Animal.builder()
                        .code("1111")
                        .name("animal")
                        .build())
                    .name("animalKind")
                    .build())
                .town(Town.builder()
                    .city(City.builder()
                        .code("11111")
                        .name("city")
                        .build())
                    .name("town")
                    .build())
                .bookmarkCount(10)
                .build());
        given(shelterPostRepository.findById(anyLong())).willReturn(Optional.of(spyShelterPost));
        given(shelterPostBookmarkRepository.deleteByShelterPostIdAndAccount(anyLong(), any(Account.class))).willReturn(1L);

        // when
        shelterPostBookmarkService.deletePostBookmark(1L, mock(Account.class));

        // then
        verify(shelterPostBookmarkRepository, times(1)).deleteByShelterPostIdAndAccount(anyLong(), any(Account.class));
        verify(spyShelterPost).decreaseBookMarkCount();
        assertThat(spyShelterPost.getBookmarkCount()).isEqualTo(9);

    }

}
