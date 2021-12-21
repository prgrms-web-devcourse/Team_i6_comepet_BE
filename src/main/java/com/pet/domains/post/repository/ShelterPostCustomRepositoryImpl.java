package com.pet.domains.post.repository;

import static com.pet.domains.animal.domain.QAnimal.animal;
import static com.pet.domains.animal.domain.QAnimalKind.animalKind;
import static com.pet.domains.area.domain.QCity.city;
import static com.pet.domains.area.domain.QTown.town;
import static com.pet.domains.post.domain.QShelterPost.shelterPost;
import static com.pet.domains.post.domain.QShelterPostBookmark.shelterPostBookmark;
import com.pet.domains.account.domain.Account;
import com.pet.domains.post.domain.SexType;
import com.pet.domains.post.domain.ShelterPost;
import com.pet.domains.post.dto.serach.PostSearchParam;
import com.pet.domains.post.repository.projection.QShelterPostWithFetch;
import com.pet.domains.post.repository.projection.QShelterPostWithIsBookmark;
import com.pet.domains.post.repository.projection.ShelterPostWithFetch;
import com.pet.domains.post.repository.projection.ShelterPostWithIsBookmark;
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
public class ShelterPostCustomRepositoryImpl extends QuerydslRepositorySupport implements ShelterPostCustomRepository {

    private static final String START_FILTER_FIELD_NAME = "foundDate";

    private static final String IS_BOOK_MARK_AS = "isBookmark";

    private final JPAQueryFactory jpaQueryFactory;

    public ShelterPostCustomRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(ShelterPost.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public Page<ShelterPost> findAllWithFetch(Pageable pageable, PostSearchParam postSearchParam) {
        boolean hasStartFilter = getHasStartFilter(postSearchParam.getStart());
        JPAQuery<ShelterPost> query = jpaQueryFactory.select(shelterPost)
            .from(shelterPost)
            .innerJoin(shelterPost.animalKind, animalKind).fetchJoin()
            .innerJoin(animalKind.animal, animal).fetchJoin()
            .innerJoin(shelterPost.town, town).fetchJoin()
            .innerJoin(town.city, city).fetchJoin()
            .where(
                eqTown(postSearchParam.getTown()),
                eqCity(postSearchParam.getCity()),
                eqAnimal(postSearchParam.getAnimal()),
                eqAnimalKind(postSearchParam.getAnimalKind()),
                eqSexType(postSearchParam.getSex()),
                goeFoundDate(postSearchParam.getStart()),
                loeFoundDate(postSearchParam.getEnd()));
        QueryResults<ShelterPost> queryResults = Objects.requireNonNull(getQuerydsl())
            .applyPagination(getPageable(pageable, hasStartFilter), query)
            .fetchResults();

        return new PageImpl<>(queryResults.getResults(), pageable, queryResults.getTotal());
    }

    @Override
    public Page<ShelterPostWithFetch> findAllByAccountBookmarkWithFetch(Account account, Pageable pageable) {
        JPAQuery<ShelterPostWithFetch> query = getShelterPostByAccountBookmarkWithFetchQuery(account);
        QueryResults<ShelterPostWithFetch> queryResults = Objects.requireNonNull(getQuerydsl())
            .applyPagination(pageable, query)
            .fetchResults();

        return new PageImpl<>(queryResults.getResults(), pageable, queryResults.getTotal());
    }

    @Override
    public Page<ShelterPostWithIsBookmark> findAllWithIsBookmark(Account account, Pageable pageable,
        PostSearchParam postSearchParam) {
        JPAQuery<ShelterPostWithIsBookmark> query = getShelterPostWithIsBookmarkQuery(account)
            .where(
                eqTown(postSearchParam.getTown()),
                eqCity(postSearchParam.getCity()),
                eqAnimal(postSearchParam.getAnimal()),
                eqAnimalKind(postSearchParam.getAnimalKind()),
                eqSexType(postSearchParam.getSex()),
                goeFoundDate(postSearchParam.getStart()),
                loeFoundDate(postSearchParam.getEnd()));
        QueryResults<ShelterPostWithIsBookmark>
            queryResults = Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query)
            .fetchResults();

        return new PageImpl<>(queryResults.getResults(), pageable, queryResults.getTotal());
    }

    @Override
    public Optional<ShelterPostWithIsBookmark> findByIdWithIsBookmark(Account account, Long postId) {
        ShelterPostWithIsBookmark result = getShelterPostWithIsBookmarkQuery(account)
            .where(shelterPost.id.eq(postId))
            .fetchOne();

        return Optional.ofNullable(result);
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

    private JPAQuery<ShelterPostWithFetch> getShelterPostByAccountBookmarkWithFetchQuery(Account account) {
        return jpaQueryFactory.select(
            new QShelterPostWithFetch(
                shelterPost,
                animal,
                animalKind,
                city,
                town))
            .from(shelterPost)
            .innerJoin(shelterPost.animalKind, animalKind)
            .innerJoin(shelterPost.animalKind.animal, animal)
            .innerJoin(shelterPost.town, town)
            .innerJoin(shelterPost.town.city, city)
            .innerJoin(shelterPostBookmark)
            .on(shelterPost.id.eq(shelterPostBookmark.shelterPost.id)
                .and(shelterPostBookmark.account.id.eq(account.getId())));
    }

    private JPAQuery<ShelterPostWithIsBookmark> getShelterPostWithIsBookmarkQuery(Account account) {
        return jpaQueryFactory.select(
            new QShelterPostWithIsBookmark(
                shelterPost,
                animal,
                animalKind,
                city,
                town,
                ExpressionUtils.as(shelterPostBookmark.id.isNotNull(), IS_BOOK_MARK_AS)))
            .from(shelterPost)
            .innerJoin(shelterPost.animalKind, animalKind)
            .innerJoin(shelterPost.animalKind.animal, animal)
            .innerJoin(shelterPost.town, town)
            .innerJoin(shelterPost.town.city, city)
            .leftJoin(shelterPostBookmark)
            .on(shelterPost.id.eq(shelterPostBookmark.shelterPost.id)
                .and(shelterPostBookmark.account.id.eq(account.getId())));
    }

    private BooleanExpression eqTown(Long townId) {
        if (Objects.isNull(townId)) {
            return null;
        }
        return shelterPost.town.id.eq(townId);
    }

    private BooleanExpression eqCity(Long cityId) {
        if (Objects.isNull(cityId)) {
            return null;
        }
        return shelterPost.town.city.id.eq(cityId);
    }

    private BooleanExpression eqAnimalKind(Long animalKindId) {
        if (Objects.isNull(animalKindId)) {
            return null;
        }
        return shelterPost.animalKind.id.eq(animalKindId);
    }

    private BooleanExpression eqAnimal(Long animalId) {
        if (Objects.isNull(animalId)) {
            return null;
        }
        return shelterPost.animalKind.animal.id.eq(animalId);
    }

    private BooleanExpression eqSexType(SexType sexType) {
        if (Objects.isNull(sexType)) {
            return null;
        }
        return shelterPost.sex.eq(sexType);
    }

    private BooleanExpression goeFoundDate(LocalDate start) {
        if (Objects.isNull(start)) {
            return null;
        }
        return shelterPost.foundDate.goe(start);
    }

    private BooleanExpression loeFoundDate(LocalDate end) {
        if (Objects.isNull(end)) {
            return null;
        }
        return shelterPost.foundDate.loe(end);
    }
}
