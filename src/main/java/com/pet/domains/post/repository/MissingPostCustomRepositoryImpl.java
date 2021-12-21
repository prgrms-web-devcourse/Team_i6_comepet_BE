package com.pet.domains.post.repository;

import static com.pet.domains.animal.domain.QAnimal.animal;
import static com.pet.domains.animal.domain.QAnimalKind.animalKind;
import static com.pet.domains.area.domain.QCity.city;
import static com.pet.domains.area.domain.QTown.town;
import static com.pet.domains.post.domain.QMissingPost.missingPost;
import static com.pet.domains.post.domain.QMissingPostBookmark.missingPostBookmark;
import com.pet.domains.account.domain.Account;
import com.pet.domains.post.domain.MissingPost;
import com.pet.domains.post.domain.SexType;
import com.pet.domains.post.domain.Status;
import com.pet.domains.post.dto.serach.PostSearchParam;
import com.pet.domains.post.repository.projection.MissingPostWithFetch;
import com.pet.domains.post.repository.projection.MissingPostWithIsBookmark;
import com.pet.domains.post.repository.projection.QMissingPostWithFetch;
import com.pet.domains.post.repository.projection.QMissingPostWithIsBookmark;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Repository
public class MissingPostCustomRepositoryImpl extends QuerydslRepositorySupport implements MissingPostCustomRepository {

    private static final String START_FILTER_FIELD_NAME = "createdAt";

    private static final String IS_BOOK_MARK_AS = "isBookmark";

    private final JPAQueryFactory jpaQueryFactory;

    public MissingPostCustomRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(MissingPost.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Page<MissingPost> findMissingPostAllWithFetch(Pageable pageable, PostSearchParam postSearchParam) {
        boolean hasStartFilter = getHasStartFilter(postSearchParam.getStart());
        JPAQuery<MissingPost> query = jpaQueryFactory.select(missingPost)
            .from(missingPost)
            .innerJoin(missingPost.animalKind, animalKind).fetchJoin()
            .innerJoin(animalKind.animal, animal).fetchJoin()
            .innerJoin(missingPost.town, town).fetchJoin()
            .innerJoin(town.city, city).fetchJoin()
            .where(
                eqStatus(postSearchParam.getStatus()),
                eqTown(postSearchParam.getTown()),
                eqCity(postSearchParam.getCity()),
                eqAnimal(postSearchParam.getAnimal()),
                eqAnimalKind(postSearchParam.getAnimalKind()),
                eqSexType(postSearchParam.getSex()),
                goeCreateAt(postSearchParam.getStart()),
                loeCreateAt(postSearchParam.getEnd()));
        QueryResults<MissingPost> queryResults =
            Objects.requireNonNull(getQuerydsl())
                .applyPagination(getPageable(pageable, hasStartFilter), query)
                .fetchResults();

        return new PageImpl<>(queryResults.getResults(), pageable, queryResults.getTotal());
    }

    @Override
    public Page<MissingPostWithFetch> findMissingPostAllByAccountBookmarkWithFetch(Account account, Pageable pageable) {
        JPAQuery<MissingPostWithFetch> query = getMissingPostByAccountBookmarkWithFetchQuery(account);
        QueryResults<MissingPostWithFetch> queryResults =
            Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query)
                .fetchResults();

        return new PageImpl<>(queryResults.getResults(), pageable, queryResults.getTotal());
    }

    @Override
    public Page<MissingPostWithIsBookmark> findMissingPostAllWithIsBookmark(Account account,
        Pageable pageable, PostSearchParam postSearchParam) {
        JPAQuery<MissingPostWithIsBookmark> query = getMissingPostWithIsBookmarkQuery(account)
            .where(
                eqStatus(postSearchParam.getStatus()),
                eqTown(postSearchParam.getTown()),
                eqCity(postSearchParam.getCity()),
                eqAnimal(postSearchParam.getAnimal()),
                eqAnimalKind(postSearchParam.getAnimalKind()),
                eqSexType(postSearchParam.getSex()),
                goeCreateAt(postSearchParam.getStart()),
                loeCreateAt(postSearchParam.getEnd()));
        QueryResults<MissingPostWithIsBookmark> queryResults = Objects.requireNonNull(getQuerydsl())
            .applyPagination(pageable, query)
            .fetchResults();

        return new PageImpl<>(queryResults.getResults(), pageable, queryResults.getTotal());
    }


