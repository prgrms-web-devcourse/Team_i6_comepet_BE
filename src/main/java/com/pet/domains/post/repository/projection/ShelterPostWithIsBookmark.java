package com.pet.domains.post.repository.projection;


import com.pet.domains.post.domain.ShelterPost;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ShelterPostWithIsBookmark {

    private ShelterPost shelterPost;

    private boolean isBookmark;

    @QueryProjection
    public ShelterPostWithIsBookmark(ShelterPost shelterPost, boolean isBookmark) {
        this.shelterPost = shelterPost;
        this.isBookmark = isBookmark;
    }
}
