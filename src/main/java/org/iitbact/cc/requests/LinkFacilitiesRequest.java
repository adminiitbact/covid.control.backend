package org.iitbact.cc.requests;

import java.util.List;

import org.iitbact.cc.constants.LinkingStatus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LinkFacilitiesRequest extends BaseRequest {
    private List<Link> facilityLinks;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Link {
        private Integer facilityId;
        private LinkingStatus linkingStatus;
    }
}
