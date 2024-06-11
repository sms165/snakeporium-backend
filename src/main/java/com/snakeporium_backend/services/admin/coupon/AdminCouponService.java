package com.snakeporium_backend.services.admin.coupon;

import com.snakeporium_backend.entity.Coupon;

import java.util.List;

public interface AdminCouponService {

    Coupon createCoupon(Coupon coupon);

    List<Coupon> getAllCoupons();
}
