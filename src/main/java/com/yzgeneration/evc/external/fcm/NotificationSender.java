package com.yzgeneration.evc.external.fcm;

public interface NotificationSender {

    void send(String deviceToken, String title, String body);
}
