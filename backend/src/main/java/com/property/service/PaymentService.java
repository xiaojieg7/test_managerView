package com.property.service;

import com.property.entity.Payment;
import com.property.entity.PageResult;
import java.math.BigDecimal;
import java.util.List;

public interface PaymentService {
    PageResult<Payment> getPaymentPage(Integer pageNum, Integer pageSize, String status, Long ownerId, String period);
    Payment getPaymentById(Long id);
    Payment createPayment(Payment payment);
    Payment updatePayment(Payment payment);
    Payment payPayment(Long paymentId, String paymentMethod);
    void deletePayment(Long id);
    List<Payment> getMyPayments(Long ownerId);
    BigDecimal getUnpaidAmount(Long ownerId);
}
