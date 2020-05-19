package org.iitbact.cc.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "facility_mapping")
@NamedQuery(name = "FacilityLink.findAll", query = "SELECT a FROM FacilityLink a")
public class FacilityLink implements Serializable {

	private static final long serialVersionUID = -2338022546557596303L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "source_facility")
    private Integer sourceFacilityId;

    @Column(name = "mapped_facility")
    private Integer mappedFacilityId;

    @Column(name = "covid_facility_type")
    private String covidFacilityType;

    @JoinColumn(name="mapped_facility", insertable = false, updatable = false)
    @ManyToOne(targetEntity = Facility.class)
    private Facility mappedFacilty;
}
