package ru.git.ivanv_lab.framework.http.api.fabric.admin_api.settings;

import ru.git.ivanv_lab.framework.data.PropertiesHandler;
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
    private final ApiWorker apiWorker = new ApiWorker(PropertiesHandler.baseUrl,
            new AuthContract(APIs.ADMIN_CONSOLE_API, PropertiesHandler.adminLogin, PropertiesHandler.adminPassword));

    public List<PartnerListGet> getAllPartners() {
        return getAllPartnersByFilter(null);
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
        return getPartnerById(getPartnerIdByName(partnerName));
    }

    public int getPartnerIdByName(String partnerName){
        return getAllPartners()
                .stream()
                .filter(partnerListGet -> partnerListGet.getName().equals(partnerName))
                .findFirst()
                .orElseThrow(()->new JsonContractException("Не удалось найти клиента с именем: "+partnerName))
                .getId();
    }

    public int createPartner(PartnerPost partnerPost) {
        apiWorker.post("/acapi/partners", JsonMapperHolder.convertContractToString(partnerPost))
                .code(200);

        return getPartnerIdByName(partnerPost.getName());
    }

    public PartnerGet updatePartnerById(int id, PartnerPut partnerPut) {
        apiWorker.put("/acapi/partners/"+id, JsonMapperHolder.convertContractToString(partnerPut))
                .code(200);

        return getPartnerById(id);
    }

    public void deletePartnerById(int id) {
        apiWorker.delete("/acapi/partners/"+id).code(200);
    }

    public void deletePartnerByName(String partnerName) {
        deletePartnerById(getPartnerIdByName(partnerName));
    }
}
