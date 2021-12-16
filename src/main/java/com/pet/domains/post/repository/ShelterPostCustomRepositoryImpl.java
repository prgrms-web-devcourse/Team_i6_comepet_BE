package com.pet.domains.post.repository;

import static com.pet.domains.post.domain.QShelterPost.shelterPost;
import static com.pet.domains.post.domain.QShelterPostBookmark.shelterPostBookmark;
import com.pet.domains.account.domain.Account;
import com.pet.domains.post.repository.projection.QShelterPostWithIsBookmark;
import com.pet.domains.post.repository.projection.ShelterPostWithIsBookmark;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ShelterPostCustomRepositoryImpl implements ShelterPostCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<ShelterPostWithIsBookmark> findAllWithIsBookmark(Account account, Pageable pageable) {
        QueryResults<ShelterPostWithIsBookmark> result = jpaQueryFactory.select(
            new QShelterPostWithIsBookmark(
                shelterPost,
                ExpressionUtils.as(shelterPostBookmark.id.isNotNull(), "isBookmark")))
            .from(shelterPost)
            .leftJoin(shelterPostBookmark)
            .on(shelterPost.id.eq(shelterPostBookmark.shelterPost.id)
                .and(shelterPostBookmark.account.id.eq(account.getId())))
            .limit(pageable.getPageSize())
            .offset(pageable.getPageNumber())
            .fetchResults();
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    @Override
    public Optional<ShelterPostWithIsBookmark> findByIdWithIsBookmark(Account account, Long postId) {
        ShelterPostWithIsBookmark result = jpaQueryFactory.select(new QShelterPostWithIsBookmark(
            shelterPost,
            ExpressionUtils.as(shelterPostBookmark.id.isNotNull(), "isBookmark")))
            .from(shelterPost)
            .leftJoin(shelterPostBookmark)
            .on(shelterPost.id.eq(shelterPostBookmark.shelterPost.id)
                .and(shelterPostBookmark.account.id.eq(account.getId())))
            .where(shelterPost.id.eq(postId))
            .fetchOne();
        return Optional.ofNullable(result);
    }
}
