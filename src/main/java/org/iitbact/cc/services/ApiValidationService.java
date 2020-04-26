package org.iitbact.cc.services;

import org.iitbact.cc.constants.SYSTEM_ENVIRONMENT;
import org.iitbact.cc.exceptions.CovidControlErrorCode;
import org.iitbact.cc.exceptions.CovidControlErrorMsg;
import org.iitbact.cc.exceptions.CovidControlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

@Service
public class ApiValidationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApiValidationService.class);

	@Value("${ENV}")
	private String env;

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
				LOGGER.error("Firebase token has expired or invalid!");
				throw new CovidControlException(CovidControlErrorCode.INVALID_ACCESS_CODE,
						CovidControlErrorMsg.INVALID_ACCESS_CODE);
			}
		}
	}
}
