package ru.git.ivanv_lab.framework.json.contracts.admin_api.accounts;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.git.ivanv_lab.framework.http.api.fabric.admin_api.settings.PartnerFabric;
import ru.git.ivanv_lab.framework.http.api.fabric.general.Transport;
import ru.git.ivanv_lab.framework.http.api.fabric.general.TransportFabric;
import ru.git.ivanv_lab.framework.json.contract_validator.exceptions.JsonContractException;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AccountPost {
    @JsonProperty("name")
    private String name;
    @JsonProperty("login")
    private String login;
    @JsonProperty("password")
    private String password;
    @JsonProperty("protocol_id")
    private int protocolId;
    @JsonProperty("partner_id")
    private int partnerId;
    @JsonProperty("transport_id")
    private int transportId;
    @JsonProperty("status")
    private int status;
    @JsonProperty("push_statuses")
    private int pushStatuses;
    @JsonProperty("url")
    private String url;
    @JsonProperty("status_report")
    private int statusReport;
    @JsonProperty("event_report")
    private int eventReport;
    @JsonProperty("max_throughput")
    private String maxThroughput;
    @JsonProperty("immediate_response_needed")
    private int immediateResponseNeeded;
    @JsonProperty("additional_processing_rule")
    private String additionalProcessingRule;

    public AccountPost(Builder builder) {
        this.name = builder.name;
        this.login = builder.login;
        this.password = builder.password;
        this.protocolId = builder.protocolId;
        this.partnerId = builder.partnerId;
        this.transportId = builder.transportId;
        this.status = builder.status;
        this.pushStatuses = builder.pushStatuses;
        this.url = builder.url;
        this.statusReport = builder.statusReport;
        this.eventReport = builder.eventReport;
        this.maxThroughput = builder.maxThroughput;
        this.immediateResponseNeeded = builder.immediateResponseNeeded;
        this.additionalProcessingRule = builder.additionalProcessingRule;
    }

    public static class Builder {
        private String name;
        private String login;
        private String password;
        private int protocolId;
        private int partnerId;
        private int transportId;
        private int status;
        private int pushStatuses;
        private String url;
        private int statusReport;
        private int eventReport;
        private String maxThroughput;
        private int immediateResponseNeeded;
        private String additionalProcessingRule;

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withLogin(String login) {
            this.login = login;
            return this;
        }

        public Builder withPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder withPartner(int partnerId) {
            this.partnerId = partnerId;
            return this;
        }

        public Builder withStatus(boolean status) {
            this.status = status ? 1 : 0;
            return this;
        }

        public Builder withHttpProtocol(boolean pushStatuses, boolean eventReport,
                                        String url, boolean allStatuses) {
            this.pushStatuses = pushStatuses ? 1 : 0;
            this.eventReport = eventReport ? 1 : 0;
            this.url = url;
            this.statusReport = allStatuses ? 1 : 0;
            this.protocolId = 1;
            return this;
        }

        public Builder withSmppProtocol(String maxThroughput, boolean immediateResponseNeeded,
                                        Transport transport, String additionalProcessingRule) {
            this.maxThroughput = maxThroughput;
            this.immediateResponseNeeded = immediateResponseNeeded ? 1 : 0;
            this.protocolId = 2;

            if (transport != null) {
                final TransportFabric transportFabric = new TransportFabric();
                this.transportId = transportFabric.getTransportByName(transport.getTransportName())
                        .getId();
            } else {
                this.additionalProcessingRule = additionalProcessingRule;
            }

            return this;
        }

        public AccountPost build() {
            return new AccountPost(this);
        }
    }
}
