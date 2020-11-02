package service;

import model.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.PaymentRepository;
import repository.UserRepository;

import java.util.List;

@Service
public class PaymentService implements CustomService<Payment, Long> {

    @Autowired
    private PaymentRepository repository;

    @Override
    public List<Payment> findAll() {
        return null;
    }

    @Override
    public Payment findById(Long aLong) {
        return null;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void save(Payment item) {

    }
}
