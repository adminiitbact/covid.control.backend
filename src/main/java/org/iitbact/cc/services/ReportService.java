package org.iitbact.cc.services;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.iitbact.cc.dto.AvailabilityStatus;
import org.iitbact.cc.entities.AdminUser;
import org.iitbact.cc.entities.Facility;
import org.iitbact.cc.entities.Patient;
import org.iitbact.cc.exceptions.CovidControlException;
import org.iitbact.cc.repository.FacilityRepository;
import org.iitbact.cc.repository.PatientRepository;
import org.iitbact.cc.repository.WardRepository;
import org.springframework.stereotype.Service;

import com.opencsv.CSVWriter;

@Service
public class ReportService {

	private final FacilityRepository facilityRepository;
	private final WardRepository wardRepository;
	private final PatientRepository patientRepository;
	private final UserServices userService;

	public ReportService(FacilityRepository facilityRepository, WardRepository wardRepository,
			PatientRepository patientRepository, UserServices userService) {
		this.facilityRepository = facilityRepository;
		this.wardRepository = wardRepository;
		this.patientRepository = patientRepository;
		this.userService = userService;
	}

	public List<String[]> fetchFacilityData(String uid) throws CovidControlException {
		AdminUser user = userService.profile(uid);
		List<Facility> facilities = facilityRepository.findByRegion(user.getRegion());

		List<String[]> entries = new ArrayList<>();

		// Add header for the CSV file.
		entries.add(new String[] { "ID", "Name", "Area", "Facility Type", "Telephone", "Email",
				"COVID: Total Beds", "COVID: Occupied Beds", "COVID: Available Beds",
				"Mild Cases: Total beds", "Mild Cases: Occupied Beds", "Mild Cases: Available Beds",
				"Moderate Cases: Total Beds", "Moderate Cases: Occupied Beds",
				"Moderate Cases: Available Beds", "Severe Cases: Total beds",
				"Severe Cases: Occupied Beds", "Severe Cases: Available Beds", "Total Ventilators",
				"Occupied Ventilators", "Available Ventilators" });

		// Add the data rows.
		for (Facility f : facilities) {
			List<String> row = new ArrayList<>();
			row.addAll(Arrays.asList(Integer.toString(f.getFacilityId()), f.getName(), f.getArea(),
					f.getCovidFacilityType(), f.getTelephone(), f.getEmail()));

			// Get all the ward data for the different severity of the wards.
			List<AvailabilityStatus> availability =
					wardRepository.getAvailabilityStatus(Arrays.asList(f.getFacilityId()));
			AvailabilityStatus total =
					new AvailabilityStatus(f.getFacilityId(), "TOTAL", 0L, 0L, 0L, 0L);
			AvailabilityStatus mild = null;
			AvailabilityStatus moderate = null;
			AvailabilityStatus severe = null;
			for (AvailabilityStatus a : availability) {
				total.setTotalBeds(total.getTotalBeds() + a.getTotalBeds());
				total.setAvailableBeds(total.getAvailableBeds() + a.getAvailableBeds());
				total.setTotalVentilators(total.getTotalVentilators() + a.getTotalVentilators());
				total.setVentilatorsOccupied(
						total.getVentilatorsOccupied() + a.getVentilatorsOccupied());
				if (a.getSeverity().equals("MILD")) {
					mild = a;
				} else if (a.getSeverity().equals("MODERATE")) {
					moderate = a;
				} else if (a.getSeverity().equals("SEVERE")) {
					severe = a;
				}
			}
			for (AvailabilityStatus a : Arrays.asList(total, mild, moderate, severe)) {
				if (a != null) {
					row.addAll(Arrays.asList(Long.toString(a.getTotalBeds()),
							Long.toString(a.getTotalBeds() - a.getAvailableBeds()),
							Long.toString(a.getAvailableBeds())));
				} else {
					row.addAll(Arrays.asList("0", "0", "0"));
				}
			}
			row.addAll(Arrays.asList(Long.toString(total.getTotalVentilators()),
					Long.toString(total.getVentilatorsOccupied()),
					Long.toString(total.getTotalVentilators() - total.getVentilatorsOccupied())));
			String[] entry = row.stream().toArray(String[]::new);
			entries.add(entry);
		}
		return entries;
	}

	public List<String[]> fetchPatientData(String uid) throws CovidControlException {
		AdminUser user = userService.profile(uid);
		List<Patient> patients = patientRepository.findAll();

		List<String[]> entries = new ArrayList<>();

		// Add header for the CSV file.
		entries.add(new String[] { "ID", "GOI Covid ID", "Name", "Age", "Gender", "Address",
				"Locality", "Pincode", "Occupation", "Contact Number", "District", "State" });

		// Add the data rows.
		for (Patient p : patients) {
			entries.add(new String[] { Integer.toString(p.getPatientId()), p.getGoiCovidId(),
					p.getName(), Integer.toString(p.getAge()), p.getGender(), p.getAddress(),
					p.getLocality(), p.getPincode(), p.getOccupation(), p.getContactNumber(),
					p.getDistrict(), p.getState() });
		}
		return entries;
	}

	public File writeCsvFile(List<String[]> data, String filename) throws IOException {
		File csvFile = new File(filename);
		CSVWriter writer = new CSVWriter(new FileWriter(csvFile, false));
		for (String[] mapping : data) {
			writer.writeNext(mapping);
		}
		writer.close();
		return csvFile;
	}
}
