package com.yzgeneration.evc.domain.like.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LikeResponse {

    private Boolean isLike;

    private Long likeCount;
}
