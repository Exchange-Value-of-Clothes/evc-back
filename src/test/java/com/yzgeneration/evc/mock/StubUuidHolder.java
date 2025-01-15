package com.yzgeneration.evc.mock;

import com.yzgeneration.evc.common.service.port.UuidHolder;

public class StubUuidHolder implements UuidHolder {
    @Override
    public String random() {
        return "1234";
    }
}
