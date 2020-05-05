package org.iitbact.cc.batch;

import java.io.IOException;
import java.io.Writer;

import javax.sql.DataSource;

import org.iitbact.cc.entities.Facility;
import org.iitbact.cc.mappers.FacilityRowMapper;
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
public class FacilityCsvExporter extends DefaultBatchConfigurer {

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
	public JdbcCursorItemReader<Facility> reader() {
		JdbcCursorItemReader<Facility> cursorItemReader = new JdbcCursorItemReader<>();
		cursorItemReader.setDataSource(dataSource);
		cursorItemReader.setSql("SELECT * FROM facilities");
		cursorItemReader.setRowMapper(new FacilityRowMapper());
		return cursorItemReader;
	}

	@Bean
	public FlatFileItemWriter<Facility> writer() {
		FlatFileItemWriter<Facility> writer = new FlatFileItemWriter<Facility>();
		writer.setResource(new ClassPathResource("facilities.csv"));

		DelimitedLineAggregator<Facility> lineAggregator = new DelimitedLineAggregator<Facility>();
		lineAggregator.setDelimiter(",");

		BeanWrapperFieldExtractor<Facility> fieldExtractor =
				new BeanWrapperFieldExtractor<Facility>();
		fieldExtractor.setNames(new String[] { "facilityId", "name", "area", "covidFacilityType",
				"telephone", "email" });

		lineAggregator.setFieldExtractor(fieldExtractor);
		writer.setLineAggregator(lineAggregator);

		writer.setHeaderCallback(new FlatFileHeaderCallback() {

			@Override
			public void writeHeader(Writer writer) throws IOException {
				writer.write("ID,Name,Area,Facility Type,Telephone,Email");
			}
		});

		return writer;
	}

	@Bean
	public Step step() {
		return stepBuilderFactory.get("step").<Facility, Facility>chunk(100).reader(reader())
				.writer(writer()).build();
	}

	@Bean
	Job exportFacilitiesJob() {
		return jobBuilderFactory.get("exportFacilitiesJob").incrementer(new RunIdIncrementer())
				.flow(step()).end().build();
	}
}
