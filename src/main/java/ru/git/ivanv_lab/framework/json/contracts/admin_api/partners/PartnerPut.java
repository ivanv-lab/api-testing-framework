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
public class PartnerPut {
    @JsonProperty("name")
    private String name;
    @JsonProperty("transports")
    private PartnerTransport[] transports;
    @JsonProperty("prepaid")
    private boolean prepaid;
    @JsonProperty("status")
    private int status;
}
