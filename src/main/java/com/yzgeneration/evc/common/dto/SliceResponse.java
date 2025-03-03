package com.yzgeneration.evc.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class SliceResponse <T>{
    private List<T> content;
    private boolean hasNext;
    private int size;
    private int numberOfElements;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime cursor;

    public SliceResponse(Slice<T> slice, LocalDateTime cursor) {
        this.content = slice.getContent();
        this.hasNext = slice.hasNext();
        this.size = slice.getSize();
        this.numberOfElements = slice.getNumberOfElements();
        this.cursor = cursor;
    }

}
