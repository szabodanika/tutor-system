package service;

import model.Lesson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.LessonRepository;

import java.util.List;

@Service
public class LessonService implements CustomService<Lesson, Long>{

    @Autowired
    private LessonRepository repository;

    @Override
    public List<Lesson> findAll() {
        return null;
    }

    @Override
    public Lesson findById(Long aLong) {
        return null;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void save(Lesson item) {

    }
}
