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
public class AccountListGet {
    @JsonProperty("id")
    private int id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("login")
    private String login;
    @JsonProperty("protocol")
    private String protocol;
    @JsonProperty("partner")
    private String partner;
    @JsonProperty("transports")
    private String[] transports;
    @JsonProperty("additional_processing_rule")
    private String additionalProcessingRule;
    @JsonProperty("status")
    private int status;
}
