package com.example.api.service;

import com.example.api.repository.CouponRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ApplyServiceTest {

    @Autowired
    private ApplyService applyService;

    @Autowired
    private CouponRepository couponRepository;

    @Test
    public void oneTimeApply() {
        applyService.apply(1L);

        long count = couponRepository.count();

        assertThat(count).isEqualTo(1);
    }

    @Test
    public void multipleApply() throws InterruptedException {
        //1000개의 요청을
        int threadCount = 1000;
        //32개의 스레드풀 생성하여 병렬처리
        ExecutorService executorService = Executors.newFixedThreadPool(32);

        //다른스레드에서 수행하는 작업을 기다리도록 도와주는 클래스
        CountDownLatch latch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            long userId = i;
            executorService.submit(() -> {
                try {
                    applyService.apply(userId);
                } finally {
                    //각 작업이 완료될 때마다 latch의 카운트를 감소시킴
                    latch.countDown();
                }
            });
        }

        //모든 스레드의 작업이 완료될 때까지 대기
        latch.await();

        long count = couponRepository.count();

        //100개의 쿠폰만 생성되는지 테스트케이스 결과 확인
        assertThat(count).isEqualTo(100);

        //100개의 쿠폰이아닌 116개의 쿠폰을 발행시킴 --> 레이스 컨디션 발생
        //멀티스레드+redis의 incr 활용한 동기화
        // --> Redis의 `INCR` 명령어를 사용하여 원자적(atomic)으로 증가 연산을 수행함으로써 동기화 문제를 해결
    }
}