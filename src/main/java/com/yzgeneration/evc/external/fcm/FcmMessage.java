package com.yzgeneration.evc.external.fcm;

import lombok.*;

@Getter
@AllArgsConstructor
public class FcmMessage {
    private String token;
    private Notification notification;

    @Getter
    @AllArgsConstructor
    private static class Notification {
        private String title;
        private String body;

        private static Notification create(String title, String body) {
            return new Notification(title, body);
        }
    }

    public static FcmMessage create(String token, String title, String body) {
        return new FcmMessage(token, Notification.create(title, body));
    }
}
