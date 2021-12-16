package com.pet.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@Slf4j
public class OptimisticLockingHandlingUtils {

    private OptimisticLockingHandlingUtils() {
        throw new AssertionError("유틸 클래스입니다.");
    }

    public static void handling(Runnable runnable, int handlingCount, String description) {
        for (int i = 0; i < handlingCount; i++) {
            try {
                runnable.run();
                break;
            } catch (ObjectOptimisticLockingFailureException e) {
                log.warn("#{}: locking failure occurred when try {}", handlingCount, description);
            }
        }
    }
}
