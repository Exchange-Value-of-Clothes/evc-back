package com.yzgeneration.evc.domain.chat.infrastructure;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;

public interface ChatMessageMongoRepository extends MongoRepository<ChatMessageDocument, String> {

}
