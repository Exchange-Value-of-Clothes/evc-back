package com.yzgeneration.evc.domain.image.service;

import com.yzgeneration.evc.domain.image.service.port.UsedItemImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsedItemImageService {
    private final UsedItemImageRepository usedItemImageRepository;
}
