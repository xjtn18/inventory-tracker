package com.shopify.inventorytracker.api;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import com.shopify.inventorytracker.response.WeatherResponse;



/**
 * Handles the inventory's weather API requests.
 */ 
@Component
public class ApiHandler {

	// Attributes
	@Autowired
	private String apiKey;


	// Methods

	/** Constructor */
    public ApiHandler() {
		warmup();
    }



	/**
	 * Gets the response object from a given API request.
	 * @param <T> The custom response type.
	 * @param uri - URI of the API request.
	 * @param responseClass - The '.class' of whatever response object we want.
	 * @return Our custom response object.
	 */
	private <T> T request(String uri, Class<T> responseClass){
		// create the connection to the resource
		WebClient client = WebClient.create(uri);

		// grab the response and put it into a Mono instance
		Mono<T> responseMono = client
			.get()
			.retrieve()
			.bodyToMono(responseClass);

		// get our custom response object from Mono and return it
		return responseMono.share().block();
	}



	/**
	 * Returns the weather response of a given a city.
	 * @param cityName - The city where we want weather data from.
	 * @param units - The unit standard we want our response to use.
	 * @return A weather response object.
	 */
	public WeatherResponse getWeatherInCity(String cityName){
		String uri = getWeatherUriBuilder()
			.queryParam("q", cityName)
			.build().toUriString();

		return request(uri, WeatherResponse.class);
	}



	/**
	 * Gives us a weather URI component builder with the unchanging components already set.
	 * @param units - The unit standard we want our response to use.
	 * @return A preset UriComponentsBuilder instance for weather requests.
	 */
	private UriComponentsBuilder getWeatherUriBuilder(){
		return UriComponentsBuilder.newInstance()
			.scheme("https").host("api.openweathermap.org")
			.path("data/2.5/weather")
			.queryParam("appid", apiKey)
			.queryParam("units", "imperial");
	}



	/**
	 * Makes random API call to skip the cold start before the user's first request.
	 */
	private void warmup(){
		try {
			getWeatherInCity("Boston");
		} catch (WebClientResponseException ignored) { }
	}

}

