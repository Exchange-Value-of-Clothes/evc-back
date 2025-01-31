package com.yzgeneration.evc.common.validator;

import com.yzgeneration.evc.common.exception.CustomException;
import com.yzgeneration.evc.common.exception.ErrorCode;

import java.util.Arrays;

public abstract class EnumValidator {

    public static void validate(Class<? extends Enum<?>> enumClass, String targetFieldName, String targetFieldValue) {
        Enum<?>[] enumConstants = enumClass.getEnumConstants();
        boolean isValid = Arrays.stream(enumConstants)
                .map(Enum::name)
                .anyMatch(name -> name.equalsIgnoreCase(targetFieldValue));

        if (!isValid) {
            throw new CustomException(ErrorCode.INVALID_ENUM, targetFieldName + " 는 '" + targetFieldValue + "' 일 수 없습니다.");
        }
    }

}
