package org.iitbact.cc.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.iitbact.cc.entities.Facility;
import org.springframework.jdbc.core.RowMapper;

public class FacilityRowMapper implements RowMapper<Facility> {

	@Override
	public Facility mapRow(ResultSet rs, int rowNum) throws SQLException {
		Facility facility = new Facility();
		facility.setFacilityId(rs.getInt("facility_id"));
		facility.setName(rs.getString("name"));
		facility.setArea(rs.getString("area"));
		facility.setCovidFacilityType(rs.getString("covid_facility_type"));
		facility.setTelephone(rs.getString("telephone"));
		facility.setEmail(rs.getString("email"));
		return facility;
	}

}
