package com.example.userservice.util;

import com.example.userservice.exception.common.InternalServerErrorException;
import com.example.userservice.exception.common.ServiceIllegalStateException;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordEncoder {

    public static String encrypt(String password, String salt) {
        String text = password + salt;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(text.getBytes());
            return bytesToHex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new ServiceIllegalStateException();
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }
}
