package com.pet.domains.statistics.repository;

import com.pet.domains.post.domain.Status;

public interface PostCountByStatus {

    Status getPostStatus();

    long getCount();

}
