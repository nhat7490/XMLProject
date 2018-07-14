package com.xml.controller;

import com.xml.model.Movie;
import com.xml.model.Movies;
import com.xml.service.MovieService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigInteger;

@Controller
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

//    @GetMapping(value = "/movies/top/page={page}&no={number}", produces = MediaType.APPLICATION_XML_VALUE)
//    public ResponseEntity getAll(@PathVariable String page,
//                                 @PathVariable String number) {
//        Movies movies = new Movies();
//        Page<Movie> movieList = this.movieService.findMoviePerPage(Integer.parseInt(page), Integer.parseInt(number));
//        movies.setMovie(movieList.getContent());
//        return ResponseEntity.status(HttpStatus.OK).body(movies);
//    }

    @GetMapping(value = "/movie-detail/{id}", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity getMovieById(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(this.movieService
                        .getById(new BigInteger(id)));
    }


}
