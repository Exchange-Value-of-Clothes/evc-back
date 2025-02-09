package com.yzgeneration.evc.common.implement.infrastructure;

import com.yzgeneration.evc.common.implement.port.UuidHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SystemUuidHolder implements UuidHolder {
    @Override
    public String random() {
        return UUID.randomUUID().toString();
    }
}
