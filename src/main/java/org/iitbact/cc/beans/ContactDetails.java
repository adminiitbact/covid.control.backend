package org.iitbact.cc.beans;

import org.iitbact.cc.constants.Constants;
import org.iitbact.cc.exceptions.CovidControlException;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.auto.value.AutoValue.Builder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactDetails {
	private String name;
	private String mobile;
	private String email;

	public Object generateJson(JsonFactory jsonFactory) throws CovidControlException {
		ObjectMapper mapper=new ObjectMapper(jsonFactory);
		ObjectNode node=mapper.createObjectNode();
		node.put(Constants.PRIMARY_CONTACT_EMAIL, this.email);
		node.put(Constants.PRIMARY_CONTACT_MOBILE, this.mobile);
		node.put(Constants.PRIMARY_CONTACT_NAME, this.name);
		return node;
	}

}
