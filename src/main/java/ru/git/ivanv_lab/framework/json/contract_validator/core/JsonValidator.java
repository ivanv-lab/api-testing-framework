package ru.git.ivanv_lab.framework.json.contract_validator.core;

import com.fasterxml.jackson.databind.JsonNode;
import ru.git.ivanv_lab.framework.json.contract_validator.exceptions.JsonContractException;

/**
 * Класс предоставляет унифицированные базовые проверки JsonNode:
 * наличие поля, его отсутствие/null, соответствие ожидаемому значению.
 */
public class JsonValidator {

    /**
     * Проверяет, присутствует ли поле fieldName в node.
     */
    public boolean hasField(JsonNode node, String fieldName) {
        requireNode(node, fieldName);
        return node.has(fieldName);
    }

    /**
     * Проверяет, что поле fieldName присутствует в node и его значение равно JSON null.
     */
    public boolean isFieldNull(JsonNode node, String fieldName) {
        requireNode(node, fieldName);
        return node.path(fieldName).isNull();
    }

    /**
     * Проверяет, что поле fieldName отсутствует в node вовсе (в отличие от isFieldNull).
     */
    public boolean isFieldMissing(JsonNode node, String fieldName) {
        requireNode(node, fieldName);
        return node.path(fieldName).isMissingNode();
    }

    /**
     * Проверяет, что значение поля fieldName равно expected.
     * Поддерживаются значения типов String, Integer, Long, Double/Float, Boolean;
     * прочие типы сравниваются как текст.
     */
    public boolean fieldEquals(JsonNode node, String fieldName, Object expected) {
        requireNode(node, fieldName);
        return JsonNodeMatcher.matches(node.path(fieldName), expected);
    }

    private void requireNode(JsonNode node, String fieldName) {
        if (node == null) {
            throw new JsonContractException("Node не может быть null при проверке поля '" + fieldName + "'");
        }
    }
}
