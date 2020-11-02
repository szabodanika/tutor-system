package service;

import model.Lesson;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.LessonRepository;
import repository.UserRepository;

import java.util.List;

@Service
public class UserService implements CustomService<User, Long>{

    @Autowired
    private UserRepository repository;


    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public User findById(Long aLong) {
        return null;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void save(User item) {

    }
}
