package com.property.controller;

import com.property.entity.*;
import com.property.security.UserDetailsImpl;
import com.property.service.OwnerService;
import com.property.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;
    
    @Autowired
    private OwnerService ownerService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<PageResult<Payment>> getPaymentPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String period) {
        PageResult<Payment> result = paymentService.getPaymentPage(pageNum, pageSize, status, null, period);
        return ApiResponse.success(result);
    }

    @GetMapping("/{id}")
    public ApiResponse<Payment> getPaymentById(@PathVariable Long id) {
        Payment payment = paymentService.getPaymentById(id);
        return ApiResponse.success(payment);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Payment> createPayment(@RequestBody Payment payment) {
        Payment result = paymentService.createPayment(payment);
        return ApiResponse.success("创建成功", result);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Payment> updatePayment(@PathVariable Long id, @RequestBody Payment payment) {
        payment.setId(id);
        Payment result = paymentService.updatePayment(payment);
        return ApiResponse.success("更新成功", result);
    }

    @PostMapping("/{id}/pay")
    @PreAuthorize("hasRole('OWNER') or hasRole('ADMIN')")
    public ApiResponse<Payment> payPayment(@PathVariable Long id,
                                            @RequestParam String paymentMethod,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Payment payment = paymentService.payPayment(id, paymentMethod);
        return ApiResponse.success("支付成功", payment);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
        return ApiResponse.success("删除成功", null);
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('OWNER')")
    public ApiResponse<PageResult<Payment>> getMyPayments(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Owner owner = ownerService.getOwnerByUserId(userDetails.getUser().getId());
        if (owner == null) {
            return ApiResponse.error("未找到业主信息");
        }
        PageResult<Payment> result = paymentService.getPaymentPage(pageNum, pageSize, null, owner.getId(), null);
        return ApiResponse.success(result);
    }

    @GetMapping("/my/unpaid")
    @PreAuthorize("hasRole('OWNER')")
    public ApiResponse<BigDecimal> getMyUnpaidAmount(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Owner owner = ownerService.getOwnerByUserId(userDetails.getUser().getId());
        if (owner == null) {
            return ApiResponse.error("未找到业主信息");
        }
        BigDecimal amount = paymentService.getUnpaidAmount(owner.getId());
        return ApiResponse.success(amount);
    }
}
