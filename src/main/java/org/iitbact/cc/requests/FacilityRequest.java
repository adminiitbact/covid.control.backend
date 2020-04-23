package org.iitbact.cc.requests;

import lombok.Data;
import org.iitbact.cc.entities.Facility;

@Data
public class CreateFacilityRequest extends BaseRequest {
    private Facility facility;
}
