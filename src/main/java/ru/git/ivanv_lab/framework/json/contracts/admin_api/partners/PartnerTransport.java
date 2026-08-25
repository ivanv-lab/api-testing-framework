package ru.git.ivanv_lab.framework.json.contracts.admin_api.partners;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.git.ivanv_lab.framework.fabric.general.TransportFabric;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PartnerTransport {
    @JsonProperty("id")
    private int id;
    @JsonProperty("multisignature")
    private boolean multisignature;
    @JsonProperty("on_moderation")
    private boolean onModeration;
    @JsonProperty("template_only")
    private boolean templateOnly;

    private PartnerTransport(Builder builder) {
        this.id = builder.id;
        this.multisignature = builder.multisignature;
        this.onModeration = builder.onModeration;
        this.templateOnly = builder.templateOnly;
    }

    public static class Builder{
        private int id;
        private boolean multisignature;
        private boolean onModeration;
        private boolean templateOnly;

        public Builder withTransport(String transportName){
            final TransportFabric fabric=new TransportFabric();
            this.id = fabric.getTransportByName(transportName).getId();
            return this;
        }

        public Builder withMultisignature(boolean multisignature){
            this.multisignature=multisignature;
            return this;
        }

        public Builder withModeration(boolean onModeration){
            this.onModeration = onModeration;
            return this;
        }

        public Builder withTemplateOnly(boolean templateOnly){
            this.templateOnly = templateOnly;
            return this;
        }

        public PartnerTransport build(){
            return new PartnerTransport(this);
        }
    }
}
