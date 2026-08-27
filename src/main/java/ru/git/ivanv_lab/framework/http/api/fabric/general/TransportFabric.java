package ru.git.ivanv_lab.framework.http.api.fabric.general;

import ru.git.ivanv_lab.framework.data.PropertiesHandler;
import ru.git.ivanv_lab.framework.http.api.ApiWorker;
import ru.git.ivanv_lab.framework.http.api.authenticator.Api;
import ru.git.ivanv_lab.framework.http.api.authenticator.AuthContract;
import ru.git.ivanv_lab.framework.json.contract_validator.core.JsonContractWorker;
import ru.git.ivanv_lab.framework.json.contract_validator.exceptions.JsonContractException;
import ru.git.ivanv_lab.framework.json.contract_validator.util.JsonMapperHolder;
import ru.git.ivanv_lab.framework.json.contracts.general.Transport;

import java.util.*;

public class TransportFabric {

    private final JsonContractWorker<Transport> transportListWorker
            = new JsonContractWorker<>(Transport.class);
    private final ApiWorker apiWorker = new ApiWorker(PropertiesHandler.baseUrl,
            new AuthContract(Api.ADMIN_CONSOLE_API, PropertiesHandler.adminLogin, PropertiesHandler.adminPassword));

    private static final Set<Transport> transports=new HashSet<>();

    public List<Transport> getAllTransports() {
        if(transports.isEmpty()) {
            List<Transport> transportList = transportListWorker.getContractArrayFromJsonArray(
                    JsonMapperHolder.convertStringToJNode(
                            apiWorker.get("/acapi/transports").code(200).getResponse().asString()
                    )
            );

            transports.addAll(transportList);
        }

        return transports.stream().toList();
    }

    public Transport getTransportById(int id) {
        if(transports.isEmpty()) {
            return transportListWorker.getContractFromJson(
                    JsonMapperHolder.convertStringToJNode(
                            apiWorker.get("/acapi/transports/" + id).code(200).getResponse().asString()
                    )
            );
        }

        return transports.stream().filter(transport -> transport.getId()==id)
                .findFirst().orElseThrow(()->new JsonContractException("Не удалось найти транспорт с id: "+id));
    }

    public Transport getTransportByName(String transportName) {
        if(transports.isEmpty()) {
            return getAllTransports()
                    .stream()
                    .filter(transport -> transport.getName().equalsIgnoreCase(transportName))
                    .findFirst()
                    .orElseThrow(() -> new JsonContractException("Не удалось найти транспорт с названием: " + transportName));
        }

        return transports.stream().filter(transport -> transport.getName().equalsIgnoreCase(transportName))
                .findFirst().orElseThrow(()->new JsonContractException("Не удалось найти транспорт с именем: "+transportName));
    }
}
