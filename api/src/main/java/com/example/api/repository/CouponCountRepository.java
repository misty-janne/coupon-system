package com.example.api.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
public class CouponCountRepository {

    private static final String COUPON_COUNT_KEY = "coupon_count";

    //redis의 명령어 수행을 위한 템플릿
    private final RedisTemplate<String, String> redisTemplate;

    public CouponCountRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    //incr 명령어 수행을 위한 메서드
    public Long increment() {
        Long count = redisTemplate
                .opsForValue()
                .increment(COUPON_COUNT_KEY);
        // 카운트가 처음 증가할 때 TTL 설정 (하루)
        if (count == 1) {
            redisTemplate.expire(COUPON_COUNT_KEY, 1, TimeUnit.DAYS);
        }

        return count;
    }
}
