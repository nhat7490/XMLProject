package com.xml.controller;

import com.xml.service.CategoryService;
import com.xml.service.CrawlService;
import com.xml.service.MovieHasCategoryService;
import com.xml.service.MovieService;
import com.xml.validator.Validate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ThreadController {

    private final MovieService movieService;
    private final Validate validate;
    private final CategoryService categoryService;
    private final MovieHasCategoryService movieHasCategory;

    public ThreadController(MovieService movieService, Validate validate, CategoryService categoryService, MovieHasCategoryService movieHasCategory) {
        this.movieService = movieService;
        this.validate = validate;
        this.categoryService = categoryService;
        this.movieHasCategory = movieHasCategory;
    }

    @GetMapping(value = "/crawl", produces = MediaType.APPLICATION_XML_VALUE)
    public ModelAndView CrawlingControl(HttpServletRequest request, @RequestParam("option") String option) {
        CrawlService crawlService = new CrawlService(movieService, categoryService, movieHasCategory, validate);
            if (option.equals("run")) {
                CrawlService.flag = true;
                crawlService.run();

            }
            else if (option.equals("pause")) {
                CrawlService.flag = false;
            }

        return new ModelAndView("crawl");

    }
}
