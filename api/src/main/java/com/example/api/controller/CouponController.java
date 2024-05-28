package com.example.api.controller;

import com.example.api.domain.Coupon;
import com.example.api.service.ApplyService;
import com.example.api.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coupons")
public class CouponController {

    @Autowired
    private ApplyService applyService;

    @Autowired
    private CouponService couponService;

    @PostMapping("/apply/{userId}")
    public ResponseEntity<String> applyCoupon(@PathVariable Long userId) {
        applyService.apply(userId);
        return ResponseEntity.ok("Coupon applied if available.");
    }

    @GetMapping
    public ResponseEntity<List<Coupon>> getAllCoupons() {
        List<Coupon> coupons = couponService.getAllCoupons();
        return ResponseEntity.ok(coupons);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Coupon> getCouponById(@PathVariable Long id) {
        Coupon coupon = couponService.getCouponById(id);
        if (coupon != null) {
            return ResponseEntity.ok(coupon);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Coupon> updateCoupon(@PathVariable Long id, @RequestBody Coupon updatedCoupon) {
        Coupon coupon = couponService.updateCoupon(id, updatedCoupon);
        if (coupon != null) {
            return ResponseEntity.ok(coupon);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoupon(@PathVariable Long id) {
        if (couponService.deleteCoupon(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
