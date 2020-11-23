package com.tutorsystem.controller;

import com.tutorsystem.email.EmailService;
import com.tutorsystem.model.Lesson;
import com.tutorsystem.model.PasswordReset;
import com.tutorsystem.model.Payment;
import com.tutorsystem.model.User;
import com.tutorsystem.service.*;
import com.tutorsystem.session.UserSessionBean;
import com.tutorsystem.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@RestController
@CrossOrigin(value ="#{environment.cors_url}", allowCredentials = "true")
@RequestMapping("/api")
public class MainController {

//    ############################
//    Data Services
//    ############################

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

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordResetService passwordResetService;

    @GetMapping(value = "/")
    Object index() {
        return "hello";
    }

//    ############################
//    Account and user management endpoints
//    ############################

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

            if (firstname != null && !Util.validateName(firstname)) {
                return new ResponseEntity<>("incorrect_name", HttpStatus.BAD_REQUEST);
            }
            if (lastname != null && !Util.validateName(lastname)) {
                return new ResponseEntity<>("incorrect_name", HttpStatus.BAD_REQUEST);
            }
            if (email != null && !Util.validateEmail(email)) {
                return new ResponseEntity<>("incorrect_email", HttpStatus.BAD_REQUEST);
            }
            if (password != null && !Util.validatePassword(password)) {
                return new ResponseEntity<>("incorrect_password", HttpStatus.BAD_REQUEST);
            }

            User newUser = new User();
            newUser.setFirstName(firstname);
            newUser.setLastName(lastname);
            newUser.setPassword(Util.hashPassword(password));
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

