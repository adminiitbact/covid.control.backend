package org.iitbact.cc.services;

import org.iitbact.cc.entities.AdminUser;
import org.iitbact.cc.exceptions.CovidControlErrorCode;
import org.iitbact.cc.exceptions.CovidControlErrorMsg;
import org.iitbact.cc.exceptions.CovidControlException;
import org.iitbact.cc.repository.UserRepository;
import org.iitbact.cc.requests.BaseRequest;
import org.iitbact.cc.response.AdminUserProfileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServices {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ApiValidationService validationService;

	public AdminUser userProfileWrtUserId(String userId) {
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

	public AdminUserProfileResponse profile(BaseRequest request) {
		
		String userId = validationService.verifyFirebaseIdToken(request.getAuthToken());
		
		AdminUser user = userProfileWrtUserId(userId);
		
		AdminUserProfileResponse response = new AdminUserProfileResponse();
		response.setProfile(user);
		return response;
	}
}
