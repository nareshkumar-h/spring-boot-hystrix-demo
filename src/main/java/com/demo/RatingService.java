package com.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class RatingService {

	@Autowired
	RestTemplate restTemplate;

	@HystrixCommand(fallbackMethod = "defaultRating")
	public Float getRating(Integer productId) {

		String url = "https://product-apii.herokuapp.com/product/" + productId;
		Object rating = restTemplate.getForObject(url, Object.class);
		System.out.println("Rating:" + rating);
		if(productId == 1) {
			throw new RuntimeException("DB is down");
		}
		System.out.println("Actual method - " + productId);
		
		return 1f;
	}

	private Float defaultRating(Integer productId) {
		System.out.println("Fallback method - " + productId);
		return 0f;
	}

}
