package org.iitbact.cc.services;

import org.iitbact.cc.exceptions.CovidControlErrorCode;
import org.iitbact.cc.exceptions.CovidControlErrorMsg;
import org.iitbact.cc.exceptions.CovidControlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

@Service
public class ApiValidationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApiValidationService.class);

	public String verifyFirebaseIdToken(String authToken) {

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
