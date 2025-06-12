package com.example.order;

import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.Duration;
import java.util.concurrent.*;

public class CompleteableFutureTest {

    @Test
    void test()
            throws InterruptedException, ExecutionException {

        try (var svc = Executors.newFixedThreadPool(2)) {
            svc.submit(() -> System.out.println("hello W"));
            Runnable print = () -> System.out.println("Hello_print");
            Runnable newLine = System.out::println;

            svc.execute(newLine);
            svc.execute(print);

            Callable<String> print3 = () -> "Hello3";
            Future<String> futureResult = svc.submit(print3);
            String result = futureResult.get();
            System.out.println(result);
        }
    }


    @Test
    void test2()
            throws InterruptedException, ExecutionException {

        try (var svc = Executors.newFixedThreadPool(2)) {

            Callable<String> printDelay = () -> {
                Thread.sleep(3_500);
                System.out.println("printing from " + Thread.currentThread().getName());
                return "Print Delayed";
            };

            Future<String> future = svc.submit(printDelay);

            String result = future.get();
            System.out.println("printing from " + Thread.currentThread().getName());

            System.out.println(result);
        }
    }

    @Test
    void test3()
            throws InterruptedException, ExecutionException, TimeoutException {

        try (var svc = Executors.newFixedThreadPool(2)) {

            Callable<String> callable = () -> {
                Thread.sleep(3_500);
                System.out.println("printing from " + Thread.currentThread().getName());
                return "Print Delayed";
            };

            Future<String> future = svc.submit(callable);

            String result = future.get(1_50, TimeUnit.MILLISECONDS);
            System.out.println("printing from " + Thread.currentThread().getName());

            System.out.println(result);
        }
    }

    @Test
    void test4()
            throws InterruptedException, ExecutionException {

        try (var svc = Executors.newFixedThreadPool(2)) {

            CompletableFuture<String> completableFuture = new CompletableFuture<>();

            svc.submit(() -> {
                Thread.sleep(3_500);
                completableFuture.complete("Print Delayed");
                return null;
            });

            String result = completableFuture.get();
            System.out.println(result);
        }
    }

    @Test
    void test5()
            throws InterruptedException, ExecutionException {

        try (var svc = Executors.newFixedThreadPool(2)) {
            CompletableFuture<String> completableFuture = new CompletableFuture<>();

            Callable<Void> callable = () -> {
                Thread.sleep(3_500);
                completableFuture.complete("Print Delayed");
                System.out.println("printing from " + Thread.currentThread().getName());
                return null;
            };

            svc.submit(callable);
            String result = completableFuture.get();
            System.out.println("printing from " + Thread.currentThread().getName());

            System.out.println(result);
        }
    }

    @Test
    void test6()
            throws InterruptedException, ExecutionException {
//        https://www.baeldung.com/java-completablefuture

    }

}



