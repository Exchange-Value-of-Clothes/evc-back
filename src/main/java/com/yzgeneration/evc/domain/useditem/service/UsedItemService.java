package com.yzgeneration.evc.domain.useditem.service;

import com.yzgeneration.evc.domain.image.implement.UsedItemImageAppender;
import com.yzgeneration.evc.domain.useditem.dto.UsedItemRequest.CreateUsedItem;
import com.yzgeneration.evc.domain.useditem.dto.UsedItemResponse;
import com.yzgeneration.evc.domain.useditem.implement.UsedItemAppender;
import com.yzgeneration.evc.domain.useditem.model.UsedItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsedItemService {
    private final UsedItemAppender usedItemAppender;
    private final UsedItemImageAppender usedItemImageAppender;

    public UsedItemResponse createUsedItem(Long memberId, CreateUsedItem createUsedItem, List<MultipartFile> imageFiles) throws IOException {
        UsedItem usedItem = usedItemAppender.createUsedItem(memberId, createUsedItem);
        List<String> usedItemImageURLs = usedItemImageAppender.createUsedItemImages(usedItem.getId(), imageFiles);

        return UsedItemResponse.of(usedItem, usedItemImageURLs);
    }
}
