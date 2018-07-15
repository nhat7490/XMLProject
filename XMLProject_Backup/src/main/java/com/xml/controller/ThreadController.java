package com.xml.controller;

import com.xml.service.CrawlService;
import com.xml.service.MovieService;
import com.xml.validator.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ThreadController {

    private final MovieService movieService;
    private final Validate validate;
    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadController.class);

    public ThreadController(MovieService movieService, Validate validate) {
        this.movieService = movieService;
        this.validate = validate;
    }

    @GetMapping(value = "/crawl")
    public ModelAndView CrawlingControl(HttpServletRequest request, @RequestParam("option") String option) {
        CrawlService crawlService = new CrawlService(movieService, validate);
        if(!CrawlService.check){
            if (option.equals("run")) {
                CrawlService.flag = true;
                CrawlService.check = true;
                crawlService.run();

            }
        }else {
            if (option.equals("run")) {
                try {
                    Thread.sleep(180000);

                } catch (InterruptedException e) {
                    LOGGER.error("THREAD INTERRUPTED");
                }
                CrawlService.flag = true;
                CrawlService.check = true;
                crawlService.run();

            }
            else if (option.equals("pause")) {
                CrawlService.flag = false;
            }
        }


        return new ModelAndView("crawl");
    }
}
