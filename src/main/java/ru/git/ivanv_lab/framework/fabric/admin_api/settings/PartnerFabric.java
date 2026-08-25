package ru.git.ivanv_lab.framework.fabric.admin_api.settings;

import com.fasterxml.jackson.databind.JsonNode;
import io.qameta.allure.Step;
import ru.git.ivanv_lab.framework.http.api.ApiWorker;
import ru.git.ivanv_lab.framework.http.api.authenticator.APIs;
import ru.git.ivanv_lab.framework.http.api.authenticator.AuthContract;
import ru.git.ivanv_lab.framework.json.contract_validator.core.JsonContractWorker;
import ru.git.ivanv_lab.framework.json.contract_validator.exceptions.JsonContractException;
import ru.git.ivanv_lab.framework.json.contract_validator.util.JsonMapperHolder;
import ru.git.ivanv_lab.framework.json.contracts.admin_api.partners.PartnerGet;
import ru.git.ivanv_lab.framework.json.contracts.admin_api.partners.PartnerListGet;
import ru.git.ivanv_lab.framework.json.contracts.admin_api.partners.PartnerPost;
import ru.git.ivanv_lab.framework.json.contracts.admin_api.partners.PartnerPut;

import java.util.List;

public class PartnerFabric {

    private final JsonContractWorker<PartnerListGet> partnerListWorker
            = new JsonContractWorker<>(PartnerListGet.class);
    private final JsonContractWorker<PartnerGet> partnerWorker
            = new JsonContractWorker<>(PartnerGet.class);
    private final ApiWorker apiWorker = new ApiWorker("http://192.168.128.250",
            new AuthContract(APIs.ADMIN_CONSOLE_API, "admin@admin.com", "Admin"));

    public List<PartnerListGet> getAllPartners() {
        return partnerListWorker.getContractArrayFromJsonArray(
                JsonMapperHolder.convertStringToJNode(
                        apiWorker.get("/acapi/partners").code(200).getResponse().asString()
                )
        );
    }

    public List<PartnerListGet> getAllPartnersByFilter(String additionalUrl){
        return partnerListWorker.getContractArrayFromJsonArray(
                JsonMapperHolder.convertStringToJNode(
                        apiWorker.get("/acapi/partners"+additionalUrl).code(200).getResponse().asString()
                )
        );
    }

    public PartnerGet getPartnerById(int id) {
        return partnerWorker.getContractFromJson(
                JsonMapperHolder.convertStringToJNode(
                        apiWorker.get("/acapi/partners/" + id).code(200).getResponse().asString()
                )
        );
    }

    public PartnerGet getPartnerByName(String partnerName) {
        int partnerId = getAllPartners()
                .stream()
                .filter(partnerListGet -> partnerListGet.getName().equalsIgnoreCase(partnerName))
                .findFirst()
                .orElseThrow(()->new JsonContractException("Не удалось найти клиента с именем: "+partnerName))
                .getId();

//        return partnerWorker.getContractFromJson(
//                JsonMapperHolder.convertStringToJNode(
//                        apiWorker.get("/acapi/partners/"+partnerId).code(200).getResponse().asString()
//                )
//        );

        JsonNode json = JsonMapperHolder.convertStringToJNode(
                apiWorker.get("/acapi/partners/"+partnerId).code(200).getResponse().asString()
        );
        PartnerGet partnerGet = partnerWorker.getContractFromJson(json);
        return partnerGet;
    }

    public int createPartner(PartnerPost partnerPost) {
        apiWorker.post("/acapi/partners", JsonMapperHolder.convertContractToString(partnerPost))
                .code(200);

        return getAllPartners()
                .stream()
                .filter(partnerListGet -> partnerListGet.getName().equalsIgnoreCase(partnerPost.getName()))
                .findFirst()
                .orElseThrow(()->new JsonContractException("Не удалось найти клиента с именем: "+partnerPost.getName()))
                .getId();
    }

    public PartnerGet updatePartnerById(int id, PartnerPut partnerPut) {
        apiWorker.put("/acapi/partners/"+id, JsonMapperHolder.convertContractToString(partnerPut))
                .code(200);

        return getPartnerByName(partnerPut.getName());
    }

    public void deletePartnerById(int id) {
        apiWorker.delete("/acapi/partners/"+id).code(200);
    }

    public void deletePartnerByName(String partnerName) {
        int partnerId = getAllPartners()
                .stream()
                .filter(partnerListGet -> partnerListGet.getName().equalsIgnoreCase(partnerName))
                .findFirst()
                .orElseThrow(()->new JsonContractException("Не удалось найти клиента с именем: "+partnerName))
                .getId();

        apiWorker.delete("/acapi/partners/"+partnerId).code(200);
    }
}
