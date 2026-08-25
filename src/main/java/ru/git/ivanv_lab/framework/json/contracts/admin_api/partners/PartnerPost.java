package ru.git.ivanv_lab.framework.json.contracts.admin_api.partners;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PartnerPost {
    @JsonProperty("name")
    private String name;
    @JsonProperty("transports")
    private PartnerTransport[] transports;
    @JsonProperty("prepaid")
    private boolean prepaid;
    @JsonProperty("status")
    private int status;

    private PartnerPost(Builder builder) {
        this.name = builder.name;
        this.transports = builder.transports;
        this.prepaid = builder.prepaid;
        this.status = builder.status;
    }

    public static class Builder{
        private String name;
        private PartnerTransport[] transports;
        private boolean prepaid;
        private int status;

        public Builder withName(String name){
            this.name = name;
            return this;
        }

        public Builder withTransports(PartnerTransport[] transports){
            this.transports = transports;
            return this;
        }

        public Builder withPrepaid(boolean prepaid){
            this.prepaid=prepaid;
            return this;
        }

        public Builder withStatus(int status){
            this.status=status;
            return this;
        }

        public PartnerPost build(){
            return new PartnerPost(this);
        }
    }
}
