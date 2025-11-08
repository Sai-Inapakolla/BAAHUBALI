package com.bank.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public final class PasswordEncoderUtil {
    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();
    private PasswordEncoderUtil() {}
    public static PasswordEncoder encoder() { return ENCODER; }
}


