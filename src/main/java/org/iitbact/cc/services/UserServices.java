package org.iitbact.cc.services;

import org.iitbact.cc.entities.AdminUser;
import org.iitbact.cc.exceptions.CovidControlErrorCode;
import org.iitbact.cc.exceptions.CovidControlErrorMsg;
import org.iitbact.cc.exceptions.CovidControlException;
import org.iitbact.cc.repository.UserRepository;
import org.iitbact.cc.requests.BaseRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServices {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApiValidationService validationService;

    private AdminUser userProfileWrtUserId(String userId) throws CovidControlException {
        try {
            AdminUser user = userRepository.findByUserId(userId);
            if (user == null) {
                System.out.println("UserId not found : " + userId);
                throw new CovidControlException(CovidControlErrorCode.DATABASE_ERROR, CovidControlErrorMsg.DATABASE_ERROR);
            }
            return user;
        } catch (Exception e) {
            System.out.println("System Error " + userId);
            throw new CovidControlException(CovidControlErrorCode.DATABASE_ERROR, CovidControlErrorMsg.DATABASE_ERROR);
        }
    }

    public AdminUser profile(BaseRequest request) throws CovidControlException {

        String userId = validationService.verifyFirebaseIdToken(request.getAuthToken());

        return userProfileWrtUserId(userId);
    }
}
