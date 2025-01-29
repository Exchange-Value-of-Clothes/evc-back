package com.yzgeneration.evc.image.infrastructure.entity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@SuperBuilder
@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EntityListeners(value = {AuditingEntityListener.class})
public abstract class BaseImageEntity {
    //[notice] S3 버킷 연결하며 수정될 예정 (지금은 뼈대 단계)
    private String imageName;
    private String imageURL;
}