            try {
                emailService.sendmail(newUser.getEmail(), "Welcome to Oktatutor",
                        String.format(
                                "Hello %s,\n" +
                                        "Your oktatutor account has been created successfully.\n\n" +
                                        "If you did not sign up, please contact our support.", newUser.getFirstName()));
            } catch (MessagingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

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


            if (firstname != null && !Util.validateName(firstname)) {
                return new ResponseEntity<>("incorrect_name", HttpStatus.BAD_REQUEST);
            }
            if (lastname != null && !Util.validateName(lastname)) {
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
            newUser.setActivationCode(users.generateActivationCode(configService.get().getActivationCodeMaxValue()));

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


        if (phone != null && !Util.validatePhone(phone)) {
            return new ResponseEntity<>("incorrect_phone", HttpStatus.BAD_REQUEST);
        }
        if (password != null && !Util.validatePassword(password)) {
            return new ResponseEntity<>("incorrect_password", HttpStatus.BAD_REQUEST);
        }
        if (firstname != null && !Util.validateName(firstname)) {
            return new ResponseEntity<>("incorrect_name", HttpStatus.BAD_REQUEST);
        }
        if (lastname != null && !Util.validateName(lastname)) {
            return new ResponseEntity<>("incorrect_name", HttpStatus.BAD_REQUEST);
        }
        if (email != null && !Util.validateEmail(email)) {
            return new ResponseEntity<>("incorrect_email", HttpStatus.BAD_REQUEST);
        }

        User user = userSessionBean.getUser();
        user.setFirstName(firstname);
        user.setLastName(lastname);
        user.setEmail(email);
        user.setPhone(phone);
        if (password != null) {
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


        if (phone != null && !Util.validatePhone(phone)) {
            return new ResponseEntity<>("incorrect_phone", HttpStatus.BAD_REQUEST);
        }
        if (password != null && !Util.validatePassword(password)) {
            return new ResponseEntity<>("incorrect_password", HttpStatus.BAD_REQUEST);
        }
        if (firstname != null && !Util.validateName(firstname)) {
            return new ResponseEntity<>("incorrect_name", HttpStatus.BAD_REQUEST);
        }
        if (lastname != null && !Util.validateName(lastname)) {
            return new ResponseEntity<>("incorrect_name", HttpStatus.BAD_REQUEST);
        }
        if (email != null && !Util.validateEmail(email)) {
            return new ResponseEntity<>("incorrect_email", HttpStatus.BAD_REQUEST);
        }

        User user = userSessionBean.getUser();
        user.setFirstName(firstname);
        user.setLastName(lastname);
        user.setEmail(email);
        user.setPhone(phone);
        user.setRate(rate);
        user.setInfo(info);
        if (password != null) {
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

            if (password != null && !Util.validatePassword(password)) {
                return new ResponseEntity<>("incorrect_password", HttpStatus.BAD_REQUEST);
            }
            if (email != null && !Util.validateEmail(email)) {
                return new ResponseEntity<>("incorrect_email", HttpStatus.BAD_REQUEST);
            }

            user.setPassword(Util.hashPassword(password));
            user.setEmail(email);
            user.setActivated(true);
            users.save(user);

            this.userSessionBean.setUser(user);

            try {
                emailService.sendmail(user.getEmail(), "Welcome to Oktatutor",
                        String.format("Hello %s,\n" +
                                "Your oktatutor account has been created successfully.\n\n" +
                                "If you did not sign up, please contact our support.", user.getFirstName()));
            } catch (MessagingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return user;

        } else return new ResponseEntity<>("signup_closed", HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/login")
    Object postRegister(@RequestParam String email,
                        @RequestParam String password) {
        if (configService.get().isLoginOpen()) {
            User user = users.findByEmailAndPassword(email, Util.hashPassword(password));
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

        try {
            emailService.sendmail(this.userSessionBean.getUser().getEmail(), "Oktatutor Account Disabled",
                    "Hello %s,\n" +
                            "Your Oktatutor account has been deactivated."
            );
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>("account_deactivated_successfully", HttpStatus.OK);
    }

    @GetMapping(value = "/requestpasswordreset")
    Object requestPasswordReset(@RequestParam String email) {
        User user = this.users.findByEmail(email);

        if (user != null) {
            PasswordReset passwordReset = new PasswordReset();
            passwordReset.setDate(new Date());
            passwordReset.setUser(user);
            user.setPasswordReset(passwordReset);
            passwordReset.setResetCode(passwordResetService.generateResetCode());
            passwordResetService.save(passwordReset);

            try {
                emailService.sendmail(email, "Oktatutor Password Reset",
                        String.format(
                                "Hello %s,\n" +
                                        "Your password reset code is <strong>%s</strong>\n" +
                                        "\n" +
                                        "If you did not request password reset, please contact our support."
                                ,
                                user.getFirstName(), passwordReset.getResetCode())
                );
            } catch (MessagingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return new ResponseEntity<>("reset_requested_successfully", HttpStatus.OK);
    }

    @GetMapping(value = "/passwordreset")
    Object requestPasswordReset(@RequestParam int code, @RequestParam String password) {

        PasswordReset passwordReset = passwordResetService.findByCode(code);
        if (passwordReset != null) {
            if (passwordReset.isValid() && passwordReset.getResetCode() == code) {
                if (!Util.validatePassword(password)) {
                    return new ResponseEntity<>("incorrect_password", HttpStatus.BAD_REQUEST);
                }
                User user = passwordReset.getUser();
                if (user.getPasswordReset() != null) {
                    PasswordReset passwordReset1 = user.getPasswordReset();
                    user.setPasswordReset(null);
                    users.save(user);
                    passwordResetService.remove(passwordReset1);
                }
                user.setPassword(Util.hashPassword(password));
                user.setPasswordReset(null);
                users.save(user);
                passwordResetService.remove(passwordReset);
                try {
                    emailService.sendmail(user.getEmail(), "Oktatutor Password Reset",
                            String.format("Hello %s,\n" +
                                            "You have successfully changed your oktatutor password.\n\n" +
                                            "If you did not request password reset, please contact our support.",
                                    user.getFirstName()));
                } catch (MessagingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return new ResponseEntity<>("successfully_reset_password", HttpStatus.OK);
            }
        }

        return new ResponseEntity<>("password_reset_failed", HttpStatus.FORBIDDEN);
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

        try {
            emailService.sendmail(student.getEmail(), "Oktatutor Account Deactivated",
                    String.format("Hello %s,\n" +
                            "Your oktatutor account has been deactivated.", student.getFirstName()));
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


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

//    ############################
//    Payment management endpoints
//    ############################

    @GetMapping(value = "/savepayment")
    Object postPayment(@RequestParam(required = false, defaultValue = "-1") Long id,
                       @RequestParam(name = "tutor") Long tutorId,
                       @RequestParam(name = "student") Long studentId,
                       @RequestParam int amount,
                       @RequestParam(required = false) String comment) {
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
            payment.setDate(new Date());
        }
        payment.setStudent(student);
        payment.setTutor(tutor);
        payment.setAmount(amount);
        payment.setComment(comment);
        payments.save(payment);

        try {
            emailService.sendmail(student.getEmail(),
                    "Oktatutor Payment Registered",
                    String.format("Hello %s,\n" +
                                    "Your tutor %s has registered a payment of <strong>&pound;" + amount + "</strong> on " +
                                    "oktatutor.",
                            student.getFirstName(),
                            tutor.getFirstName()));

            emailService.sendmail(tutor.getEmail(),
                    "Oktatutor Payment Registered",
                    String.format("Hello %s,\n" +
                                    "Your have registered a payment of <strong>&pound;" + amount + "</strong> for %s on " +
                                    "oktatutor.",
                            tutor.getFirstName(),
                            student.getFirstName()));
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

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

//    ############################
//    Lesson management endpoints
//    ############################

    @GetMapping(value = "/savelesson")
    Object postLesson(@RequestParam(required = false, defaultValue = "-1") Long id,
                      @RequestParam(name = "student") Long studentId,
                      @RequestParam String date,
                      @RequestParam String start,
                      @RequestParam String end,
                      @RequestParam String location,
                      @RequestParam(required = false) String comment) {

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
        lesson.setComment(comment);

        lessons.save(lesson);


        SimpleDateFormat dateFormatDateOnly = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat dateFormatTimeOnly = new SimpleDateFormat("HH:mm");

        try {
            emailService.sendmail(student.getEmail(),
                    "New Lesson on Oktatutor",
                    String.format("Hello %s,\n" +
                                    "Your tutor %s has just added a lesson on oktatutor on %s from %s to %s. \n" +
                                    "Location:  %s.\n\n" +
                                    "%s",
                            student.getFirstName(),
                            userSessionBean.getUser().getFirstName(),
                            dateFormatDateOnly.format(lesson.getStart()),
                            dateFormatTimeOnly.format(lesson.getStart()),
                            dateFormatTimeOnly.format(lesson.getEnd()),
                            lesson.getLocation(),
                            lesson.getComment() != null ? "\n<strong>Message </strong>\n" + lesson.getComment() : ""));

            emailService.sendmail(userSessionBean.getUser().getEmail(),
                    "New Lesson on Oktatutor",
                    String.format("Hello %s,\n" +
                                    "Your have just added a lesson for %s on %s from %s to %s. \n" +
                                    "Location:  %s.\n\n" +
                                    "%s",
                            userSessionBean.getUser().getFirstName(),
                            student.getFirstName(),
                            dateFormatDateOnly.format(lesson.getStart()),
                            dateFormatTimeOnly.format(lesson.getStart()),
                            dateFormatTimeOnly.format(lesson.getEnd()),
                            lesson.getLocation(),
                            lesson.getComment() != null ? "<strong>Message </strong>\n" + lesson.getComment() : ""));

        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    @GetMapping(value = "/deletelesson")
    Object deleteLessonById(@RequestParam Long id) {

        ResponseEntity validityResult = validateSession();
        if (validityResult != null) return validityResult;

        Lesson lesson = lessons.findById(id);
        if (lesson == null)
            return new ResponseEntity<>("lesson_not_found", HttpStatus.NOT_FOUND);
        lessons.deleteById(lesson.getId());

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

//    ############################
//    Mail management endpoints
//    ############################

    @GetMapping(value = "/testmail")
    void sendTestMail() {
        try {
            emailService.sendmail("daniel.szabo99@outlook.com", "Message from Oktatutor",
                    "Hello dear,\n what is up?");
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
    }

//    ############################
//    Utility functions
//    ############################

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

}
