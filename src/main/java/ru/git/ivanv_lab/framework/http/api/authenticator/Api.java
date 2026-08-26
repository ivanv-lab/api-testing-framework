package ru.git.ivanv_lab.framework.http.api.authenticator;

public enum Api {
    BROKER_API("broker-api"),
    ADMIN_CONSOLE_API("acapi"),
    LK_API("lk-api"),
    CDP_API("cdp-api"),
    IMSI_API("imsi-api"),
    CALL_CENTER_API("cc-api");

    private String apiName;

    Api(String apiName){
        this.apiName = apiName;
    }

    public String getApiName() {
        return apiName;
    }
}
