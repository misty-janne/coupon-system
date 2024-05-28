package com.example.api.service;

import com.example.api.domain.Coupon;
import com.example.api.repository.CouponCountRepository;
import com.example.api.repository.CouponRepository;
import org.springframework.stereotype.Service;

@Service
public class ApplyService {
    private final CouponRepository couponRepository;

    private final CouponCountRepository couponCountRepository;

    public ApplyService(CouponRepository couponRepository, CouponCountRepository couponCountRepository) {
        this.couponRepository = couponRepository;
        this.couponCountRepository = couponCountRepository;
    }

    //쿠폰발급
    public void apply(Long userId) {
        //쿠폰의 개수 가져오기 (최대 100개)
//        long count = couponRepository.count();

        //redis의 incr 명령어를 통한 제어
        long count = couponCountRepository.increment();

        //발급가능 개수초과시 수행X(하루에 100개만 발급)
        if (count > 100) {
            return;
        }

        //쿠폰 남아있으면 발급 수행 (쿠폰 새로 생성)
        couponRepository.save(new Coupon(userId));
    }
}
