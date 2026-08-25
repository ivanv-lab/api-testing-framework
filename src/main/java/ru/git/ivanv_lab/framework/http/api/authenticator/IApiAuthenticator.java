package ru.git.ivanv_lab.framework.http.api.authenticator;

public interface IApiAuthenticator {

    String getToken(AuthContract authContract);

    String getAuthorization();

    String resetToken(AuthContract authContract);
}
