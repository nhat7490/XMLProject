package com.xml.service;

import com.xml.bilutv.BiluStAXParser;
import com.xml.crawler.Crawler;
import com.xml.model.Movie;
import com.xml.validator.Validate;
import com.xml.vkool.PhimBatHuStAXParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.xml.stream.XMLStreamException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class CrawlService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CrawlService.class);
    private final MovieService movieService;
    private final CategoryService categoryService;
    private final MovieHasCategoryService movieHasCategory;

    private final Validate validate;

    public static boolean flag = false;
    public static boolean check = false;

    public CrawlService(MovieService movieService, CategoryService categoryService, MovieHasCategoryService movieHasCategory, Validate validate) {
        this.movieService = movieService;
        this.categoryService = categoryService;
        this.movieHasCategory = movieHasCategory;
        this.validate = validate;
    }

    private static final String SCHEMA = "static/xslt/Movies.xsd";

    public void run() {
        while (flag) {
            String phimBatHuHtml = "http://phimbathu.com/";
            String phimBatHuBegin = "ul class=\"container\"";
            String phimBatHuEnd = "</div>";

            String biluHTML = "http://bilutv.com/";
            String biluBeginSign = "main-menu";
            String biluEndSign = "</div>";

            List<Movie> vkoolList = new ArrayList<>();
            List<Movie> phimBatHuList = new ArrayList<>();


            for (int i = 1; i <= 5 && flag; i++) {
                LOGGER.info("Crawling PhimBatHu page: " + i);
                Crawler.parseHTML(phimBatHuHtml, phimBatHuBegin, phimBatHuEnd);
                String htmlContent = Crawler.htmlContent;
                PhimBatHuStAXParser phimBatHuStAXParser = new PhimBatHuStAXParser(movieService, categoryService, movieHasCategory, validate);
                InputStream is = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
                try {
                    String url = phimBatHuStAXParser.subDomainStAXCursorParser(is) + "?page=" + i;
                    Crawler.parseHTML(url, "adspruce-bannerspot", "</ul>");
                    String html = Crawler.htmlContent;
                    html = html.split("<div class=\"adspruce-bannerspot\"></div>")[1] + "</ul>";

                    InputStream ist = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
                    phimBatHuStAXParser.movieStAXCursorParser(ist);
                } catch (XMLStreamException ex) {
                    LOGGER.error("PHIMBATHU WELLFORM ERROR, PAGE: " + i);
                }
            }

            for (int i = 1; i <= 5 && flag; i++) {
                LOGGER.info("Crawling Bilu page: " + i);
                Crawler.parseHTML(biluHTML, biluBeginSign, biluEndSign);
                String htmlContent = Crawler.htmlContent + "</div></div>";
                BiluStAXParser biluStAXParser = new BiluStAXParser(movieService, categoryService, movieHasCategory, validate);
                InputStream is = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
                try {
                    String url = biluStAXParser.subDomainStAXCursorParser(is) + "?page=" + i;
                    Crawler.parseHTML(url, "list-film", "pagination");
                    String html = Crawler.htmlContent;
                    html = html.replace("</form>", "").
                            replace("<div class=\"clear\"></div>", "");
                    InputStream ist = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
                    biluStAXParser.movieStAXCursorParser(ist);
                } catch (XMLStreamException ex) {
                    ex.printStackTrace();
                    LOGGER.error("BILU WELLFORM ERROR, PAGE: " + i);
                }
            }


            LOGGER.info("SAVE TO DB SUCCESSFUL");
            flag = false;
        }
    }
}
