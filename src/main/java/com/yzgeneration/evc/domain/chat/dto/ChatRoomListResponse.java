package com.yzgeneration.evc.domain.chat.dto;



import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ChatRoomListResponse {

    private Long chatRoomId;
    private String lastMessage;
    private Long otherMemberId;
    private String otherNickname;
    private String profileImageName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

}
