package com.yzgeneration.evc.domain.point.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yzgeneration.evc.domain.point.enums.PointChargeType;
import com.yzgeneration.evc.validator.EnumValidator;
import com.yzgeneration.evc.validator.Validatable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PointChargeOrderRequest implements Validatable {


    private String pointChargeType;

    @JsonCreator
    public PointChargeOrderRequest(@JsonProperty("pointChargeType") String pointChargeType) {
        this.pointChargeType = pointChargeType;
        valid();
    }

    @Override
    public void valid() {
        EnumValidator.validate(PointChargeType.class, "pointChargeType", pointChargeType);
    }
}
