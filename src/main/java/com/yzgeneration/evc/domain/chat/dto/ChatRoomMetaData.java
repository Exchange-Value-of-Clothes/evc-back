package com.yzgeneration.evc.domain.chat.dto;

import com.yzgeneration.evc.domain.item.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatRoomMetaData {
    private TransactionType transactionType;
    private String title;
    private int price;
    private String otherPersonProfileUrl;
}
