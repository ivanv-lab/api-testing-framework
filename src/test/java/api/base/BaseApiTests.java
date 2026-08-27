package api.base;

import ru.git.ivanv_lab.framework.data.PropertiesHandler;
import ru.git.ivanv_lab.framework.http.api.ApiWorker;
import ru.git.ivanv_lab.framework.http.api.authenticator.Api;
import ru.git.ivanv_lab.framework.http.api.authenticator.AuthContract;
import ru.git.ivanv_lab.framework.http.api.fabric.general.TransportFabric;
import ru.git.ivanv_lab.framework.json.contract_validator.core.JsonWorker;

public class BaseApiTests {

    protected final JsonWorker jsonWorker=new JsonWorker();
    protected final ThreadLocal<ApiWorker> adminConsoleApiWorker=ThreadLocal
            .withInitial(()->new ApiWorker(PropertiesHandler.baseUrl,
                    new AuthContract(Api.ADMIN_CONSOLE_API, PropertiesHandler.adminLogin, PropertiesHandler.adminPassword)));

    protected final TransportFabric transportFabric = new TransportFabric(adminConsoleApiWorker.get());
}
