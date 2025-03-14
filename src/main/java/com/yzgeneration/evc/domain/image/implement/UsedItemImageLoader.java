package com.yzgeneration.evc.domain.image.implement;

import com.yzgeneration.evc.domain.image.service.port.UsedItemImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UsedItemImageLoader {
    private final UsedItemImageRepository usedItemImageRepository;

    public List<String> loadUsedItemImages(Long itemId) {
        return usedItemImageRepository.findImageURLsByUsedItemId(itemId);
    }
}