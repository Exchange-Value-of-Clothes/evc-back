package com.yzgeneration.evc.validator;

import com.yzgeneration.evc.exception.CustomException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.yzgeneration.evc.exception.ErrorCode.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)

public final class PasswordValidator {
    private static final String PASSWORD_REGEX = "^(?!.*\\s).{8,}$";
    private static final Pattern pattern = Pattern.compile(PASSWORD_REGEX);

    public static void validate(String password) {
        Matcher matcher = pattern.matcher(password);
        if (matcher.matches()) return;
        throw new CustomException(INVALID_PASSWORD);
    }
}
