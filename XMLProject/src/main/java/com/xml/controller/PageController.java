package com.xml.controller;

import com.xml.model.Movie;
import com.xml.model.Movies;
import com.xml.service.MovieService;
import com.xml.utils.JAXBUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;
import java.math.BigInteger;

@Controller
public class PageController {

    private final MovieService movieService;

    public PageController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/trang-chu")
    public ModelAndView getAll(HttpServletRequest request, @RequestParam(defaultValue = "1") int page) {

        Page<Movie> moviePage = movieService.findMoviePerPage(page, 8);

        Movies movies = new Movies();
        movies.setMovie(moviePage.getContent());

        try {
            String xmlString = JAXBUtils.marshall(movies);
            request.setAttribute("RESULT", xmlString);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        request.setAttribute("PAGE", moviePage.getTotalPages());
        request.setAttribute("CURRENTPAGE", page);

        return new ModelAndView("home");
    }

    @GetMapping("/thong-tin/{id}")
    public ModelAndView getMovieDetail(HttpServletRequest request, @PathVariable("id") String id) {

        Movie movie = movieService.getById(new BigInteger(id));

        try {
            String xmlString = JAXBUtils.marshall(movie);
            request.setAttribute("RESULT", xmlString);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        request.setAttribute("ID", id);
        return new ModelAndView("detail");
    }

    @GetMapping("/")
    public ModelAndView loginPage() {
        return new ModelAndView("login");
    }

    @GetMapping("/admin")
    public ModelAndView crawler() {
        return new ModelAndView("crawl");
    }

}
