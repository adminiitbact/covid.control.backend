package org.iitbact.cc.beans;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.iitbact.cc.constants.Constants;
import org.iitbact.cc.exceptions.CovidControlErrorCode;
import org.iitbact.cc.exceptions.CovidControlErrorMsg;
import org.iitbact.cc.exceptions.CovidControlException;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

import lombok.Data;

@Data
public class ContactDetails {
	private String name;
	private String mobile;
	private String email;

	public Object generateJson(JsonFactory jsonFactory) throws CovidControlException {
		try (Writer writer = new StringWriter(); JsonGenerator g = jsonFactory.createGenerator(writer);) {
			g.writeStartObject();
			g.writeStringField(Constants.PRIMARY_CONTACT_EMAIL, this.email);
			g.writeStringField(Constants.PRIMARY_CONTACT_MOBILE, this.mobile);
			g.writeStringField(Constants.PRIMARY_CONTACT_NAME, this.name);
			g.writeEndObject();
			g.close();
			return writer.toString();
		} catch (IOException e) {
			throw new CovidControlException(CovidControlErrorCode.SYSTEM_ERROR, CovidControlErrorMsg.SYSTEM_ERROR);
		}
	}

}
