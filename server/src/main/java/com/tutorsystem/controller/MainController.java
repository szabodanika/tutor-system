package controller;

import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.LessonService;
import service.PaymentService;
import service.UserService;

@RestController
@RequestMapping("/api")
public class MainController {

    static {
        System.out.println("GECI");
    }

    @Autowired
    private UserService users;

    @Autowired
    private LessonService lessons;

    @Autowired
    private PaymentService payments;

    @GetMapping(value = "/")
    String index(){
        users.save(new User());
        System.out.println("gex");
        return "gex";
    }

    @GetMapping(value = "/ping")
    String ping(){
        users.save(new User());
        System.out.println("gechi");
        return "Pong";
    }
}
