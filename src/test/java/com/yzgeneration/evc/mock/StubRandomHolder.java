package com.yzgeneration.evc.mock;

import com.yzgeneration.evc.common.implement.port.RandomHolder;

public class StubRandomHolder implements RandomHolder {
    @Override
    public String randomFourDigit() {
        return "1234";
    }
}
