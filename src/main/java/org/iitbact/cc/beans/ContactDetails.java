package org.iitbact.cc.beans;

import java.io.Serializable;

import org.iitbact.cc.constants.Constants;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContactDetails implements Serializable {
	private static final long serialVersionUID = 4366883634691587898L;

	@JsonProperty(value = Constants.PRIMARY_CONTACT_NAME)
	private String name;

	@JsonProperty(value = Constants.PRIMARY_CONTACT_MOBILE)
	private String mobile;

	@JsonProperty(value = Constants.PRIMARY_CONTACT_EMAIL)
	private String email;
}
