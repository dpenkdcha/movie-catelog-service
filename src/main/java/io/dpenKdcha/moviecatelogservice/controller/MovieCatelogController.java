package io.dpenKdcha.moviecatelogservice.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.dpenKdcha.moviecatelogservice.config.RestBuilder;
import io.dpenKdcha.moviecatelogservice.model.CatelogDto;
import io.dpenKdcha.moviecatelogservice.model.MovieDto;
import io.dpenKdcha.moviecatelogservice.model.RatingDto;
import reactor.core.publisher.Mono;

@RestController()
@RequestMapping("/api/v1")
public class MovieCatelogController {
	private static final Logger LOG = LoggerFactory.getLogger(MovieCatelogController.class);

	@Autowired
	private RestBuilder restBuilder;

	@PostMapping("/catelog")
	public List<CatelogDto> getCatelog(String userId) {

		/* Rest templet */
		
		List<RatingDto> l = Arrays.asList( 
				new RatingDto("500", 100), 
				new RatingDto("700", 200) 
				);
		 

		/*
		 * return ratings.stream().map(rating -> { MovieDto movieDto =
		 * restBuilder.getRestTemplet().getForObject(
		 * "http://localhost:8082/api/v1/movies", MovieDto.class); return new
		 * CatelogDto(movieDto.getName(), "abc", rating.getRating());
		 * }).collect(Collectors.toList());
		 */

		/* Web Client */


		// ratings.stream().map(rating -> rating).collect(Collectors.toList());
		/*ratings.stream().forEach( rating -> {
			LOG.info(rating.getMovidId());
			LOG.info(rating.getMovidId());
		});*/
		
		List<RatingDto> ratings = l.stream().map( a -> {
			RatingDto rating = restBuilder.getWebClientBuilder().build()
					.post()
					.uri("http://rating-data-service/api/v1/ratings")
					.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
					.body(Mono.just(a), RatingDto.class)
					.retrieve()
					.bodyToMono(RatingDto.class)
					.block();

			return new RatingDto(rating.getMovidId(), rating.getRating());
		}).collect(Collectors.toList());
		

		return ratings.stream().map(rating -> { 
			//MovieDto movieDto = restBuilder.getRestTemplet().getForObject("http://localhost:8082/api/v1/movies", MovieDto.class);
			/*MovieDto movieDto = restBuilder.getWebClientBuilder().build()
					.post()
					.uri("http://movie-info-service/api/v1/movies")
					.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
					.body(Mono.just(rating), RatingDto.class)
					.retrieve()
					.bodyToMono(MovieDto.class)
					.block();*/
			
			MovieDto movieDto = restBuilder.getWebClientBuilder().build()
					.post()
					.uri("http://movie-info-service/api/v1/movieDB")
					.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
					.body(Mono.just(rating.getMovidId()), String.class)
					.retrieve()
					.bodyToMono(MovieDto.class)
					.block();

			return new CatelogDto(movieDto.getName(), movieDto.getDesc(), rating.getRating());
		}).collect(Collectors.toList());

	}

}
