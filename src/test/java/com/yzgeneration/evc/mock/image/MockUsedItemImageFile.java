package com.yzgeneration.evc.mock.image;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

public class MockUsedItemImageFile {

    public List<MultipartFile> getImageFiles() {
        List<MultipartFile> imageFiles = new ArrayList<>();
        imageFiles.add(new MockMultipartFile("imageName1", "imageName1.jpg", MediaType.MULTIPART_FORM_DATA_VALUE, "imageName1".getBytes()));
        imageFiles.add(new MockMultipartFile("imageName2", "imageName2.jpg", MediaType.MULTIPART_FORM_DATA_VALUE, "imageName2".getBytes()));
        return imageFiles;
    }
}
