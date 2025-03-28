package com.yzgeneration.evc.domain.image.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ImageRequest {
    String prefix;

    List<String> imageNames = new ArrayList<>();
}
