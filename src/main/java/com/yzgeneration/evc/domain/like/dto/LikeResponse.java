package com.yzgeneration.evc.domain.like.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class LikeResponse {

    private boolean isLike;

    private Long likeCount;
}
