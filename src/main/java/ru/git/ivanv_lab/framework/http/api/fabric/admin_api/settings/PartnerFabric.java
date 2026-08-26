package ru.git.ivanv_lab.framework.http.api.fabric.admin_api.settings;

import ru.git.ivanv_lab.framework.http.api.ApiWorker;
import ru.git.ivanv_lab.framework.http.api.fabric.Fabric;
import ru.git.ivanv_lab.framework.json.contracts.admin_api.partners.PartnerGet;
import ru.git.ivanv_lab.framework.json.contracts.admin_api.partners.PartnerListGet;
import ru.git.ivanv_lab.framework.json.contracts.admin_api.partners.PartnerPost;
import ru.git.ivanv_lab.framework.json.contracts.admin_api.partners.PartnerPut;

public class PartnerFabric extends Fabric<PartnerListGet, PartnerGet> {
    public PartnerFabric(ApiWorker apiWorker){
        super(PartnerListGet.class, PartnerGet.class,
                "/acapi/partners",apiWorker,
                PartnerListGet::getId, PartnerListGet::getName);
    }

    public int createPartner(PartnerPost post){
        return create(post, post.getName());
    }

    public PartnerGet updatePartnerById(int id, PartnerPut put){
        return updateById(id, put);
    }
}
