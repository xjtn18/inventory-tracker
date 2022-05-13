package com.shopify.inventorytracker.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import javax.sql.DataSource;


// Simple config class to store the injected OpenWeather API key from application.properties.
@Configuration
@PropertySource(value="classpath:application.properties", ignoreResourceNotFound=true)
class WeatherApiConfig {

	@Autowired
	private Environment env;

	@Bean
	public String apiKey(){
		return env.getProperty("weather.apiKey");
	}
}