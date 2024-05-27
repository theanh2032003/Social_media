package com.example.demo.encryption;

import org.springframework.stereotype.Component;

@Component
public class PasswordEncryption {
    private static final int SHIFT_AMOUNT = 3;


    public String encryptPassword(String password) {
        StringBuilder encryptedPassword = new StringBuilder();

        // Đảo ngược mật khẩu
        String reversedPassword = new StringBuilder(password).reverse().toString();

        // Dịch trái các kí tự đi SHIFT_AMOUNT đơn vị trong bảng mã ASCII
        for (char c : reversedPassword.toCharArray()) {
            int shiftedValue = c + SHIFT_AMOUNT;
            encryptedPassword.append((char) shiftedValue);
        }

        return encryptedPassword.toString();
    }


    public String decryptPassword(String encryptedPassword) {
        StringBuilder decryptedPassword = new StringBuilder();

        // Dịch phải các kí tự đi SHIFT_AMOUNT đơn vị trong bảng mã ASCII
        for (char c : encryptedPassword.toCharArray()) {
            int shiftedValue = c - SHIFT_AMOUNT;
            decryptedPassword.append((char) shiftedValue);
        }

        // Đảo ngược mật khẩu đã giải mã
        String originalPassword = decryptedPassword.reverse().toString();

        return originalPassword;
    }
}
