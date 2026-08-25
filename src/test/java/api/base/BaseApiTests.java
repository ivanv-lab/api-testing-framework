package api.base;

import ru.git.ivanv_lab.framework.http.api.ApiWorker;
import ru.git.ivanv_lab.framework.http.api.authenticator.APIs;
import ru.git.ivanv_lab.framework.http.api.authenticator.AuthContract;
import ru.git.ivanv_lab.framework.json.contract_validator.core.JsonWorker;

public class BaseApiTests {

    protected final JsonWorker jsonWorker=new JsonWorker();
    protected final ThreadLocal<ApiWorker> adminConsoleApiWorker=ThreadLocal
            .withInitial(()->new ApiWorker("http://192.168.128.250",
                    new AuthContract(APIs.ADMIN_CONSOLE_API, "admin@admin.com","Admin")));
}
