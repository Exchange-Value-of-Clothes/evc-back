package com.yzgeneration.evc.common.validator;

import com.yzgeneration.evc.common.exception.CustomException;
import com.yzgeneration.evc.common.exception.ErrorCode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.yzgeneration.evc.common.exception.ErrorCode.*;

public class EmailValidator {
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    public static final Pattern pattern = Pattern.compile(EMAIL_REGEX);

    public static void validate(String email) {
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            throw new CustomException(EMAIL_INCORRECT_FORMAT);
        }
    }
}
