package com.example.api.service;

import com.example.api.domain.Coupon;
import com.example.api.repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CouponService {
    @Autowired
    private CouponRepository couponRepository;

    public List<Coupon> getAllCoupons() {
        return couponRepository.findAll();
    }

    public Coupon getCouponById(Long id) {
        Optional<Coupon> coupon = couponRepository.findById(id);
        return coupon.orElse(null);
    }

    public Coupon updateCoupon(Long id, Coupon updatedCoupon) {
        Optional<Coupon> existingCoupon = couponRepository.findById(id);
        if (existingCoupon.isPresent()) {
            Coupon coupon = existingCoupon.get();
            coupon.setUserId(updatedCoupon.getUserId());
            return couponRepository.save(coupon);
        } else {
            return null;
        }
    }

    public boolean deleteCoupon(Long id) {
        if (couponRepository.existsById(id)) {
            couponRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
