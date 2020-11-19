package com.tutorsystem.controller;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    @GetMapping(value = "/register")
    Object register(@RequestParam String firstname,
                    @RequestParam(required = false) String lastname,
                    @RequestParam String password,
                    @RequestParam String email,
                    @RequestParam(required = false, value = "tutor", defaultValue = "-1") int tutorCode,
                    @RequestParam(required = false, value = "rate", defaultValue = "-1") int rate) {
        if (configService.get().isSignupOpen()) {
            if (users.findByEmail(email) != null)
                return new ResponseEntity<>("email_already_registered", HttpStatus.BAD_REQUEST);

            if (firstname != null && !validateName(firstname)){
                return new ResponseEntity<>("incorrect_name", HttpStatus.BAD_REQUEST);
            }
            if (lastname != null && !validateName(lastname)){
                return new ResponseEntity<>("incorrect_name", HttpStatus.BAD_REQUEST);
            }
            if (email != null && !validateEmail(email)){
                return new ResponseEntity<>("incorrect_email", HttpStatus.BAD_REQUEST);
            }
            if (password != null && !validatePassword(password)){
                return new ResponseEntity<>("incorrect_password", HttpStatus.BAD_REQUEST);
            }

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
                newUser.setTutorAccount(false);
                newUser.setTutor(tutor);
                newUser.setRate(tutor.getRate());
            } else {
                newUser.setTutorAccount(true);
                newUser.setTutorCode(users.generateTutorCode());
                newUser.setRate(rate);
            }
            users.save(newUser);

            return new ResponseEntity<>("succesfully_registered", HttpStatus.OK);
        } else return new ResponseEntity<>("signup_closed", HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/registervirtualstudent")
    Object registerVirtualStudent(@RequestParam String firstname,
                                  @RequestParam(required = false) String lastname,
                                  @RequestParam int rate) {
        if (configService.get().isSignupOpen()) {
            ResponseEntity validityResult = validateSession();
            if (validityResult != null) return validityResult;


            if (firstname != null && !validateName(firstname)){
                return new ResponseEntity<>("incorrect_name", HttpStatus.BAD_REQUEST);
            }
            if (lastname != null && !validateName(lastname)){
                return new ResponseEntity<>("incorrect_name", HttpStatus.BAD_REQUEST);
            }
            User newUser = new User();
            newUser.setFirstName(firstname);
            newUser.setLastName(lastname);
            newUser.setRegistered(new Date());

            User tutor = this.userSessionBean.getUser();
            newUser.setTutorAccount(false);
            newUser.setTutor(tutor);
            newUser.setRate(rate);
            newUser.setActivationCode(this.generateActivationCode());

            users.save(newUser);

            return new ResponseEntity<>("succesfully_registered", HttpStatus.OK);
        } else return new ResponseEntity<>("signup_closed", HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/editstudentaccount")
    Object editStudentAccount(@RequestParam String firstname,
                              @RequestParam(required = false) String lastname,
                              @RequestParam String email,
                              @RequestParam(required = false) String phone,
                              @RequestParam(required = false) String password) {
        ResponseEntity validityResult = validateSession();
        if (validityResult != null) return validityResult;


        if (phone != null && !validatePhone(phone)){
            return new ResponseEntity<>("incorrect_phone", HttpStatus.BAD_REQUEST);
        }
        if (password != null && !validatePassword(password)){
            return new ResponseEntity<>("incorrect_password", HttpStatus.BAD_REQUEST);
        }
        if (firstname != null && !validateName(firstname)){
            return new ResponseEntity<>("incorrect_name", HttpStatus.BAD_REQUEST);
        }
        if (lastname != null && !validateName(lastname)){
            return new ResponseEntity<>("incorrect_name", HttpStatus.BAD_REQUEST);
        }
        if (email != null && !validateEmail(email)){
            return new ResponseEntity<>("incorrect_email", HttpStatus.BAD_REQUEST);
        }

        User user = userSessionBean.getUser();
        user.setFirstName(firstname);
        user.setLastName(lastname);
        user.setEmail(email);
        user.setPhone(phone);
        if(password != null) {
            user.setPassword(lastname);
        }

        users.save(user);

        return new ResponseEntity<>("succesfully_saved_account", HttpStatus.OK);
    }

    @GetMapping(value = "/edittutoraccount")
    Object editTutorAccount(@RequestParam String firstname,
                              @RequestParam(required = false) String lastname,
                              @RequestParam String email,
                              @RequestParam(required = false) String phone,
                              @RequestParam(required = false) int rate,
                              @RequestParam(required = false) String info,
                              @RequestParam(required = false) String password) {
        ResponseEntity validityResult = validateSession();
        if (validityResult != null) return validityResult;


        if (phone != null && !validatePhone(phone)){
            return new ResponseEntity<>("incorrect_phone", HttpStatus.BAD_REQUEST);
        }
        if (password != null && !validatePassword(password)){
            return new ResponseEntity<>("incorrect_password", HttpStatus.BAD_REQUEST);
        }
        if (firstname != null && !validateName(firstname)){
            return new ResponseEntity<>("incorrect_name", HttpStatus.BAD_REQUEST);
        }
        if (lastname != null && !validateName(lastname)){
            return new ResponseEntity<>("incorrect_name", HttpStatus.BAD_REQUEST);
        }
        if (email != null && !validateEmail(email)){
            return new ResponseEntity<>("incorrect_email", HttpStatus.BAD_REQUEST);
        }

        User user = userSessionBean.getUser();
        user.setFirstName(firstname);
        user.setLastName(lastname);
        user.setEmail(email);
        user.setPhone(phone);
        user.setRate(rate);
        user.setInfo(info);
        if(password != null) {
            user.setPassword(lastname);
        }

        users.save(user);

        return new ResponseEntity<>("succesfully_saved_account", HttpStatus.OK);
    }


    @GetMapping(value = "/activatestudent")
    Object activateStudent(@RequestParam(name = "activationcode") int activationCode,
                           @RequestParam String email,
                           @RequestParam String password) {
        if (configService.get().isSignupOpen()) {

            User user = users.findByActivationCode(activationCode);
            if (user == null) {
                return new ResponseEntity<>("invalid_activation_code", HttpStatus.OK);
            }
            if (user.isActivated()) {
                return new ResponseEntity<>("user_already_activated", HttpStatus.OK);
            }

            if (password != null && !validatePassword(password)){
                return new ResponseEntity<>("incorrect_password", HttpStatus.BAD_REQUEST);
            }
            if (email != null && !validateEmail(email)){
                return new ResponseEntity<>("incorrect_email", HttpStatus.BAD_REQUEST);
            }

            user.setPassword(this.hashPassword(password));
            user.setEmail(email);
            user.setActivated(true);
            users.save(user);

            this.userSessionBean.setUser(user);

            return user;

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


    @GetMapping(value = "/signout")
    Object postSignOut() {
        ResponseEntity validityResult = validateSession();
        if (validityResult != null) return validityResult;

        this.userSessionBean.signOut();
        return new ResponseEntity<>("signed_out_successfully", HttpStatus.OK);
    }

    @GetMapping(value = "/deactivate")
    Object deactivate() {
        ResponseEntity validityResult = validateSession();
        if (validityResult != null) return validityResult;

        this.userSessionBean.getUser().setDisabled(true);
        this.users.save(this.userSessionBean.getUser());
        return new ResponseEntity<>("account_deactivated_successfully", HttpStatus.OK);
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

    @GetMapping(value = "/savestudent")
    Object saveStudent(@RequestParam Long id, @RequestParam int rate) {

        ResponseEntity validityResult = validateSession();
        if (validityResult != null) return validityResult;

        User student = users.findById(id);
        if (student == null)
            return new ResponseEntity<>("student_not_found", HttpStatus.NOT_FOUND);
        if (student.isDisabled())
            return new ResponseEntity<>("student_disabled", HttpStatus.NOT_FOUND);

        student.setRate(rate);
        users.save(student);

        return student;
    }

    @GetMapping(value = "/disablestudent")
    Object disableStudent(@RequestParam Long id) {

        ResponseEntity validityResult = validateSession();
        if (validityResult != null) return validityResult;

        User student = users.findById(id);
        if (student == null)
            return new ResponseEntity<>("student_not_found", HttpStatus.NOT_FOUND);
        if (student.isDisabled())
            return new ResponseEntity<>("student_disabled", HttpStatus.NOT_FOUND);

        student.setDisabled(true);
        users.save(student);

        return student;
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
        if (student.isDisabled())
            return new ResponseEntity<>("student_disabled", HttpStatus.NOT_FOUND);

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

    @GetMapping(value = "/savelesson")
    Object postLesson(@RequestParam(required = false, defaultValue = "-1") Long id,
                      @RequestParam(name = "student") Long studentId,
                      @RequestParam String date,
                      @RequestParam String start,
                      @RequestParam String end,
                      @RequestParam String location) {

        ResponseEntity validityResult = validateSession();
        if (validityResult != null) return validityResult;

        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate;
        Date endDate;
        try {
            startDate = parser.parse(date + " " + start);
            endDate = parser.parse(date + " " + end);
        } catch (ParseException e) {
            return new ResponseEntity<>("invalid_date", HttpStatus.NOT_FOUND);
        }

        User student = users.findById(studentId);
        if (student == null)
            return new ResponseEntity<>("student_not_found", HttpStatus.NOT_FOUND);
        if (student.isDisabled())
            return new ResponseEntity<>("student_disabled", HttpStatus.NOT_FOUND);

        Lesson lesson;

        if (id != -1) {
            lesson = lessons.findById(id);
            if (lesson == null)
                return new ResponseEntity<>("lesson_not_found", HttpStatus.NOT_FOUND);
        } else {
            lesson = new Lesson();
        }

        lesson.setTutor(userSessionBean.getUser());
        lesson.setStudent(student);
        lesson.setStart(startDate);
        lesson.setEnd(endDate);
        lesson.setLocation(location);
        lesson.setRate(student.getRate());

        lessons.save(lesson);
        return new ResponseEntity<>("lesson_created", HttpStatus.OK);
    }

    @GetMapping(value = "/getlesson")
    Object getLessonById(@RequestParam Long id) {

        ResponseEntity validityResult = validateSession();
        if (validityResult != null) return validityResult;

        Lesson lesson = lessons.findById(id);
        if (lesson == null)
            return new ResponseEntity<>("lesson_not_found", HttpStatus.NOT_FOUND);

        return lesson;
    }


    @GetMapping(value = "/locklesson")
    Object lockLesson(@RequestParam Long id) {

        ResponseEntity validityResult = validateSession();
        if (validityResult != null) return validityResult;

        Lesson lesson = lessons.findById(id);
        if (lesson == null)
            return new ResponseEntity<>("lesson_not_found", HttpStatus.NOT_FOUND);

        User student = lesson.getStudent();
        if (student.isDisabled())
            return new ResponseEntity<>("student_disabled", HttpStatus.NOT_FOUND);

        lesson.setLocked(true);
        lessons.save(lesson);
        return new ResponseEntity<>("lesson_locked", HttpStatus.OK);
    }

    @GetMapping(value = "/unlocklesson")
    Object unlockLesson(@RequestParam Long id) {

        ResponseEntity validityResult = validateSession();
        if (validityResult != null) return validityResult;

        Lesson lesson = lessons.findById(id);
        if (lesson == null)
            return new ResponseEntity<>("lesson_not_found", HttpStatus.NOT_FOUND);

        User student = lesson.getStudent();
        if (student.isDisabled())
            return new ResponseEntity<>("student_disabled", HttpStatus.NOT_FOUND);

        lesson.setLocked(false);
        lessons.save(lesson);
        return new ResponseEntity<>("lesson_unlocked", HttpStatus.OK);
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
    Object getTutorLessons(@RequestParam(value = "tutor") Long tutorId, @RequestParam(required = false, value = "week", defaultValue = "-1") int week) {
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

    private int generateActivationCode() {
        return new Random().nextInt(configService.get().getActivationCodeMaxValue());
    }

    private boolean validateName(String name){
        return !name.matches("['\\/~`\\!@#\\$%\\^&\\*\\(\\)_\\-\\+=\\{\\}\\[\\]\\|;:\"\\<\\>,\\.\\?\\\\]");
    }

    private boolean validatePhone(String phone){
        return phone.matches("^\\s*(?:\\+?(\\d{1,3}))?([-. (]*(\\d{3})[-. )]*)?((\\d{3})[-. ]*(\\d{2,4})(?:[-.x ]*" +
                "(\\d+))?)\\s*$");
    }

    private boolean validateEmail(String email){
        return email.matches("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9]" +
                "(?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?");
    }

    private boolean validatePassword(String password){
        return password.matches("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$");
    }
}
