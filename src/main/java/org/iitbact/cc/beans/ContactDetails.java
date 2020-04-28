package org.iitbact.cc.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.iitbact.cc.constants.Constants;
import org.iitbact.cc.exceptions.CovidControlException;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContactDetails implements Serializable {
	@JsonProperty(value = Constants.PRIMARY_CONTACT_NAME)
	private String name;

	@JsonProperty(value = Constants.PRIMARY_CONTACT_MOBILE)
	private String mobile;

	@JsonProperty(value = Constants.PRIMARY_CONTACT_EMAIL)
	private String email;
}
