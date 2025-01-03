package com.yzgeneration.evc.useditem.service;

import com.yzgeneration.evc.useditem.service.port.UsedItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsedItemRegisterService {
    private final UsedItemRepository usedItemRepository;
}
