
ALTER TABLE `admin_users`
	ADD COLUMN `jurisdiction` VARCHAR(50) NOT NULL AFTER `designation`;

	
ALTER TABLE `admin_users`
	ADD COLUMN `region` TINYINT NOT NULL DEFAULT 1 AFTER `jurisdiction`;

	
	INSERT INTO areas (AREA) VALUES ('Akurdi'),('Alandi'),('Ambegaon'),('Ambi'),('Ambil Odha'),('Anand Nagar'),('Aundh'),('Balaji Nagar'),('Balewadi'),('Baner'),('Baramati'),('Bavdhan Budruk'),('Bavdhan Khurd'),('Bhandarkar Road'),('Bhawani Peth'),('Bhor'),('Bhosari'),('Bhugaon'),('Bhukum'),('Bibvewadi'),('Bijali Nagar'),('Bopkhel'),('Bopodi'),('Camp Pune'),('Chakan'),('Chandan Nagar'),('Chandoli'),('Charholi Budruk'),('Chikhali'),('Chimbali'),('Chinchwad'),('Chinchwad Station'),('Dapodi'),('Deccan Gymkhana'),('Dehu Road'),('Dhankawadi'),('Dhanori'),('Dhayari'),('Dighi'),('Dudulgaon'),('Duttawadi'),('Erandwane'),('Fursungi'),('Ghorpadi'),('Guruwar Peth'),('Hadapsar'),('Hingne Khurd'),('Hinjawadi'),('Kalas'),('Kalewadi'),('Kalyani Nagar'),('Karve Nagar'),('Kasarwadi'),('Katraj'),('Khadki'),('Kharadi'),('Kondhwa'),('Koregaon Park'),('Kothrud'),('Lavale'),('Maan'),('Mangalwar Peth'),('Manjri'),('Markal'),('Mohammed Wadi'),('Moshi'),('Mundhwa'),('Nanded'),('Nehrunagar'),('New Sangvi'),('Nigdi'),('Panmala'),('Parandwadi'),('Parvati'),('Pashan'),('Paud Road'),('Phugewadi'),('Pimple Gurav'),('Pimple Nilakh'),('Pimple Saudagar'),('Pimpri'),('Pimpri Camp'),('Pirangut'),('Rahatani'),('Rasta Peth'),('Ravet'),('Sadashiv Peth'),('Sangvi'),('Shivajinagar'),('Shivane'),('Shukrawar Peth'),('Somatne'),('Sus'),('Swargate'),('Talawade'),('Talegaon'),('Tathawade'),('Thergaon'),('Undri'),('Urse'),('Vadgaon Budruk'),('Vadgaon Khurd'),('Vadgaon Maval'),('Varale'),('Vishrantwadi'),('Vitthalwadi'),('Wadgaon Sheri'),('Wagholi'),('Wakad'),('Wanowrie'),('Warje'),('Yerwada');
	
	
	ALTER TABLE `hospital_users`
	ADD COLUMN `region` TINYINT NOT NULL DEFAULT '1' AFTER `facility_id`;

	
	alter table facilities
    add column has_links tinyint not null default false;

    update facilities f,facility_mapping m
    set has_links = true
    where f.facility_id = m.source_facility;

    alter table facilities
    add column operating_status tinyint not null default false;