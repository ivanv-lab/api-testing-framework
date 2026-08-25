package ru.git.ivanv_lab.framework.json.contract_validator.exceptions;

public class JsonContractException extends RuntimeException{
    public JsonContractException(String message) {
        super(message);
    }

    public JsonContractException(String message, Throwable cause) {
        super(message, cause);
    }
}
