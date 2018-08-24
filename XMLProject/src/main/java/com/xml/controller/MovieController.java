package com.xml.controller;

import com.xml.model.Category;
import com.xml.model.Movie;
import com.xml.model.MovieHasCategory;
import com.xml.service.CategoryService;
import com.xml.service.MovieHasCategoryService;
import com.xml.service.MovieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MovieController {

    private final MovieService movieService;
    private final MovieHasCategoryService movieHasCategoryService;
    private final CategoryService categoryService;

    public MovieController(MovieService movieService, MovieHasCategoryService movieHasCategoryService, CategoryService categoryService) {
        this.movieService = movieService;
        this.movieHasCategoryService = movieHasCategoryService;
        this.categoryService = categoryService;
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
    public ResponseEntity getMovieById(HttpServletRequest request, @PathVariable String id) {

        List<MovieHasCategory> movieHasCategories = this.movieHasCategoryService.findAllByMovieId(Integer.parseInt(id));
        List<String> categories = new ArrayList<>();
        for (MovieHasCategory movieHasCategory : movieHasCategories) {
            categories.add(this.categoryService.findById(movieHasCategory.getCategoryId()).getName());
        }
        Movie movie = this.movieService.getById(new BigInteger(id));
        movie.setTitle(movie.getTitle() + "-" + String.join(",", categories));
        return ResponseEntity.status(HttpStatus.OK)
                .body(movie);
    }


}
