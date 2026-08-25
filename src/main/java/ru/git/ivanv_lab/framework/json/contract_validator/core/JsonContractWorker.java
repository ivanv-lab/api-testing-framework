package ru.git.ivanv_lab.framework.json.contract_validator.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import ru.git.ivanv_lab.framework.json.contract_validator.exceptions.JsonContractException;
import ru.git.ivanv_lab.framework.json.contract_validator.util.JsonMapperHolder;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Класс предоставляет методы для конвертации JsonNode в контрактную модель.
 *
 * @param <T> целевой класс-контракт, во что конвертируем JsonNode
 */
public class JsonContractWorker<T> {

    private final Class<T> targetContract;
    private final JsonWorker jsonWorker = new JsonWorker();

    /**
     * Сравнивает фактический и ожидаемый экземпляры контрактной модели.
     * Использует recursive comparison, чтобы контрактные тесты сравнивали объект с объектом,
     * а не отдельные JSON-строки или поля.
     *
     * @param actualContract   фактическая контрактная модель
     * @param expectedContract ожидаемая контрактная модель
     */
    public static <T> void assertContract(T actualContract, T expectedContract) {
        assertThat(actualContract)
                .usingRecursiveComparison()
                .isEqualTo(expectedContract)
                .withFailMessage("""
                        Ошибка эквивалентности:
                        Ожидаемый результат: %s
                        Фактический результат: %s
                        """.formatted(actualContract, expectedContract));
    }

    /**
     * Сравнивает контрактные модели, исключая поля по regex-путям.
     * Используется для контрактов, где часть служебных полей проверяется отдельными assertions.
     *
     * @param actualContract               фактическая контрактная модель
     * @param expectedContract             ожидаемая контрактная модель
     * @param ignoredFieldsMatchingRegexes regex-пути полей, которые нужно исключить из сравнения
     */
    public static <T> void assertContract(T actualContract, T expectedContract, String... ignoredFieldsMatchingRegexes) {
        assertThat(actualContract)
                .usingRecursiveComparison()
                .ignoringFieldsMatchingRegexes(ignoredFieldsMatchingRegexes)
                .isEqualTo(expectedContract);
    }

    /**
     * @param targetContract целевой класс для конвертации
     */
    public JsonContractWorker(Class<T> targetContract) {
        this.targetContract = targetContract;
    }

    /**
     * Возвращает контрактную модель из JsonNode.
     * Маппинг выполняется напрямую по дереву ({@code treeToValue}), без промежуточного
     * приведения node к тексту.
     *
     * @param node JsonNode, представляющий объект контракта
     * @throws JsonContractException если маппинг не удался
     */
    public T getContractFromJson(JsonNode node) {
        try {
            return JsonMapperHolder.MAPPER.treeToValue(node, targetContract);
        } catch (JsonProcessingException e) {
            throw new JsonContractException(
                    "Не удалось смаппить JsonNode в контракт " + targetContract.getSimpleName(), e);
        }
    }

    /**
     * Возвращает контрактную модель из массива JsonNode по id.
     * Если переданный node не является массивом, пытается смаппить его напрямую
     * как единичный объект контракта.
     *
     * @param node массив JsonNode (или единичный объект)
     * @param id   id искомого элемента в массиве
     * @throws JsonContractException если node — массив, но элемент с таким id не найден,
     *                               либо если маппинг не удался
     */
    public T getContractFromJsonArray(JsonNode node, int id) {
        if (jsonWorker.isArray(node)) {
            JsonNode found = jsonWorker.getNodeById(node, id);
            if (found == null) {
                throw new JsonContractException(
                        "В массиве не найден элемент с id=" + id + " для контракта "
                        + targetContract.getSimpleName());
            }
            return getContractFromJson(found);
        }
        return getContractFromJson(node);
    }

    public List<T> getContractArrayFromJsonArray(JsonNode arrayNode) {
        List<T> objectList = new ArrayList<>();

        arrayNode = arrayNode.elements().next();
        if (jsonWorker.isArray(arrayNode)) {
            for (JsonNode node : arrayNode) {
                objectList.add(getContractFromJson(node));
            }
            return objectList;
        }

        throw new JsonContractException(
                "Не удалось конвертировать массив JsonNode в коллекцию контрактов " + targetContract.getSimpleName());
    }
}
