package com.xml.controller;

import com.xml.model.Movie;
import com.xml.model.Movies;
import com.xml.service.CrawlService;
import com.xml.service.MovieService;
import com.xml.utils.JAXBUtils;
import com.xml.validator.Validate;
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

    private final Validate validate;

    public PageController(MovieService movieService, Validate validate) {
        this.movieService = movieService;
        this.validate = validate;
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

    @GetMapping("/tim-kiem")
    public ModelAndView getByTitle(HttpServletRequest request,
                                   @RequestParam String search,
                                   @RequestParam(defaultValue = "1") String pageNo) {

        Page<Movie> moviePage = movieService.findMovieByTitle(Integer.parseInt(pageNo), 8, search);

        Movies movies = new Movies();
        movies.setMovie(moviePage.getContent());

        try {
            String xmlString = JAXBUtils.marshall(movies);
            request.setAttribute("RESULT", xmlString);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        request.setAttribute("PAGE", moviePage.getTotalPages());
        request.setAttribute("CURRENTPAGE", pageNo);
        request.setAttribute("SEARCHVALUE", search);

        return new ModelAndView("search");
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
