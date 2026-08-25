package ru.git.ivanv_lab.framework.json.contract_validator.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Держатель единого {@link ObjectMapper} для всего фреймворка.
 * Вынесен отдельно, чтобы конфигурация маппера (например, поведение на
 * незнакомых полях ответа API) настраивалась в одном месте, а не в каждом
 * {@code JsonContractWorker} по отдельности.
 */
public class JsonMapperHolder {

    public static final ObjectMapper MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public static JsonNode convertStringToJNode(String stringToConvert){
        try{
            return MAPPER.readTree(stringToConvert);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка при конвертации строки в JsonNode: "+e.getMessage());
        }
    }

    public static String convertContractToString(Object contract){
        try{
            return MAPPER.writeValueAsString(contract);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка при конвертации JsonNode в строку: "+e.getMessage());
        }
    }

    private JsonMapperHolder() {
    }
}
