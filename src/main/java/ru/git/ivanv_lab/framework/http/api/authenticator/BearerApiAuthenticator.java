package ru.git.ivanv_lab.framework.http.api.authenticator;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.ConcurrentHashMap;

import static io.restassured.RestAssured.given;

public class BearerApiAuthenticator implements IApiAuthenticator{

    private final static Logger log = LoggerFactory.getLogger(BearerApiAuthenticator.class);
    private final static ConcurrentHashMap<AuthContract, String> tokenMap = new ConcurrentHashMap<>();

    private ThreadLocal<AuthContract> contract = new ThreadLocal<>();
    private ThreadLocal<Response> response = new ThreadLocal<>();

    private final String baseUrl;
    private final String tokenUrl;

    public BearerApiAuthenticator(String baseUrl, String tokenUrl) {
        this.baseUrl = baseUrl;
        this.tokenUrl = tokenUrl;
    }

    @Override
    public String getToken(AuthContract authContract) {
        if(!tokenMap.containsKey(authContract)) {
            String tempToken = null;

            if (authContract.getApi().equals(Api.BROKER_API)) {
                String toEncode = authContract.getLogin() + ":" + authContract.getPassword();
                tempToken = "Basic " + Base64.getEncoder()
                        .encodeToString(toEncode.getBytes(StandardCharsets.UTF_8));
            } else {
                String url = baseUrl + tokenUrl;

                Response resp = given()
                        .filter(new AllureRestAssured())
                        .contentType(ContentType.URLENC)
                        .formParam("grant_type", "password")
                        .formParam("client_id", authContract.getApi())
                        .formParam("password", authContract.getPassword())
                        .formParam("username", authContract.getPassword())
                        .formParam("scope", "openid")
                        .post(url)
                        .then().extract().response();

                tempToken = resp.body().jsonPath().getString("access_token");
                response.set(resp);
            }

            if (tempToken != null) tokenMap.put(authContract, tempToken);
            else throw new RuntimeException("Ошибка при получении токена.");
        }

        return tokenMap.get(authContract);
    }

    @Override
    public String getAuthorization() {
        return "Bearer ";
    }

    @Override
    public String resetToken(AuthContract authContract) {
        tokenMap.remove(authContract);
        return getToken(authContract);
    }
}
