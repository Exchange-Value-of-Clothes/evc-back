package com.yzgeneration.evc.external.social;

public interface CsrfRepository {
    void save(String token, String socialPlatform);
    void valid(String token, String socialPlatform);
}
