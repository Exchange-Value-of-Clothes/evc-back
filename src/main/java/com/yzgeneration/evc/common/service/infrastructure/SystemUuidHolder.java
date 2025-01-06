package com.yzgeneration.evc.common.service.infrastructure;

import com.yzgeneration.evc.common.service.port.UuidHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SystemUuidHolder implements UuidHolder {
    @Override
    public String random() {
        return UUID.randomUUID().toString();
    }
}
