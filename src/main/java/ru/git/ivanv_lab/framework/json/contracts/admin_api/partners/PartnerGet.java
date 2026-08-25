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
public class PartnerGet {
    @JsonProperty("name")
    private String name;
    @JsonProperty("transport_names")
    private String[] transportNames;
    @JsonProperty("status")
    private int status;
    @JsonProperty("prepaid")
    private boolean prepaid;
}
