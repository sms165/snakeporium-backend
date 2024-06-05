package com.snakeporium_backend.utils;

import java.security.SecureRandom;
import java.util.Base64;

public class SecretKeyGenerator {

    public static String generateSecretKey() {
        // Generate a secure random byte array
        byte[] secretKeyBytes = new byte[32]; // You can adjust the length as needed
        new SecureRandom().nextBytes(secretKeyBytes);

        // Encode the byte array to a Base64 string
        return Base64.getEncoder().encodeToString(secretKeyBytes);
    }
}