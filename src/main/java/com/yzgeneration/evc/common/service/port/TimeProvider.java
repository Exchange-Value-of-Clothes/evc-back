package com.yzgeneration.evc.common.service.port;

import java.time.LocalDateTime;

public interface TimeProvider {
    LocalDateTime now();
}
