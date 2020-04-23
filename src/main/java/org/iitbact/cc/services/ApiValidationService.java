package org.iitbact.cc.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ApiValidationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApiValidationService.class);

	public String verifyFirebaseIdToken(String authToken) {
		return "xCCmSYQSYhRvG8s2QL5Su2O2khy1";
		/*
		 * try { FirebaseToken decodedToken =
		 * FirebaseAuth.getInstance().verifyIdToken(authToken); String uid =
		 * decodedToken.getUid(); return uid; } catch (FirebaseAuthException e) {
		 * e.printStackTrace(); LOGGER.error("Firebase token has expired or invalid!");
		 * throw new CovidControlException(CovidControlErrorCode.INVALID_ACCESS_CODE,
		 * CovidControlErrorMsg.INVALID_ACCESS_CODE); }
		 */
	}

}
