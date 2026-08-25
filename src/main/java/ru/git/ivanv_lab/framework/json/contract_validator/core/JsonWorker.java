package ru.git.ivanv_lab.framework.json.contract_validator.core;

import com.fasterxml.jackson.databind.JsonNode;
import ru.git.ivanv_lab.framework.json.contract_validator.exceptions.JsonContractException;

/**
 * Класс предоставляет методы для навигации по JsonNode: проверку типа
 * и унифицированный поиск элемента в массиве по произвольному полю.
 */
public class JsonWorker {

    /**
     * Проверяет, является ли JsonNode массивом.
     *
     * @param node целевой node для проверки
     */
    public boolean isArray(JsonNode node) {
        return node != null && node.isArray();
    }

    /**
     * Ищет в массиве JsonNode элемент, у которого значение поля fieldName
     * равно expectedValue. Поддерживаются значения типов String, Integer,
     * Long, Double/Float, Boolean; прочие типы сравниваются как текст.
     *
     * @param arrayNode     массив JsonNode, в котором производится поиск
     * @param fieldName     имя поля, по которому ищем
     * @param expectedValue ожидаемое значение поля
     * @return найденный JsonNode или {@code null}, если совпадение не найдено
     * @throws JsonContractException если arrayNode не является массивом
     */
    public JsonNode findNodeByField(JsonNode arrayNode, String fieldName, Object expectedValue) {
        if (!isArray(arrayNode)) {
            throw new JsonContractException(
                    "Переданный JsonNode не является массивом, поиск по полю '" + fieldName + "' невозможен");
        }
        for (JsonNode item : arrayNode) {
            if (JsonNodeMatcher.matches(item.path(fieldName), expectedValue)) {
                return item;
            }
        }
        return null;
    }

    /**
     * Ищет в массиве JsonNode элемент по значению поля "id".
     * Частный случай {@link #findNodeByField(JsonNode, String, Object)}.
     *
     * @param arrayNode массив JsonNode, в котором производится поиск
     * @param id        id для поиска
     * @return найденный JsonNode или {@code null}, если совпадение не найдено
     */
    public JsonNode getNodeById(JsonNode arrayNode, int id) {
        return findNodeByField(arrayNode, "id", id);
    }
}
