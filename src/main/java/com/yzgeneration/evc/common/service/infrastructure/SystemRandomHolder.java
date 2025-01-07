package com.yzgeneration.evc.common.service.infrastructure;

import com.yzgeneration.evc.common.service.port.RandomHolder;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class SystemRandomHolder implements RandomHolder {

    private final Random random = new Random();

    @Override
    public String randomFourDigit() {
        return String.format("%04d", random.nextInt(10000));
    }
}
