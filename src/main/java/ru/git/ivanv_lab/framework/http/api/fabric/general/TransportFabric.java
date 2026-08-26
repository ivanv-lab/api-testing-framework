package ru.git.ivanv_lab.framework.http.api.fabric.general;

import ru.git.ivanv_lab.framework.data.PropertiesHandler;
import ru.git.ivanv_lab.framework.http.api.ApiWorker;
import ru.git.ivanv_lab.framework.http.api.authenticator.Api;
import ru.git.ivanv_lab.framework.http.api.authenticator.AuthContract;
import ru.git.ivanv_lab.framework.json.contract_validator.core.JsonContractWorker;
import ru.git.ivanv_lab.framework.json.contract_validator.exceptions.JsonContractException;
import ru.git.ivanv_lab.framework.json.contract_validator.util.JsonMapperHolder;
import ru.git.ivanv_lab.framework.json.contracts.general.Transport;

import java.util.List;

public class TransportFabric {

    private final JsonContractWorker<Transport> transportListWorker
            = new JsonContractWorker<>(Transport.class);
    private final ApiWorker apiWorker = new ApiWorker(PropertiesHandler.baseUrl,
            new AuthContract(Api.ADMIN_CONSOLE_API, PropertiesHandler.adminLogin, PropertiesHandler.adminPassword));

    public List<Transport> getAllTransports() {
        return transportListWorker.getContractArrayFromJsonArray(
                JsonMapperHolder.convertStringToJNode(
                        apiWorker.get("/acapi/transports").code(200).getResponse().asString()
                )
        );
    }

    public Transport getTransportById(int id) {
        return transportListWorker.getContractFromJson(
                JsonMapperHolder.convertStringToJNode(
                        apiWorker.get("/acapi/transports/" + id).code(200).getResponse().asString()
                )
        );
    }

    public Transport getTransportByName(String transportName) {
        return getAllTransports()
                .stream()
                .filter(transport -> transport.getName().equalsIgnoreCase(transportName))
                .findFirst()
                .orElseThrow(() -> new JsonContractException("Не удалось найти транспорт с названием: " + transportName));
    }
}
