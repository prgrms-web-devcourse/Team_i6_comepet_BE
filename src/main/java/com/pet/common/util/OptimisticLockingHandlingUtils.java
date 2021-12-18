package com.pet.common.util;

import com.pet.common.exception.ExceptionMessage;
import java.util.function.Supplier;
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

    public static <T> T handling2(Supplier<T> supplier, T defaultValue, int handlingCount, String description) {
        T result = defaultValue;
        for (int i = 0; i < handlingCount; i++) {
            try {
                result = supplier.get();
                break;
            } catch (ObjectOptimisticLockingFailureException ex) {
                log.warn("#{}: locking failure occurred when try {}", i, description);
            }
        }
        return result;
    }

    public static void handling2(Runnable runnable, int handlingCount, String description) {
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
}
