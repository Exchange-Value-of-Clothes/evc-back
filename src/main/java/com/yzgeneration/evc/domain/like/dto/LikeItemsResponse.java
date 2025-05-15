package com.yzgeneration.evc.domain.like.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yzgeneration.evc.domain.item.enums.TransactionMode;
import com.yzgeneration.evc.domain.item.enums.TransactionStatus;
import com.yzgeneration.evc.domain.item.useditem.enums.ItemStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Builder
public class LikeItemsResponse {

    private Long itemId;

    private String title;

    private int price;

    private TransactionMode transactionMode;

    private TransactionStatus transactionStatus;

    private String imageName;

    @Setter
    private Long likeCount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createAt;
}
