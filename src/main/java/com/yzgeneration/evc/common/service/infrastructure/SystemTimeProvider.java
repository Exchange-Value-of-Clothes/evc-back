package com.yzgeneration.evc.common.service.infrastructure;

import com.yzgeneration.evc.common.service.port.TimeProvider;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SystemTimeProvider implements TimeProvider {
    @Override
    public LocalDateTime now() {
        return LocalDateTime.now();
    }
}
