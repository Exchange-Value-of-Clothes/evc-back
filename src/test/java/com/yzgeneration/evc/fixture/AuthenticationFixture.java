package com.yzgeneration.evc.fixture;


import static com.yzgeneration.evc.authentication.dto.AuthenticationRequest.*;

public abstract class AuthenticationFixture extends Fixture{

    public static LoginRequest fixLoginRequest() {
        return fixtureMonkey.giveMeBuilder(LoginRequest.class)
                .set("email", "ssar@naver.com")
                .set("password", "12345678")
                .sample();
    }

}
