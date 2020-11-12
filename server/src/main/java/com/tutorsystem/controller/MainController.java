package com.tutorsystem.controller;

import com.tutorsystem.model.Config;
import com.tutorsystem.model.Lesson;
import com.tutorsystem.model.Payment;
import com.tutorsystem.model.User;
import com.tutorsystem.service.ConfigService;
import com.tutorsystem.service.LessonService;
import com.tutorsystem.service.PaymentService;
import com.tutorsystem.service.UserService;
import com.tutorsystem.session.UserSessionBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

@RestController
@CrossOrigin(value = "http://localhost:3000", allowCredentials = "true")
@RequestMapping("/api")
public class MainController {

    @Autowired
    private UserService users;

    @Autowired
    private LessonService lessons;

    @Autowired
    private PaymentService payments;

    @Autowired
    private UserSessionBean userSessionBean;

    @Autowired
    private ConfigService configService;

    @GetMapping(value = "/")
    Object index() {
        return "hello";
    }

    @GetMapping(value = "/init")
    Object init() {
        User tutor1 = new User();
        tutor1.setFirstName("Test");
        tutor1.setLastName("Tutor");
        tutor1.setRegistered(new Date());
        tutor1.setRate(12);
        tutor1.setEmail("testtutor@email.com");
        tutor1.setPassword("testpass123");

        User student1 = new User();
        student1.setFirstName("Test");
        student1.setLastName("Student 1");
        student1.setRegistered(new Date());
        student1.setTutor(tutor1);
        student1.setEmail("teststudent1@email.com");
        student1.setPassword("testpass123");
        student1.setRate(student1.getTutor().getRate());

        User student2 = new User();
        student2.setFirstName("Test");
        student2.setLastName("Student 2");
        student2.setRegistered(new Date());
        student2.setTutor(tutor1);
        student2.setEmail("teststudent2@email.com");
        student2.setPassword("testpass123");
        student2.setRate(student1.getTutor().getRate());

        tutor1.setStudents(Arrays.asList(new User[]{student1, student2}));

        Lesson lesson1 = new Lesson();
        lesson1.setTutor(tutor1);
        lesson1.setStudent(student1);
        lesson1.setRate(student1.getRate());
        lesson1.setStart(new Date());
        lesson1.setEnd(new Date(new Date().getTime() + 3600000));

        Lesson lesson2 = new Lesson();
        lesson2.setTutor(tutor1);
        lesson2.setStudent(student1);
        lesson2.setRate(student1.getRate());
        lesson2.setStart(new Date());
        lesson2.setEnd(new Date(new Date().getTime() + 7200000));

        Lesson lesson3 = new Lesson();
        lesson3.setTutor(tutor1);
        lesson3.setStudent(student1);
        lesson3.setRate(student1.getRate());
        lesson3.setStart(new Date());
        lesson3.setEnd(new Date(new Date().getTime() + 3600000));

        Lesson lesson4 = new Lesson();
        lesson4.setTutor(tutor1);
        lesson4.setStudent(student2);
        lesson4.setRate(student2.getRate());
        lesson4.setStart(new Date());
        lesson4.setEnd(new Date(new Date().getTime() + 7200000));

        student1.setStudentLessons(Arrays.asList(new Lesson[]{lesson1, lesson2, lesson3}));
        student2.setStudentLessons(Arrays.asList(new Lesson[]{lesson4}));
        tutor1.setTutorLessons(Arrays.asList(new Lesson[]{lesson1, lesson2, lesson3, lesson4}));

        Payment payment1 = new Payment();
        payment1.setStudent(student1);
        payment1.setTutor(tutor1);
        payment1.setAmount(10);

        Payment payment2 = new Payment();
        payment2.setStudent(student1);
        payment2.setTutor(tutor1);
        payment2.setAmount(20);

        Payment payment3 = new Payment();
        payment3.setStudent(student2);
        payment3.setTutor(tutor1);
        payment3.setAmount(12);

        student1.setPaymentsSent(Arrays.asList(new Payment[]{payment1, payment2}));
        student2.setPaymentsSent(Arrays.asList(new Payment[]{payment3}));
        tutor1.setPaymentsReceived(Arrays.asList(new Payment[]{payment1, payment2, payment3}));

        users.save(tutor1);
        users.save(student1);
        users.save(student2);

        lessons.save(lesson1);
        lessons.save(lesson2);
        lessons.save(lesson3);
        lessons.save(lesson4);

        payments.save(payment1);
        payments.save(payment2);
        payments.save(payment3);

        return new ResponseEntity<>("database_initialised", HttpStatus.OK);
    }

