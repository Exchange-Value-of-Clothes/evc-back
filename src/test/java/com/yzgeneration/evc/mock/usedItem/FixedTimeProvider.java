package com.yzgeneration.evc.mock.usedItem;

import com.yzgeneration.evc.common.service.port.TimeProvider;

import java.time.LocalDateTime;

public class FixedTimeProvider implements TimeProvider {
    @Override
    public LocalDateTime now() {
        return LocalDateTime.of(2000, 1, 2, 3, 0, 0, 0);
    }
}
