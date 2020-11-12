package com.tutorsystem.service;

import com.tutorsystem.model.Lesson;
import com.tutorsystem.model.Payment;
import com.tutorsystem.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentService implements CustomService<Payment, Long> {

    @Autowired
    private PaymentRepository repository;

    @Override
    public List<Payment> findAll() {
        return this.repository.findAll();
    }

    @Override
    public Payment findById(Long id) {
        Optional<Payment> payment = this.repository.findById(id);
        return payment.orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        this.repository.deleteById(id);
    }

    @Override
    public void save(Payment payment) {
        this.repository.save(payment);
    }
}
