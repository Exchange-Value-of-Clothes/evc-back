package com.yzgeneration.evc.mock.usedItem;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

public class MockUsedItemImageFile {

    public List<MultipartFile> getImageFiles() {
        List<MultipartFile> imageFiles = new ArrayList<>();
        imageFiles.add(new MockMultipartFile("usedItem1", "usedItem1.png", MediaType.MULTIPART_FORM_DATA_VALUE, "usedItem1".getBytes()));
        imageFiles.add(new MockMultipartFile("usedItem2", "usedItem2.png", MediaType.MULTIPART_FORM_DATA_VALUE, "usedItem2".getBytes()));
        return imageFiles;
    }
}
