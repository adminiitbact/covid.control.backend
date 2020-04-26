package org.iitbact.cc.helper;

import org.iitbact.cc.beans.BaseBean;
import org.iitbact.cc.beans.ResponseBean;
import org.iitbact.cc.beans.ResponseBuilder;
import org.iitbact.cc.exceptions.CovidControlErpError;
import org.iitbact.cc.exceptions.CovidControlException;
import org.iitbact.cc.requests.BaseRequest;
import org.iitbact.cc.services.ApiValidationService;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class ControllerWrapper {

    private final ApiValidationService validationService;

    public ControllerWrapper(ApiValidationService validationService) {
        this.validationService = validationService;
    }

    public <Entity, T extends BaseBean<Entity>, B extends BaseRequest> ResponseBean<T>
    wrap(Supplier<T> baseBeanSupplier,
         B baseRequest,
         CovidServiceSupplier<Entity> serviceSupplier) {
        CovidControlErpError error = null;
        T response = baseBeanSupplier.get();
        try {
            // First verify the token
            String userId = validationService.verifyFirebaseIdToken(baseRequest.getAuthToken());
            // Then call the function
            response.setEntity(serviceSupplier.get(userId));
        } catch (CovidControlException e) {
            error = e.getError();
        }
        ResponseBuilder<T> responseBuilder = new ResponseBuilder<>(response, error);
        return responseBuilder.build();
    }
}
