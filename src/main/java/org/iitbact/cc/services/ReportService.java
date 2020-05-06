package org.iitbact.cc.services;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.iitbact.cc.entities.Facility;
import org.iitbact.cc.entities.Patient;
import org.iitbact.cc.repository.FacilityRepository;
import org.iitbact.cc.repository.PatientRepository;
import org.springframework.stereotype.Service;

import com.opencsv.CSVWriter;

@Service
public class ReportService {

	private final FacilityRepository facilityRepository;
	private final PatientRepository patientRepository;

	public ReportService(FacilityRepository facilityRepository,
			PatientRepository patientRepository) {
		this.facilityRepository = facilityRepository;
		this.patientRepository = patientRepository;
	}

	public List<String[]> fetchFacilityData() {
		List<Facility> facilities = facilityRepository.findAll();
		List<String[]> entries = new ArrayList<>();

		// Add header for the CSV file.
		entries.add(new String[] { "ID", "Name", "Area", "Facility Type", "Telephone", "Email" });

		// Add the data rows.
		for (Facility f : facilities) {
			entries.add(new String[] { Integer.toString(f.getFacilityId()), f.getName(),
					f.getArea(), f.getCovidFacilityType(), f.getTelephone(), f.getEmail() });
		}
		return entries;

	}

	public List<String[]> fetchPatientData() {
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
