package com.yzgeneration.evc.common.validator;

import com.yzgeneration.evc.common.exception.CustomException;
import com.yzgeneration.evc.common.exception.ErrorCode;

public abstract class EnumValidator {

    public static void validate(Class<? extends Validatable> targetClass, String targetFieldName, Class<? extends Enum<?>> enumClass, String targetFiledValue) {
        Enum<?>[] enumConstants = enumClass.getEnumConstants();
        if (enumConstants == null) {
            throw new CustomException(ErrorCode.INVALID_ENUM, targetClass.getSimpleName()+" dto 에서 " + targetFieldName + " 는 '" + targetFiledValue + "'일 수 없습니다.");
        }
    }

}
