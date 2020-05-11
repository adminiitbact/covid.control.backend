package org.iitbact.cc.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "wards")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Ward implements Serializable {
	private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "facility_id")
    private Integer facilityId;

    @Column(name = "name")
    private String name;

    @Column(name = "floor")
    private String floor;

    @Column(name = "building_name")
    private String buildingName;

    @Column(name = "gender")
    private String gender;

    @Column(name = "severity")
    private String severity;

    @Column(name = "covid_status")
    private String covidStatus;

    @Column(name = "total_beds")
    private Integer totalBeds;

    @Column(name = "available_beds")
    private Integer availableBeds;

    @Column(name = "ventilators")
    private Integer ventilators;

    @Column(name = "ventilators_occupied")
    private Integer ventilatorsOccupied;

    @Type(type = "json")
    @Column(name = "extra_fields")
    private Object extraFields;

    @Column(name = "covid_ward")
    private Integer covidWard;

    @Column(name = "active")
    private Integer active;
}
