package com.tutorsystem.repository;

import com.tutorsystem.model.Lesson;
import com.tutorsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    List<Lesson> findLessonsByTutor(User tutor);

}
