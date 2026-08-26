package ru.git.ivanv_lab.framework.http.api;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.git.ivanv_lab.framework.http.api.authenticator.AuthContract;
import ru.git.ivanv_lab.framework.http.api.authenticator.BasicApiAuthenticator;
import ru.git.ivanv_lab.framework.http.api.authenticator.BearerApiAuthenticator;
import ru.git.ivanv_lab.framework.http.api.authenticator.IApiAuthenticator;

import java.util.function.Supplier;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApiWorker {

    private static final Logger log = LoggerFactory.getLogger(ApiWorker.class);

    private final IApiAuthenticator authenticator;
    private final String baseUrl;

    private final ThreadLocal<AuthContract> contract = new ThreadLocal<>();
    private final ThreadLocal<Response> response = new ThreadLocal<>();

    public ApiWorker(String baseUrl, AuthContract contract) {
        this.baseUrl = baseUrl;
        this.authenticator = new BasicApiAuthenticator(baseUrl);
        this.contract.set(contract);
    }

    public ApiWorker(String baseUrl, String tokenUrl, AuthContract contract) {
        this.baseUrl = baseUrl;
        this.authenticator = new BearerApiAuthenticator(baseUrl, tokenUrl);
        this.contract.set(contract);
    }

    public ApiWorker get(String url) {
        try {
            log.info("GET {}{}", baseUrl, url);
            Supplier<Response> requestSupplier = () -> given()
                    .filter(new AllureRestAssured())
                    .header("Authorization", authenticator.getAuthorization() + authenticator.getToken(contract.get()))
                    .contentType(ContentType.JSON)
                    .get(baseUrl + url)
                    .then()
                    .extract().response();

            Response resp = requestSupplier.get();
            if (resp.getStatusCode() == 401)
                resp = executeWithRetry(requestSupplier);

            response.set(resp);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

        return this;
    }

    public ApiWorker put(String url, String body) {
        try {
            log.info("PUT {}{}\n{}", baseUrl, url, body);
            Supplier<Response> requestSupplier = () -> given()
                    .filter(new AllureRestAssured())
                    .header("Authorization", authenticator.getAuthorization() + authenticator.getToken(contract.get()))
                    .contentType("application/json")
                    .and()
                    .body(body)
                    .when()
                    .put(baseUrl + url)
                    .then()
                    .extract().response();

            Response resp = requestSupplier.get();
            if (resp.getStatusCode() == 401)
                resp = executeWithRetry(requestSupplier);

            response.set(resp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public ApiWorker post(String url, String body) {
        try {
            log.info("POST {}{}\n{}", baseUrl, url, body);
            Supplier<Response> requestSupplier = () -> given()
                    .filter(new AllureRestAssured())
                    .header("Authorization", authenticator.getAuthorization() + authenticator.getToken(contract.get()))
                    .contentType("application/json;charset=utf-8;")
                    .and()
                    .body(body)
                    .when()
                    .post(baseUrl + url)
                    .then()
                    .extract().response();

            Response resp = requestSupplier.get();
            if (resp.getStatusCode() == 401)
                resp = executeWithRetry(requestSupplier);

            response.set(resp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public ApiWorker patch(String url, String body) {
        try {
            log.info("PATCH {}{}\n{}", baseUrl, url, body);
            Supplier<Response> requestSupplier = () -> given()
                    .filter(new AllureRestAssured())
                    .header("Authorization", authenticator.getAuthorization() + authenticator.getToken(contract.get()))
                    .contentType("application/json")
                    .and()
                    .body(body)
                    .when()
                    .patch(baseUrl + url)
                    .then()
                    .extract().response();

            Response resp = requestSupplier.get();
            if (resp.getStatusCode() == 401)
                resp = executeWithRetry(requestSupplier);

            response.set(resp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public ApiWorker delete(String url) {
        try {
            log.info("DELETE {}{}", baseUrl, url);
            Supplier<Response> requestSupplier = () -> given()
                    .filter(new AllureRestAssured())
                    .header("Authorization", authenticator.getAuthorization() + authenticator.getToken(contract.get()))
                    .when()
                    .delete(baseUrl + url)
                    .then()
                    .extract().response();

            Response resp = requestSupplier.get();
            if (resp.getStatusCode() == 401)
                resp = executeWithRetry(requestSupplier);

            response.set(resp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    private Response executeWithRetry(Supplier<Response> requestSupplier) {
        for (int attempt = 1; attempt <= 3; attempt++) {
            authenticator.resetToken(contract.get());
            Response resp = requestSupplier.get();

            if (resp.getStatusCode() != 401)
                return resp;
        }
        throw new RuntimeException("Ошибка авторизации");
    }

    public ApiWorker code(int code) {
        try {
            assertEquals(code, response.get().getStatusCode());
        } catch (AssertionError e) {
            throw new RuntimeException("""
                    Ожидаемый статус: %d
                    Фактический статус: %d
                    """.formatted(code, response.get().getStatusCode()));
        }
        return this;
    }

    public Response getResponse() {
        return response.get();
    }
}
