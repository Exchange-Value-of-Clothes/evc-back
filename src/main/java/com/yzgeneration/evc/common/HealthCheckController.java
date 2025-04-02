package com.yzgeneration.evc.common;

import com.yzgeneration.evc.external.delivery.KakaoMobility;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;

@RestController
@RequiredArgsConstructor
public class HealthCheckController {

    private final KakaoMobility kakaoMobility;

    @GetMapping("/health")
    public String healthCheck() {
        return "OK";
    }

    @GetMapping("/check")
    public void check() throws NoSuchAlgorithmException {
        kakaoMobility.authCheck();
    }

}
