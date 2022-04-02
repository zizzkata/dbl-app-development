package com.example.dbl_app_dev.util;

import android.util.Log;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Wrapper that simulates async/await functionality
 */
public class AsyncWrapper {

    /**
     * Async/await wrapper that throws an exception from the task.
     *
     * @param task executable task
     * @param <U>  return type of task
     * @return U result
     * @throws Exception
     */
    public static <U> U wrap(Task<U> task) throws Exception {
        U result = null;
        AtomicReference<Exception> exceptionReference = new AtomicReference<>();

        result = CompletableFuture.supplyAsync(() -> {
            try {
                return Tasks.await(task);
            } catch (Exception e) {
                Log.e("AsyncWrapper", e.getMessage());
                exceptionReference.set(e);
            }
            return null;
        }).get();

        if (exceptionReference.get() != null) {
            Log.e("AsyncWrapper", exceptionReference.get().getMessage());
            throw exceptionReference.get();
        }

        return result;
    }

    /**
     * Safe Async/await wrapper for task.
     *
     * @param task executable
     * @param <U>  return type of task
     * @return U result
     */
    public static <U> U wrapSafe(Task<U> task) {
        U result = null;
        try {
            result = CompletableFuture.supplyAsync(() -> {
                try {
                    return Tasks.await(task);
                } catch (Exception e) {
                    Log.e("AsyncWrapper", e.getMessage());
                }
                return null;
            }).get();
        } catch (Exception e) {
            Log.e("AsyncWrapper", e.getMessage());
        }
        return result;
    }
}
