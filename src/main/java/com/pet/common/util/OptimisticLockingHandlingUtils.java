package com.pet.common.util;

import com.pet.common.exception.ExceptionMessage;
import java.util.Optional;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@Slf4j
public class OptimisticLockingHandlingUtils {

    private OptimisticLockingHandlingUtils() {
        throw new AssertionError("유틸 클래스입니다.");
    }

    public static void handling(Runnable runnable, int handlingCount, String description) {
        boolean isFailure = true;
        for (int i = 0; i < handlingCount; i++) {
            try {
                runnable.run();
                isFailure = false;
                break;
            } catch (ObjectOptimisticLockingFailureException ex) {
                log.warn("#{}: locking failure occurred when try {}", i, description);
            }
        }
        if (isFailure) {
            throw ExceptionMessage.SERVICE_UNAVAILABLE.getException();
        }
    }

    public static <T> Optional<T> handling(Supplier<T> supplier, int handlingCount, String description) {
        for (int i = 0; i < handlingCount; i++) {
            try {
                return Optional.ofNullable(supplier.get());
            } catch (ObjectOptimisticLockingFailureException ex) {
                log.warn("#{}: locking failure occurred when try {}", i, description);
            }
        }
        return Optional.empty();
    }
}
