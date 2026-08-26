package ru.git.ivanv_lab.framework.http.api.fabric;

import ru.git.ivanv_lab.framework.http.api.ApiWorker;
import ru.git.ivanv_lab.framework.json.contract_validator.core.JsonContractWorker;
import ru.git.ivanv_lab.framework.json.contract_validator.exceptions.JsonContractException;
import ru.git.ivanv_lab.framework.json.contract_validator.util.JsonMapperHolder;

import java.util.List;
import java.util.function.Function;

public class Fabric <LIST, DETAIL> {

    private final JsonContractWorker<LIST> listWorker;
    private final JsonContractWorker<DETAIL> detailWorker;
    private final ApiWorker apiWorker;
    private final String basePath;

    private final Function<LIST, Integer> idExtractor;
    private final Function<LIST, String> nameExtractor;

    public Fabric(Class<LIST> listClass, Class<DETAIL> detailClass,
                  String basePath, ApiWorker apiWorker,
                  Function<LIST, Integer> idExtractor,
                  Function<LIST, String> nameExtractor){
        this.listWorker = new JsonContractWorker<>(listClass);
        this.detailWorker = new JsonContractWorker<>(detailClass);
        this.basePath = basePath;
        this.apiWorker = apiWorker;
        this.idExtractor = idExtractor;
        this.nameExtractor = nameExtractor;
    }

    public List<LIST> getAll(){
        return getAllByFilter(null);
    }

    public List<LIST> getAllByFilter(String additionalUrl){
        String url = basePath + additionalUrl;
        return listWorker.getContractArrayFromJsonArray(
                JsonMapperHolder.convertStringToJNode(
                        apiWorker.get(url).code(200).getResponse().asString()
                )
        );
    }

    public DETAIL getById(int id){
        return detailWorker.getContractFromJson(
                JsonMapperHolder.convertStringToJNode(
                        apiWorker.get(basePath+"/"+id).code(200).getResponse().asString()
                )
        );
    }

    public int getIdByName(String name){
        LIST found = getAll().stream()
                .filter(item->nameExtractor.apply(item).equals(name))
                .findFirst()
                .orElseThrow(() -> new JsonContractException("Не найден элемент с именем: " + name));
        return idExtractor.apply(found);
    }

    public int create(Object postContract, String createdName) {
        apiWorker.post(basePath, JsonMapperHolder.convertContractToString(postContract)).code(200);
        return getIdByName(createdName);
    }

    public DETAIL updateById(int id, Object putContract) {
        apiWorker.put(basePath + "/" + id, JsonMapperHolder.convertContractToString(putContract)).code(200);
        return getById(id);
    }

    public void deleteById(int id) {
        apiWorker.delete(basePath + "/" + id).code(200);
    }

    public void deleteByName(String name) {
        deleteById(getIdByName(name));
    }
}
