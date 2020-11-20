package com.tutorsystem.repository;

import com.tutorsystem.model.Announcement;
import com.tutorsystem.model.PasswordReset;
import com.tutorsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetRepository extends JpaRepository<PasswordReset, Long> {

    PasswordReset findByUser(User user);

    PasswordReset findByResetCode(int resetCode);

}
