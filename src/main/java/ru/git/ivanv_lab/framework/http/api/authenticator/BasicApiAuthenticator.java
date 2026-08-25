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

public class BasicApiAuthenticator implements IApiAuthenticator {

    private final static Logger log = LoggerFactory.getLogger(BasicApiAuthenticator.class);
    private final static ConcurrentHashMap<AuthContract, String> tokenMap = new ConcurrentHashMap<>();

    private ThreadLocal<AuthContract> contract = new ThreadLocal<>();
    private ThreadLocal<Response> response = new ThreadLocal<>();

    private final String baseUrl;

    public BasicApiAuthenticator(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public String getToken(AuthContract authContract) {
        if(!tokenMap.containsKey(authContract)) {
            String tempToken = null;
            String url = baseUrl + "/" + authContract.getAPIs().getApiName() + "/auth";

            if (authContract.getAPIs().equals(APIs.BROKER_API)) {
                String toEncode = authContract.getLogin() + ":" + authContract.getPassword();
                tempToken = "Basic " + Base64.getEncoder()
                        .encodeToString(toEncode.getBytes(StandardCharsets.UTF_8));
            } else {
                Response resp = given()
                        .filter(new AllureRestAssured())
                        .body("""
                                {
                                    "username":"%s",
                                    "password":"%s"
                                }
                                """.formatted(authContract.getLogin(), authContract.getPassword()))
                        .contentType(ContentType.JSON)
                        .post(url)
                        .then()
                        .extract().response();

                tempToken = resp.body().jsonPath().getString("token");
                response.set(resp);
            }

            if (tempToken != null) tokenMap.put(authContract, tempToken);
            else throw new RuntimeException("Ошибка при получении токена.");
        }

        return tokenMap.get(authContract);
    }

    @Override
    public String getAuthorization() {
        return "";
    }

    @Override
    public String resetToken(AuthContract authContract) {
        tokenMap.remove(authContract);
        return getToken(authContract);
    }
}
