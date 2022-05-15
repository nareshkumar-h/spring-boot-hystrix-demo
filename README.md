# spring-boot-hystrix-demo


#### Dependencies

```xml
<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
			<version>2.2.10.RELEASE</version>
		</dependency>

		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-netflix-hystrix-dashboard</artifactId>
			<version>2.2.10.RELEASE</version>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-actuator</artifactId>
		</dependency>
```

##### Annotations
```java
package com.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableCircuitBreaker
@EnableHystrix
@EnableHystrixDashboard
public class CircuitbreakerDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CircuitbreakerDemoApplication.class, args);
	}
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
```

```java
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
```

```java
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

```
* application.properties

```
server.port=8080
management.endpoints.web.exposure.include=*
hystrix.dashboard.proxy-stream-allow-list=*
hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds=5000
```

##### Hystrix Dashboard
* http://localhost:8080/hystrix

![image](https://user-images.githubusercontent.com/2763774/168489124-61352d06-09ee-4ad5-a643-f4e182425f74.png)

![image](https://user-images.githubusercontent.com/2763774/168489145-8ab7296e-db3d-47cd-8338-b854551b651b.png)
