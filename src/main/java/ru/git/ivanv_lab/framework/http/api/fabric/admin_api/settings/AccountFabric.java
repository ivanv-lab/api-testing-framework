package ru.git.ivanv_lab.framework.http.api.fabric.admin_api.settings;

import ru.git.ivanv_lab.framework.http.api.ApiWorker;
import ru.git.ivanv_lab.framework.http.api.fabric.Fabric;
import ru.git.ivanv_lab.framework.json.contracts.admin_api.accounts.AccountGet;
import ru.git.ivanv_lab.framework.json.contracts.admin_api.accounts.AccountListGet;
import ru.git.ivanv_lab.framework.json.contracts.admin_api.accounts.AccountPost;
import ru.git.ivanv_lab.framework.json.contracts.admin_api.accounts.AccountPut;

public class AccountFabric extends Fabric<AccountListGet, AccountGet> {
    public AccountFabric(ApiWorker apiWorker){
        super(AccountListGet.class, AccountGet.class,
                "/acapi/accounts",apiWorker,
                AccountListGet::getId, AccountListGet::getName);
    }

    public int createAccountReturnId(AccountPost post){
        return createReturnId(post, post.getName());
    }

    public void createAccount(AccountPost post){
        create(post);
    }

    public AccountGet updateAccountById(int id, AccountPut put){
        return updateById(id, put);
    }
}
