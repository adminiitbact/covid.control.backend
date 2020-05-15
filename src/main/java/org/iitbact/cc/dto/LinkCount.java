package org.iitbact.cc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class LinkCount {
    private Integer facilityId;
    private Long dchLinkCount;
    private Long dchcLinkCount;
    private Long cccLinkCount;
}
