package com.atguigu.springcloud.service.impl;


import com.atguigu.springcloud.dao.PaymentDao;
import com.atguigu.springcloud.entities.Payment;
import com.atguigu.springcloud.service.PaymentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Resource
    private PaymentDao paymentDao8002;

    @Override
    public int create(Payment payment) {
        return paymentDao8002.create(payment);
    }

    @Override
    public Payment getPaymentById(Long id) {
        return paymentDao8002.getPaymentById(id);
    }
}
