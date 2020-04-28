package org.iitbact.cc.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * The persistent class for the facilities database table.
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "areas")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Area implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "area_id")
	private Integer areaId;

	@Column(name = "area")
	private String area;

	@Column(name = "region")
	private Integer region;
}