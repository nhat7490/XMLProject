package com.xml.controller;

import com.xml.service.CrawlService;
import com.xml.service.MovieService;
import com.xml.validator.Validate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ThreadController {

    private final MovieService movieService;
    private final Validate validate;

    public ThreadController(MovieService movieService, Validate validate) {
        this.movieService = movieService;
        this.validate = validate;
    }

    @GetMapping(value = "/crawl", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity CrawlingControl(@RequestParam("option") String option) {
        CrawlService crawlService = new CrawlService(movieService, validate);
        if (option.equalsIgnoreCase("run")) {
            CrawlService.flag = true;
            crawlService.run();
            CrawlService.flag = false;
            try {
                Thread.sleep(604800);
                CrawlService.flag = true;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else if(option.equalsIgnoreCase("stop")){
            CrawlService.flag = false;
        }
        return ResponseEntity.status(HttpStatus.OK).body("");
    }
}
