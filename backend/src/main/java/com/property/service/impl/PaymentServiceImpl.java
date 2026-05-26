package com.property.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.property.entity.Payment;
import com.property.entity.PageResult;
import com.property.mapper.PaymentMapper;
import com.property.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentMapper paymentMapper;

    @Override
    public PageResult<Payment> getPaymentPage(Integer pageNum, Integer pageSize, String status, Long ownerId, String period) {
        Page<Payment> page = new Page<>(pageNum, pageSize);
        IPage<Payment> result = paymentMapper.selectPaymentPage(page, status, ownerId, period);
        return new PageResult<>(result.getTotal(), result.getRecords());
    }

    @Override
    public Payment getPaymentById(Long id) {
        return paymentMapper.selectById(id);
    }

    @Override
    @Transactional
    public Payment createPayment(Payment payment) {
        payment.setStatus("UNPAID");
        paymentMapper.insert(payment);
        return payment;
    }

    @Override
    @Transactional
    public Payment updatePayment(Payment payment) {
        paymentMapper.updateById(payment);
        return payment;
    }

    @Override
    @Transactional
    public Payment payPayment(Long paymentId, String paymentMethod) {
        Payment payment = paymentMapper.selectById(paymentId);
        if (payment == null) {
            throw new RuntimeException("缴费记录不存在");
        }
        if ("PAID".equals(payment.getStatus())) {
            throw new RuntimeException("该账单已支付");
        }
        payment.setStatus("PAID");
        payment.setPaidTime(LocalDateTime.now());
        payment.setPaymentMethod(paymentMethod);
        paymentMapper.updateById(payment);
        return payment;
    }

    @Override
    @Transactional
    public void deletePayment(Long id) {
        paymentMapper.deleteById(id);
    }

    @Override
    public List<Payment> getMyPayments(Long ownerId) {
        return paymentMapper.selectList(new LambdaQueryWrapper<Payment>()
                .eq(Payment::getOwnerId, ownerId)
                .orderByDesc(Payment::getCreateTime));
    }

    @Override
    public BigDecimal getUnpaidAmount(Long ownerId) {
        BigDecimal amount = paymentMapper.selectList(
                new LambdaQueryWrapper<Payment>()
                        .eq(Payment::getOwnerId, ownerId)
                        .in(Payment::getStatus, "UNPAID", "OVERDUE")
        ).stream()
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return amount;
    }
}
