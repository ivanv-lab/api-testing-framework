package ru.git.ivanv_lab.framework.json.contracts.admin_api.accounts;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AccountPut {
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
    private int pushStatues;
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
}
