package ru.git.ivanv_lab.framework.http.api.authenticator;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@AllArgsConstructor
@Getter
public class AuthContract {
    private final APIs APIs;
    private final String login;
    private final String password;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthContract that = (AuthContract) o;
        return Objects.equals(APIs, that.APIs) &&
               Objects.equals(login, that.login) &&
               Objects.equals(password, that.password);
    }

    @Override
    public int hashCode(){
        return Objects.hash(APIs, login, password);
    }
}