    @GetMapping(value = "/register")
    Object register(@RequestParam String firstname,
                    @RequestParam String lastname,
                    @RequestParam String password,
                    @RequestParam String email,
                    @RequestParam(required = false, value = "tutor", defaultValue = "-1") int tutorCode,
                    @RequestParam(required = false, value = "rate", defaultValue = "-1") int rate) {
        if (configService.get().isSignupOpen()) {
            if (users.findByEmail(email) != null)
                return new ResponseEntity<>("email_already_registered", HttpStatus.BAD_REQUEST);

            if (firstname.length() < 1 || firstname.length() > 100 ||
                    lastname.length() < 1 || lastname.length() > 100 ||
                    password.length() < 1 || password.length() > 100 ||
                    email.length() < 1 || email.length() > 100 ||
                    rate != -1 && (rate < 0 || rate > 1000 ||
                            (tutorCode == -1 && rate == -1))
            )
                return new ResponseEntity<>("incorrect_details", HttpStatus.BAD_REQUEST);

            User newUser = new User();
            newUser.setFirstName(firstname);
            newUser.setLastName(lastname);
            newUser.setPassword(hashPassword(password));
            newUser.setEmail(email);
            newUser.setRegistered(new Date());
            newUser.setActivated(true);

            if (tutorCode != -1) {
                User tutor = users.findByTutorCode(tutorCode);
                if (tutor == null)
                    return new ResponseEntity<>("invalid_tutor_code", HttpStatus.NOT_FOUND);
                newUser.setTutor(false);
                newUser.setTutor(tutor);
                newUser.setRate(tutor.getRate());
            } else {
                newUser.setTutor(true);
                newUser.setTutorCode(users.generateTutorCode());
                newUser.setRate(rate);
            }
            users.save(newUser);

            return new ResponseEntity<>("succesfully_registered", HttpStatus.OK);
        } else return new ResponseEntity<>("signup_closed", HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/registervirtualstudent")
    Object register(@RequestParam String firstname,
                    @RequestParam(required = false) String lastname,
                    @RequestParam int rate) {
        if (configService.get().isSignupOpen()) {
            ResponseEntity validityResult = validateSession();
            if (validityResult != null) return validityResult;

            if (firstname.length() < 1 || firstname.length() > 100 ||
                    lastname.length() < 1 || lastname.length() > 100 ||
                    rate != -1 && (rate < 0 || rate > 1000)
            )
                return new ResponseEntity<>("incorrect_details", HttpStatus.BAD_REQUEST);

            User newUser = new User();
            newUser.setFirstName(firstname);
            newUser.setLastName(lastname);
            newUser.setRegistered(new Date());

            User tutor = this.userSessionBean.getUser();
            newUser.setTutor(false);
            newUser.setTutor(tutor);
            newUser.setRate(rate);
            newUser.setActivationCode(this.generateActivationCode());

            users.save(newUser);

            return new ResponseEntity<>("succesfully_registered", HttpStatus.OK);
        } else return new ResponseEntity<>("signup_closed", HttpStatus.NOT_FOUND);
    }


    @GetMapping(value = "/activatestudent")
    Object register(@RequestParam(name = "tutorcode") int tutorCode,
                    @RequestParam(name = "activationcode") int activationCode,
                    @RequestParam String email,
                    @RequestParam String password) {
        if (configService.get().isSignupOpen()) {

            User user = users.findByActivationCode(activationCode);
            if(user == null){
                return new ResponseEntity<>("invalid_activation_code", HttpStatus.OK);
            }
            if(user.isActivated()){
                return new ResponseEntity<>("user_already_activated", HttpStatus.OK);
            }
            user.setPassword(this.hashPassword(password));
            user.setEmail(email);
            user.setActivated(true);
            users.save(user);

            return new ResponseEntity<>("succesfully_activated", HttpStatus.OK);
        } else return new ResponseEntity<>("signup_closed", HttpStatus.NOT_FOUND);
    }



    @GetMapping(value = "/login")
    Object postRegister(@RequestParam String email,
                        @RequestParam String password) {
        if (configService.get().isLoginOpen()) {
            User user = users.findByEmailAndPassword(email, hashPassword(password));
            if (user != null) {
                this.userSessionBean.setUser(user);

                ResponseEntity validityResult = validateSession();
                if (validityResult != null) return validityResult;

                return user;
            } else {
                return new ResponseEntity<>("invalid_credentials", HttpStatus.FORBIDDEN);
            }
        } else return new ResponseEntity<>("login_closed", HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/user")
    Object getUser(@RequestParam(required = false) Long id,
                   @RequestParam(required = false) String email) {
        ResponseEntity validityResult = validateSession();
        if (validityResult != null) return validityResult;

        if (id == null && email == null) {
            return new ResponseEntity<>("no_id_or_email", HttpStatus.BAD_REQUEST);
        } else if (id != null) {
            User user = users.findById(id);
            if (user == null)
                return new ResponseEntity<>("user_not_found", HttpStatus.NOT_FOUND);
            return user;
        } else if (!email.equals("")) {
            User user = users.findByEmail(email);
            if (user == null)
                return new ResponseEntity<>("user_not_found", HttpStatus.NOT_FOUND);
            return user;
        }
        return null;
    }

    @GetMapping(value = "/students")
    Object getStudents(@RequestParam(required = false, name = "tutor") Long tutorId) {
        ResponseEntity validityResult = validateSession(tutorId);
        if (validityResult != null) return validityResult;

        User tutor = users.findById(tutorId);
        if (tutor == null)
            return new ResponseEntity<>("tutor_not_found", HttpStatus.NOT_FOUND);

        return tutor.getStudents();
    }

    @GetMapping(value = "/resettutorcode")
    Object resetTutorCode(@RequestParam(name = "tutor") Long tutorId) {
        ResponseEntity validityResult = validateSession(tutorId);
        if (validityResult != null) return validityResult;

        User tutor = users.findById(tutorId);
        if (tutor == null)
            return new ResponseEntity<>("tutor_not_found", HttpStatus.NOT_FOUND);

        int newCode = users.generateTutorCode();
        tutor.setTutorCode(newCode);
        users.save(tutor);
        return newCode;
    }


    @PostMapping(value = "/lesson")
    Object postLesson(@RequestParam Long studentId, @RequestParam Long tutorId, @RequestParam Date start,
                      @RequestParam Date end) {
        ResponseEntity validityResult = validateSession(tutorId);
        if (validityResult != null) return validityResult;


        User student = users.findById(studentId);
        if (student == null)
            return new ResponseEntity<>("student_not_found", HttpStatus.NOT_FOUND);

        User tutor = users.findById(tutorId);
        if (tutor == null)
            return new ResponseEntity<>("tutor_not_found", HttpStatus.NOT_FOUND);

        if (start.after(end))
            return new ResponseEntity<>("invalid_dates", HttpStatus.NOT_FOUND);

        Lesson lesson = new Lesson();
        lesson.setStudent(student);
        lesson.setTutor(tutor);
        lesson.setStart(start);
        lesson.setEnd(end);
        lesson.setRate(student.getRate());
        lessons.save(lesson);

        return new ResponseEntity<>("lesson_created", HttpStatus.OK);
    }

    @GetMapping(value = "/savepayment")
    Object postPayment(@RequestParam(required = false, defaultValue = "-1") Long id,
                       @RequestParam(name = "tutor") Long tutorId,
                       @RequestParam(name = "student") Long studentId,
                       @RequestParam int amount,
                       @RequestParam boolean cash,
                       @RequestParam(required = false) String transaction) {
        ResponseEntity validityResult = validateSession(tutorId);
        if (validityResult != null) return validityResult;

        User student = users.findById(studentId);
        if (student == null)
            return new ResponseEntity<>("student_not_found", HttpStatus.NOT_FOUND);

        User tutor = users.findById(tutorId);
        if (tutor == null)
            return new ResponseEntity<>("tutor_not_found", HttpStatus.NOT_FOUND);


        Payment payment;
        if (id != -1) {
            payment = payments.findById(id);
            if (payment == null)
                return new ResponseEntity<>("payment_not_found", HttpStatus.NOT_FOUND);
        } else {
            payment = new Payment();
            payment.setDate(new java.util.Date());
        }
        payment.setStudent(student);
        payment.setTutor(tutor);
        payment.setAmount(amount);
        payment.setCash(cash);
        payment.setTransactionNumber(transaction);
        payments.save(payment);

        return new ResponseEntity<>("payment_created", HttpStatus.OK);
    }

    @GetMapping(value = "/paymentssent")
    Object getPaymentsSent(@RequestParam(value = "user") Long userId) {
        ResponseEntity validityResult = validateSession(userId);
        if (validityResult != null) return validityResult;

        User user = users.findById(userId);
        if (user == null)
            return new ResponseEntity<>("user_not_found", HttpStatus.NOT_FOUND);
        else return user.getPaymentsSent();
    }

    @GetMapping(value = "/paymentsreceived")
    Object getPaymentsReceived(@RequestParam(value = "user") Long userId) {
        ResponseEntity validityResult = validateSession(userId);
        if (validityResult != null) return validityResult;

        User user = users.findById(userId);
        if (user == null)
            return new ResponseEntity<>("user_not_found", HttpStatus.NOT_FOUND);
        else return user.getPaymentsReceived();
    }

    @GetMapping(value = "/tutorlessons")
    Object getTutorLessons(@RequestParam(value = "tutor") Long tutorId, @RequestParam(required = false, value = "week",
            defaultValue = "-1") int week) {
        ResponseEntity validityResult = validateSession(tutorId);
        if (validityResult != null) return validityResult;

        User tutor = users.findById(tutorId);
        if (tutor == null)
            return new ResponseEntity<>("tutor_not_found", HttpStatus.NOT_FOUND);


        if (week > -1 && week < 53) {
            return lessons.findLessonsByTutorAndWeek(tutor, week);
        } else {
            return lessons.findLessonsByTutor(tutor);
        }
    }

    @GetMapping(value = "/studentlessons")
    Object getStudentLessons(@RequestParam(value = "student") Long studentId) {
        ResponseEntity validityResult = validateSession(studentId);
        if (validityResult != null) return validityResult;

        User student = users.findById(studentId);
        if (student == null)
            return new ResponseEntity<>("student_not_found", HttpStatus.NOT_FOUND);

        else return student.getStudentLessons();
    }

    private ResponseEntity validateSession(Long user) {
        if (configService.get().isRestOpen()) {
            String validity = userSessionBean.validate(user);
            if (validity == "ok") return null;
            else return new ResponseEntity(validity, HttpStatus.FORBIDDEN);
        } else return new ResponseEntity("api_closed", HttpStatus.FORBIDDEN);
    }

    private ResponseEntity validateSession() {
        if (configService.get().isRestOpen()) {
            String validity = userSessionBean.validate();
            if (validity == "ok") return null;
            else return new ResponseEntity(validity, HttpStatus.FORBIDDEN);
        } else return new ResponseEntity("api_closed", HttpStatus.FORBIDDEN);
    }

    private String hashPassword(String password) {
        MessageDigest md = null;
        try {
            md = java.security.MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] messageDigest = md.digest(password.getBytes());
        BigInteger no = new BigInteger(1, messageDigest);
        String hashtext = no.toString(16);
        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }
        return hashtext;
    }

    private int generateActivationCode(){
        return new Random().nextInt(configService.get().getActivationCodeMaxValue());
    }
}
