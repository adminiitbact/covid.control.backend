package org.iitbact.cc.batch;

import java.io.IOException;
import java.io.Writer;

import javax.sql.DataSource;

import org.iitbact.cc.entities.Patient;
import org.iitbact.cc.mappers.PatientRowMapper;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
@EnableBatchProcessing
public class PatientCsvExporter extends DefaultBatchConfigurer {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private DataSource dataSource;

	@Override
	protected JobRepository createJobRepository() throws Exception {
		MapJobRepositoryFactoryBean factoryBean = new MapJobRepositoryFactoryBean();
		factoryBean.afterPropertiesSet();
		return factoryBean.getObject();
	}

	@Override
	public void setDataSource(DataSource dataSource) {
	}

	@Bean
	public JdbcCursorItemReader<Patient> reader() {
		JdbcCursorItemReader<Patient> cursorItemReader = new JdbcCursorItemReader<>();
		cursorItemReader.setDataSource(dataSource);
		cursorItemReader.setSql("SELECT * FROM patients");
		cursorItemReader.setRowMapper(new PatientRowMapper());
		return cursorItemReader;
	}

	@Bean
	public FlatFileItemWriter<Patient> writer() {
		FlatFileItemWriter<Patient> writer = new FlatFileItemWriter<Patient>();
		writer.setResource(new ClassPathResource("patients.csv"));

		DelimitedLineAggregator<Patient> lineAggregator = new DelimitedLineAggregator<Patient>();
		lineAggregator.setDelimiter(",");

		BeanWrapperFieldExtractor<Patient> fieldExtractor =
				new BeanWrapperFieldExtractor<Patient>();
		fieldExtractor.setNames(new String[] { "patientId", "goiCovidId", "name", "age", "gender",
				"address", "locality", "pincode", "occupation", "contactNumber", "district",
				"state" });

		lineAggregator.setFieldExtractor(fieldExtractor);
		writer.setLineAggregator(lineAggregator);

		writer.setHeaderCallback(new FlatFileHeaderCallback() {

			@Override
			public void writeHeader(Writer writer) throws IOException {
				writer.write(
						"ID,GOI Covid ID,Name,Age,Gender,Address,Locality,Pincode,Occupation,Contact Number,District,State");
			}
		});

		return writer;
	}

	@Bean
	public Step step() {
		return stepBuilderFactory.get("step").<Patient, Patient>chunk(100).reader(reader())
				.writer(writer()).build();
	}

	@Bean
	Job exportPatientsJob() {
		return jobBuilderFactory.get("exportPatientsJob").incrementer(new RunIdIncrementer())
				.flow(step()).end().build();
	}
}