    @Override
    public Optional<MissingPostWithIsBookmark> findMissingPostByIdWithIsBookmark(Account account, Long postId) {
        MissingPostWithIsBookmark result = getMissingPostWithIsBookmarkQuery(account)
            .where(missingPost.id.eq(postId))
            .fetchOne();

        return Optional.ofNullable(result);
    }

    private JPAQuery<MissingPostWithFetch> getMissingPostByAccountBookmarkWithFetchQuery(Account account) {
        return jpaQueryFactory.select(
            new QMissingPostWithFetch(
                missingPost,
                animal,
                animalKind,
                city,
                town))
            .from(missingPost)
            .innerJoin(missingPost.animalKind, animalKind)
            .innerJoin(missingPost.animalKind.animal, animal)
            .innerJoin(missingPost.town, town)
            .innerJoin(missingPost.town.city, city)
            .innerJoin(missingPostBookmark)
            .on(missingPost.id.eq(missingPostBookmark.missingPost.id)
                .and(missingPostBookmark.account.id.eq(account.getId())));
    }

    private JPAQuery<MissingPostWithIsBookmark> getMissingPostWithIsBookmarkQuery(Account account) {
        return jpaQueryFactory.select(
            new QMissingPostWithIsBookmark(
                missingPost,
                animal,
                animalKind,
                city,
                town,
                ExpressionUtils.as(missingPostBookmark.id.isNotNull(), IS_BOOK_MARK_AS)))
            .from(missingPost)
            .innerJoin(missingPost.animalKind, animalKind)
            .innerJoin(missingPost.animalKind.animal, animal)
            .innerJoin(missingPost.town, town)
            .innerJoin(missingPost.town.city, city)
            .leftJoin(missingPostBookmark)
            .on(missingPost.id.eq(missingPostBookmark.missingPost.id)
                .and(missingPostBookmark.account.id.eq(account.getId())));
    }

    private boolean getHasStartFilter(LocalDate start) {
        return !Objects.isNull(start);
    }

    private Pageable getPageable(Pageable pageable, boolean hasStartFilter) {
        if (hasStartFilter) {
            return PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(START_FILTER_FIELD_NAME).ascending()
            );
        }
        return pageable;
    }

    private BooleanExpression eqStatus(Status status) {
        if (Objects.isNull(status)) {
            return null;
        }
        return missingPost.status.eq(status);
    }

    private BooleanExpression eqTown(Long townId) {
        if (Objects.isNull(townId)) {
            return null;
        }
        return missingPost.town.id.eq(townId);
    }

    private BooleanExpression eqCity(Long cityId) {
        if (Objects.isNull(cityId)) {
            return null;
        }
        return missingPost.town.city.id.eq(cityId);
    }

    private BooleanExpression eqAnimalKind(Long animalKindId) {
        if (Objects.isNull(animalKindId)) {
            return null;
        }
        return missingPost.animalKind.id.eq(animalKindId);
    }

    private BooleanExpression eqAnimal(Long animalId) {
        if (Objects.isNull(animalId)) {
            return null;
        }
        return missingPost.animalKind.animal.id.eq(animalId);
    }

    private BooleanExpression eqSexType(SexType sexType) {
        if (Objects.isNull(sexType)) {
            return null;
        }
        return missingPost.sexType.eq(sexType);
    }

    private BooleanExpression goeCreateAt(LocalDate start) {
        if (Objects.isNull(start)) {
            return null;
        }
        return missingPost.createdAt.goe(start.atStartOfDay());
    }

    private BooleanExpression loeCreateAt(LocalDate end) {
        if (Objects.isNull(end)) {
            return null;
        }
        return missingPost.createdAt.loe(end.atTime(23, 59, 59));
    }
}
