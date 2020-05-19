package org.iitbact.cc.services;

import java.util.function.Supplier;

import org.iitbact.cc.constants.SYSTEM_ENVIRONMENT;
import org.iitbact.cc.entities.AdminUser;
import org.iitbact.cc.entities.Facility;
import org.iitbact.cc.exceptions.CovidControlErpError;
import org.iitbact.cc.exceptions.CovidControlErrorCode;
import org.iitbact.cc.exceptions.CovidControlErrorMsg;
import org.iitbact.cc.exceptions.CovidControlException;
import org.iitbact.cc.repository.FacilityRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ApiValidationService {

	@Value("${ENV}")
	private String env;
	private final FacilityRepository facilityRepository;
	private final UserServices userServices;
	
	private static final Supplier<CovidControlException> facilityDoesNotExistException = () -> new CovidControlException(
			new CovidControlErpError(CovidControlErrorCode.FACILITY_DOES_NOT_EXIST,
					CovidControlErrorMsg.FACILITY_DOES_NOT_EXIST));
	
	public ApiValidationService(FacilityRepository facilityRepository,UserServices userServices) {
		this.facilityRepository=facilityRepository;
		this.userServices=userServices;
	}

	public String verifyFirebaseIdToken(String authToken) throws CovidControlException {
		if (SYSTEM_ENVIRONMENT.DEV.toString().equalsIgnoreCase(env)) {
			return "xCCmSYQSYhRvG8s2QL5Su2O2khy1";
		} else {
			try {
				FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(authToken);
				String uid = decodedToken.getUid();
				return uid;
			} catch (FirebaseAuthException e) {
				e.printStackTrace();
				log.error("Firebase token has expired or invalid!");
				throw new CovidControlException(CovidControlErrorCode.INVALID_ACCESS_CODE,
						CovidControlErrorMsg.INVALID_ACCESS_CODE);
			}
		}
	}

	public Facility facilityValidation(String uid, int facilityId) throws CovidControlException {
		AdminUser user=userServices.profile(uid);
		Facility facility=facilityRepository.findById(facilityId).orElseThrow(facilityDoesNotExistException);
		if(user.getRegion()!=facility.getRegion()) {
			throw new CovidControlException(CovidControlErrorCode.INVALID_ACCESS_CODE, CovidControlErrorMsg.INVALID_ACCESS_CODE);
		}
		return facility;	
	}
}
