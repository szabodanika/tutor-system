package com.tutorsystem.service;

import com.tutorsystem.model.Lesson;
import com.tutorsystem.model.Payment;
import com.tutorsystem.model.User;
import com.tutorsystem.repository.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LessonService implements CustomService<Lesson, Long>{

    @Autowired
    private LessonRepository repository;

    @Override
    public List<Lesson> findAll() {
        return this.repository.findAll();
    }

    @Override
    public Lesson findById(Long id) {
        Optional<Lesson> lesson = this.repository.findById(id);
        return lesson.orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        this.repository.deleteById(id);
    }

    @Override
    public void save(Lesson lesson) {
        this.repository.save(lesson);
    }

    public List<Lesson> findLessonsByTutorAndWeek(User tutor, int week){
        List<Lesson> lessons = repository.findLessonsByTutor(tutor);
        List<Lesson> filteredByWeek = new ArrayList<>();

        for(Lesson l : lessons){
            if (l.getWeek() == week) {
                filteredByWeek.add(l);
            }
        }

        return filteredByWeek;
    }

    public List<Lesson> findLessonsByTutor(User tutor){
        return repository.findLessonsByTutor(tutor);
    }

}
