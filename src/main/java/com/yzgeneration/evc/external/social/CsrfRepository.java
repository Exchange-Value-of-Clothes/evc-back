package com.yzgeneration.evc.external.social;

public interface CsrfRepository {
    void save(String token);
    void valid(String token);
}
