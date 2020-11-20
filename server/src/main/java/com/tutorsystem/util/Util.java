package com.tutorsystem.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Util {

    public static String hashPassword(String password) {
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

    // No special characters allowed
    public static boolean validateName(String name){
        return !name.matches("['\\/~`\\!@#\\$%\\^&\\*\\(\\)_\\-\\+=\\{\\}\\[\\]\\|;:\"\\<\\>,\\.\\?\\\\]");
    }

    // All sorts of valid phone numbers
    public static boolean validatePhone(String phone){
        return phone.matches("^\\s*(?:\\+?(\\d{1,3}))?([-. (]*(\\d{3})[-. )]*)?((\\d{3})[-. ]*(\\d{2,4})(?:[-.x ]*" +
                "(\\d+))?)\\s*$");
    }

    // Just standard email format
    public static boolean validateEmail(String email){
        return email.matches("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9]" +
                "(?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?");
    }

    // Minimum eight characters, at least one capital letter
    public static boolean validatePassword(String password){
        return password.matches("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$");
    }
}
