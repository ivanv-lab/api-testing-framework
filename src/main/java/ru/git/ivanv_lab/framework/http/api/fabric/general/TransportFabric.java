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
import java.util.concurrent.ConcurrentHashMap;

public class TransportFabric {

    private final JsonContractWorker<Transport> transportListWorker
            = new JsonContractWorker<>(Transport.class);
    private final ApiWorker apiWorker;

    private static final Set<Transport> transports = ConcurrentHashMap.newKeySet();
    private static volatile boolean loaded = false;

    public TransportFabric(ApiWorker apiWorker){
        this.apiWorker = apiWorker;
    }

    public List<Transport> getAllTransports() {
        return List.copyOf(uploadTransportsSynchronized());
    }

    public Transport getTransportById(int id) {
        return uploadTransportsSynchronized()
                .stream()
                .filter(transport -> transport.getId() == id)
                .findFirst()
                .orElseThrow(() -> new JsonContractException("Не удалось найти транспорт с id: " + id));
    }

    public Transport getTransportByName(String transportName) {
        return uploadTransportsSynchronized()
                .stream()
                .filter(transport -> transport.getName().equalsIgnoreCase(transportName))
                .findFirst()
                .orElseThrow(() -> new JsonContractException("Не удалось найти транспорт с именем: " + transportName));
    }

    private List<Transport> uploadTransportsSynchronized() {
        if (!loaded) {
            synchronized (TransportFabric.class) {
                if (!loaded) {
                    transports.addAll(transportListWorker.getContractArrayFromJsonArray(
                            JsonMapperHolder.convertStringToJNode(
                                    apiWorker.get("/acapi/transports").code(200).getResponse().asString()
                            )
                    ));
                    loaded = true;
                }
            }
        }

        return transports.stream().toList();
    }
}
