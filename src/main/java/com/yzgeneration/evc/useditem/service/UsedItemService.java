package com.yzgeneration.evc.useditem.service;

import com.yzgeneration.evc.image.service.port.UsedItemImageRepository;
import com.yzgeneration.evc.useditem.dto.UsedItemRequest.CreateUsedItem;
import com.yzgeneration.evc.useditem.dto.UsedItemResponse;
import com.yzgeneration.evc.useditem.model.UsedItem;
import com.yzgeneration.evc.useditem.service.port.UsedItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsedItemService {
    private final UsedItemRepository usedItemRepository;
    private final UsedItemImageRepository usedItemImageRepository;

    public UsedItemResponse createUsedITem(CreateUsedItem createUsedItem, List<MultipartFile> imageFiles) {
        UsedItem usedItem = usedItemRepository.save(UsedItem.create(createUsedItem));
        // [notice] 이미지 생성 로직 추가 & return 값 변경
        return UsedItemResponse.of(usedItem, new ArrayList<>());
    }
}
