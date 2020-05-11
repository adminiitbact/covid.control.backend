package org.iitbact.cc.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.iitbact.cc.entities.Patient;
import org.springframework.jdbc.core.RowMapper;

public class PatientRowMapper implements RowMapper<Patient> {
	
	@Override
	public Patient mapRow(ResultSet rs, int rowNum) throws SQLException {
		Patient patient = new Patient();
		patient.setPatientId(rs.getInt("patient_id"));
		patient.setGoiCovidId(rs.getString("goi_covid_id"));
		patient.setName(rs.getString("name"));
		patient.setAge(rs.getInt("age"));
		patient.setGender(rs.getString("gender"));
		patient.setAddress("\"" + rs.getString("address") + "\"");
		patient.setLocality(rs.getString("locality"));
		patient.setPincode(rs.getString("pincode"));
		patient.setOccupation(rs.getString("occupation"));
		patient.setContactNumber(rs.getString("contact_number"));
		patient.setDistrict(rs.getString("district"));
		patient.setState(rs.getString("state"));
		return patient;
	}

}
