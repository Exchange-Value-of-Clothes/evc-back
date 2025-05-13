package com.yzgeneration.evc.domain.chat.dto;

import com.yzgeneration.evc.domain.item.enums.TransactionMode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatRoomMetaData {
    private TransactionMode transactionMode;
    private String title;
    private int price;
    private String otherPersonProfileName;
}
