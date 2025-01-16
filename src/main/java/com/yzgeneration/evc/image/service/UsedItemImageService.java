package com.yzgeneration.evc.image.service;

import com.yzgeneration.evc.image.service.port.UsedItemImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsedItemImageService {
    private final UsedItemImageRepository usedItemImageRepository;
}
