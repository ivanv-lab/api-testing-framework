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
    @JsonProperty("transports")
    private PartnerTransport[] transports;
    @JsonProperty("status")
    private int status;
    @JsonProperty("prepaid")
    private boolean prepaid;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("[name=").append(name).append(";")
                .append("status=").append(status).append(";")
                .append("prepaid=").append(prepaid).append(";");

        builder.append("transports:[");
        for(PartnerTransport transport:transports){
            builder.append("id=").append(transport.getId()).append(";");
        }

        builder.append("]]");

        return builder.toString();
    }
}
