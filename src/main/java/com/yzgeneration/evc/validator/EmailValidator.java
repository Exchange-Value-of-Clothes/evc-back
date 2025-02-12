package com.yzgeneration.evc.validator;

import com.yzgeneration.evc.exception.CustomException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.yzgeneration.evc.exception.ErrorCode.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EmailValidator {
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    public static final Pattern pattern = Pattern.compile(EMAIL_REGEX);

    public static void validate(String email) {
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            throw new CustomException(INVALID_EMAIL);
        }
    }
}
