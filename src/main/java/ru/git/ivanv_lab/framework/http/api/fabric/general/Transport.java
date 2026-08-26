package ru.git.ivanv_lab.framework.http.api.fabric.general;

public enum Transport {
    SMS("sms"),
    CALL("call"),
    EMAIL("email"),
    PUSH("push"),
    VIBER("viber"),
    WHATSAPP("whatsapp"),
    MAIL_NOTIFY("mail notify"),
    CUSTOM("custom"),
    TELEGRAM("telegram otp");

    private String transportName;

    Transport(String transportName){
        this.transportName = transportName;
    }

    public String getTransportName() {
        return transportName;
    }
}
