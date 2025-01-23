package com.yzgeneration.evc.common.validator;

import com.yzgeneration.evc.common.exception.CustomException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.yzgeneration.evc.common.exception.ErrorCode.*;

public class PasswordValidator {
    private static final String PASSWORD_REGEX = "^(?!.*\\s).{8,}$";
    private static final Pattern pattern = Pattern.compile(PASSWORD_REGEX);

    public static void validate(String password) {
        Matcher matcher = pattern.matcher(password);
        if (!matcher.matches()) {
            throw new CustomException(PASSWORD_INCORRECT_FORMAT);
        }
    }
}
