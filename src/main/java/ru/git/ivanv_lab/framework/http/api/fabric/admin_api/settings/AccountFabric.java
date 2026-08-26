package ru.git.ivanv_lab.framework.http.api.fabric.admin_api.settings;

import ru.git.ivanv_lab.framework.data.PropertiesHandler;
import ru.git.ivanv_lab.framework.http.api.ApiWorker;
import ru.git.ivanv_lab.framework.http.api.authenticator.APIs;
import ru.git.ivanv_lab.framework.http.api.authenticator.AuthContract;
import ru.git.ivanv_lab.framework.json.contract_validator.core.JsonContractWorker;
import ru.git.ivanv_lab.framework.json.contract_validator.exceptions.JsonContractException;
import ru.git.ivanv_lab.framework.json.contract_validator.util.JsonMapperHolder;
import ru.git.ivanv_lab.framework.json.contracts.admin_api.accounts.AccountGet;
import ru.git.ivanv_lab.framework.json.contracts.admin_api.accounts.AccountListGet;
import ru.git.ivanv_lab.framework.json.contracts.admin_api.accounts.AccountPost;
import ru.git.ivanv_lab.framework.json.contracts.admin_api.accounts.AccountPut;

import java.util.List;

public class AccountFabric {

    private final JsonContractWorker<AccountListGet> accountListWorker
            = new JsonContractWorker<>(AccountListGet.class);
    private final JsonContractWorker<AccountGet> accountWorker
            = new JsonContractWorker<>(AccountGet.class);
    private final ApiWorker apiWorker = new ApiWorker(PropertiesHandler.baseUrl,
            new AuthContract(APIs.ADMIN_CONSOLE_API, PropertiesHandler.adminLogin, PropertiesHandler.adminPassword));

    public List<AccountListGet> getAllAccounts(){
        return getAllAccountsByFilter(null);
    }

    public List<AccountListGet> getAllAccountsByFilter(String additionalUrl){
        return accountListWorker.getContractArrayFromJsonArray(
                JsonMapperHolder.convertStringToJNode(
                        apiWorker.get("/acapi/accounts"+additionalUrl).code(200).getResponse().asString()
                )
        );
    }

    public AccountGet getAccountById(int id){
        return accountWorker.getContractFromJson(
                JsonMapperHolder.convertStringToJNode(
                        apiWorker.get("/acapi/accounts/"+id).code(200).getResponse().asString()
                )
        );
    }

    public AccountGet getAccountByName(String accountName){
        return getAccountById(getAccountIdByName(accountName));
    }

    public int getAccountIdByName(String accountName){
        return getAllAccounts()
                .stream()
                .filter(accountListGet -> accountListGet.getName().equals(accountName))
                .findFirst()
                .orElseThrow(()->new JsonContractException("Не удалось найти аккаунт с именем: "+accountName))
                .getId();
    }

    public int createAccount(AccountPost accountPost){
        apiWorker.post("/acapi/accounts",JsonMapperHolder.convertContractToString(accountPost))
                .code(200);

        return getAccountIdByName(accountPost.getName());
    }

    public AccountGet updateAccountById(int id, AccountPut accountPut){
        apiWorker.put("/acapi/accounts/"+id, JsonMapperHolder.convertContractToString(accountPut))
                .code(200);

        return getAccountById(id);
    }

    public void deleteAccountById(int id){
        apiWorker.delete("/acapi/accounts/"+id).code(200);
    }

    public void deleteAccountByName(String accountName){
        deleteAccountById(getAccountIdByName(accountName));
    }
}
