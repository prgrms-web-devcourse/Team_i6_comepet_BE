package com.pet.domains.post.repository;

import com.pet.domains.post.domain.MissingPost;

public interface MissingPostWithIsBookmark {

    MissingPost getMissingPost();

    boolean getIsBookmark();

}
