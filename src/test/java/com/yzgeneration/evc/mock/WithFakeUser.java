package com.yzgeneration.evc.mock;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithFakeUserSecurityContextFactory.class)
public @interface WithFakeUser {

}
