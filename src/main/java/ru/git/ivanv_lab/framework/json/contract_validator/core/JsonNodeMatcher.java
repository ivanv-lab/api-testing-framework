package ru.git.ivanv_lab.framework.json.contract_validator.core;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Внутренний вспомогательный класс для сравнения значения JsonNode с ожидаемым
 * Java-значением. Используется {@link JsonWorker} и {@link JsonValidator}, чтобы
 * не дублировать логику сравнения между поиском и проверками.
 * Не является частью публичного API фреймворка.
 */
final class JsonNodeMatcher {

    private JsonNodeMatcher() {
    }

    static boolean matches(JsonNode fieldNode, Object expectedValue) {
        if (fieldNode == null || fieldNode.isMissingNode()) {
            return expectedValue == null;
        }
        if (expectedValue == null) {
            return fieldNode.isNull();
        }
        if (expectedValue instanceof Integer) {
            return fieldNode.canConvertToInt() && fieldNode.asInt() == (Integer) expectedValue;
        }
        if (expectedValue instanceof Long) {
            return fieldNode.canConvertToLong() && fieldNode.asLong() == (Long) expectedValue;
        }
        if (expectedValue instanceof Double || expectedValue instanceof Float) {
            return fieldNode.isNumber() && fieldNode.asDouble() == ((Number) expectedValue).doubleValue();
        }
        if (expectedValue instanceof Boolean) {
            return fieldNode.isBoolean() && fieldNode.asBoolean() == (Boolean) expectedValue;
        }
        return fieldNode.asText().equals(String.valueOf(expectedValue));
    }
}
