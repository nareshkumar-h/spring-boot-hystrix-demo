package com.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("product-ratings")
public class RatingController {

	@Autowired
	RatingService ratingService;
	
	@GetMapping("{productId}")
	public Float getRating(@PathVariable("productId") Integer productId) {
		return ratingService.getRating(productId);
	}
}
