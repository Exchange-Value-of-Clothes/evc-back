package com.yzgeneration.evc.common.implement.infrastructure;

import com.yzgeneration.evc.common.implement.port.TimeProvider;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SystemTimeProvider implements TimeProvider {
    @Override
    public LocalDateTime now() {
        return LocalDateTime.now();
    }
}
