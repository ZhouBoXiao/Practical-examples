package com.example;

import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class TestReactor {

    @Test
    public void test() {
        //just()：创建Flux序列，并声明数据流，
        Flux<Integer> integerFlux = Flux.just(1, 2, 3, 4);

        //subscribe()：订阅Flux序列，只有进行订阅后才回触发数据流，
        integerFlux.subscribe(System.out::println);

        Flux<String> stringFlux = Flux.just("hello", "world");
        stringFlux.subscribe(System.out::println);

        //fromArray(),fromIterable()和fromStream()
        //
        Integer[] array = {1,2,3,4};
        Flux.fromArray(array).subscribe(System.out::println);

        List<Integer> integers = Arrays.asList(array);
        Flux.fromIterable(integers).subscribe(System.out::println);

        Stream<Integer> stream = integers.stream();
        Flux.fromStream(stream).subscribe(System.out::println);

        Flux.just("Ben", "Michael", "Mark").subscribe(new Subscriber<>() {
            public void onSubscribe(Subscription s) {
                s.request(3);
            }

            public void onNext(String s) {
                System.out.println("Hello " + s + "!");
            }

            public void onError(Throwable t) {

            }

            public void onComplete() {
                System.out.println("Completed");
            }
        });

        Flux.just("Ben", "Michael", "Mark")
                .doOnNext(s -> System.out.println("Hello " + s + "!"))
                .doOnComplete(() -> System.out.println("Completed"))
                .take(2)
                .subscribe();

        Flux.just("Ben", "Michael", "Mark").flatMap(key -> {
                    System.out.println("Map 1: " + key + " (" + Thread.currentThread().getName() + ")");
                    return Flux.just(key);
                }
        ).flatMap(value -> {
                    System.out.println("Map 2: " + value + " (" + Thread.currentThread().getName() + ")");
                    return Flux.just(value);
                }
        ).subscribeOn(Schedulers.parallel()).subscribe();


        Flux.just("Ben", "Michael", "Mark").flatMap(key -> {
            System.out.println("Map 1: " + key + " (" + Thread.currentThread().getName() + ")");
            return Flux.just(key);
        }).flatMap(value -> {
            System.out.println("Map 2: " + value + " (" + Thread.currentThread().getName() + ")");
            return Flux.just(value);
        }).subscribeOn(Schedulers.parallel()).subscribe();
    }


    @Test
    public void testMono() {
        Mono data = Mono.just("bole");
        Mono noData = Mono.empty();
        data.subscribe(System.out::println);
    }

    void demo() {
        final Flux<Long> flux = Flux.interval(Duration.of(100, ChronoUnit.MILLIS))
                .map(log())//1
                .publishOn(Schedulers.parallel())//2
                .map(log())//3
                .publishOn(Schedulers.single())//4
                .flatMap(logOfMono());//5
        flux.subscribe();
    }

    private Function logOfMono() {
        return o -> {
            System.out.println(o);
            return o;
        };
    }

    private Function log() {
        return o -> {
            System.out.println(o);
            return o;
        };
    }
}
