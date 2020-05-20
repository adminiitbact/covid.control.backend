package org.iitbact.cc.services;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;

import org.iitbact.cc.entities.Facility;
import org.iitbact.cc.exceptions.CovidControlErrorCode;
import org.iitbact.cc.exceptions.CovidControlErrorMsg;
import org.iitbact.cc.exceptions.CovidControlException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.CreateRequest;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class FirebaseUserMgmt {
	
	@Value("${ENV}")
	private String env;
	
	private final AmazonSESSample amazonSESSample;
	
	public FirebaseUserMgmt(AmazonSESSample amazonSESSample) {
		this.amazonSESSample=amazonSESSample;
	}
	private HashMap<String, Object> hasuraClaims(Facility facility,UserRecord userRecord){
		HashMap<String, Object> claims = new HashMap<String, Object>();
		Map<String, Object> hasuraClaims = new HashMap<String, Object>();
		hasuraClaims.put("x-hasura-default-role", "user");
		hasuraClaims.put("x-hasura-allowed-roles", new String[] { "user" });
		hasuraClaims.put("x-hasura-facility-id", String.valueOf(facility.getFacilityId()));
		hasuraClaims.put("x-hasura-region", String.valueOf(facility.getRegion()));
		hasuraClaims.put("x-hasura-env", env);
		hasuraClaims.put("x-hasura-app", "cov2");
		hasuraClaims.put("x-hasura-email", facility.getEmail());
		hasuraClaims.put("x-hasura-name", facility.getName());
		hasuraClaims.put("x-hasura-user-id",userRecord.getUid());
		claims.put("https://hasura.io/jwt/claims", hasuraClaims);
		return claims;
	}
	
	private void generateResetPasswordLink(String email) throws FirebaseAuthException, MessagingException,UnsupportedEncodingException {
		String resetLink=FirebaseAuth.getInstance().generateEmailVerificationLink(email);
		amazonSESSample.sendResetEmail(email, resetLink);
	}

	@SuppressWarnings("unchecked")
	public void createUpdateUser(Facility facility) throws CovidControlException {
		// Create facility user at fire-base
		try {
			FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
			UserRecord record= fetchUser(facility.getEmail());
			
			if(record==null) {
				//Create new firebase user
				CreateRequest createRequest = new CreateRequest();
				createRequest.setDisplayName(facility.getName());
				createRequest.setEmail(facility.getEmail());
				createRequest.setEmailVerified(false);
				record = firebaseAuth.createUser(createRequest);
				
				//Set custom claims
				firebaseAuth.setCustomUserClaims(record.getUid(), hasuraClaims(facility,record));
				
				//Generate email reset link
				generateResetPasswordLink(facility.getEmail());
			}else {
				//Check if the facility Id matches with jwt facility id
				Map<String, Object> claims= (Map<String, Object>) record.getCustomClaims().get("https://hasura.io/jwt/claims");
				if(record!=null && (claims.get("x-hasura-facility-id")).equals(String.valueOf(facility.getFacilityId()))) {
					//Generate email reset link
					generateResetPasswordLink(facility.getEmail());
				}else {
					log.error("EmailId conflict at firebase email {}, facility {}",facility.getEmail(),facility.getFacilityId());
					throw new CovidControlException(CovidControlErrorCode.USER_ALREADY_EXIST, CovidControlErrorMsg.USER_ALREADY_EXIST); 
				}
			}
		} catch (FirebaseAuthException | MessagingException | UnsupportedEncodingException e) {
			e.printStackTrace();
			log.error("Error in Firebase Action!");
			throw new CovidControlException(CovidControlErrorCode.SYSTEM_ERROR,
					CovidControlErrorMsg.SYSTEM_ERROR);
		}
	}

	// Delete a facility user
	public void deleteUser(String uid) throws CovidControlException {
		try {
			FirebaseAuth.getInstance().deleteUser(uid);
		} catch (FirebaseAuthException e) {
			e.printStackTrace();
			log.error("Error in Firebase Action!");
			throw new CovidControlException(CovidControlErrorCode.SYSTEM_ERROR,
					CovidControlErrorMsg.SYSTEM_ERROR);
		}
	}

	// Disable a facility login
	public void disableFacilityLogin(String uid) throws CovidControlException {
		try {
			UserRecord user= FirebaseAuth.getInstance().getUser(uid);
			FirebaseAuth.getInstance().updateUser((user.updateRequest().setDisabled(true)));
		} catch (FirebaseAuthException e) {
			e.printStackTrace();
			log.error("Error in Firebase Action!");
			throw new CovidControlException(CovidControlErrorCode.SYSTEM_ERROR,
					CovidControlErrorMsg.SYSTEM_ERROR);
		}
	}

	// Update email id for old user 
	public void updateEmailId(String emailId,String newEmailId) throws CovidControlException {
		if(!emailId.equalsIgnoreCase(newEmailId)) {
			try {
				FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
				UserRecord userByOldEmail=fetchUser(emailId);
				UserRecord userByNewEmail=fetchUser(newEmailId);
				if(userByNewEmail==null && userByOldEmail!=null) {
					firebaseAuth.updateUser((userByOldEmail.updateRequest().setEmail(newEmailId)));
				}else if(userByNewEmail==null && userByOldEmail==null) {
					log.info("Yet to generated the credentails! old {} new {}",emailId , newEmailId);
				}else {
					log.error("Error in Firebase Action! Email belongs to some other user");
					throw new CovidControlException(CovidControlErrorCode.SYSTEM_ERROR,
							CovidControlErrorMsg.SYSTEM_ERROR);
				}
			} catch (FirebaseAuthException e) {
				e.printStackTrace();
				log.error("Error in Firebase Action!");
				throw new CovidControlException(CovidControlErrorCode.SYSTEM_ERROR,
						CovidControlErrorMsg.SYSTEM_ERROR);
			}
		}
	}

	public UserRecord fetchUser(String email) {
		try {
			return FirebaseAuth.getInstance().getUserByEmail(email);
		} catch (FirebaseAuthException e) {
			log.error("User Doesn't exist!");
			return null;
		}
	}
	
	public UserRecord addHasuraClaims(Facility facility) throws CovidControlException {
		try {
			UserRecord record=  fetchUser(facility.getEmail());
			if(record!=null) {
				record= FirebaseAuth.getInstance().updateUser((record.updateRequest().setCustomClaims(hasuraClaims(facility,record))));
			}
			return record;
		} catch (FirebaseAuthException e) {
			e.printStackTrace();
			log.error("Error in Firebase Action!");
			throw new CovidControlException(CovidControlErrorCode.SYSTEM_ERROR,
					CovidControlErrorMsg.SYSTEM_ERROR);
		}
	}
	
	public FirebaseToken checkJwt(String token) throws CovidControlException {
		try {
			return FirebaseAuth.getInstance().verifyIdToken(token);
		} catch (FirebaseAuthException e) {
			e.printStackTrace();
			log.error("Error in Firebase Action!");
			throw new CovidControlException(CovidControlErrorCode.SYSTEM_ERROR,
					CovidControlErrorMsg.SYSTEM_ERROR);
		}
	}
	
}
