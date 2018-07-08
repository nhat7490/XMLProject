package com.xml.service;

import com.xml.bilutv.StAXParserBilutv;
import com.xml.crawler.Crawler;
import com.xml.model.Movie;
import com.xml.validator.Validate;
import com.xml.vkool.StAXParserVkool;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CrawlService extends Thread {

    private final MovieService movieService;

    private final Validate validate;

    public static boolean flag = false;

    public CrawlService(MovieService movieService, Validate validate) {
        this.movieService = movieService;
        this.validate = validate;
    }

    private static final String SCHEMA = "static/xslt/movie.xsd";

    @Override
    public void run() {
        while (flag) {
            String phimmoiHtml = "http://www.phimmoi.com/phim-le/";
            String phimmoiBegin = "<ul class=\"list-movie\">";
            String phimmoiEnd = "</ul>";

            String vkoolHtml = "http://vkool.net/list/phim-le/";
            String vkoolBegin = "ul class=\"list-movie\"";
            String vkoolEnd = "ad_728";

            List<Movie> vkoolList = new ArrayList<>();

            List<Movie> phimmoiList = new ArrayList<>();


            for (int i = 1; i <= 20; i++) {
                String url = phimmoiHtml + "page/" + i;

                Crawler.parseHTML(url, phimmoiBegin, phimmoiEnd);
                String htmlContent = Crawler.htmlContent + "</ul>";
//                System.out.println("------------PHIM MOI INFO------------");
                InputStream is = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
                try {
                    phimmoiList.addAll(StAXParserBilutv.StAXCursorParserBilutv(is));
                } catch (XMLStreamException ex) {
                    System.out.println("PHIMMOI WELFORM ERROR");
                }
                System.out.println("Phimm moi page: " + i);
                while(!flag){ }
            }

            Set<Movie> list = new HashSet<>();

            for (int i = 1; i <= 20; i++) {
                String url = vkoolHtml + "" + i;

                Crawler.parseHTML(url, vkoolBegin, vkoolEnd);
                String htmlContent = Crawler.htmlContent;
                htmlContent = "<div>" + htmlContent;

                InputStream is = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
                try {
                    vkoolList.addAll(StAXParserVkool.StAXCursorParserVkool(is));
                } catch (XMLStreamException ex) {
                    System.out.println("VKOOL WELFORM ERROR");
                }
                System.out.println("Vkool page: " + i);
                while(!flag){ }
            }

            List<Movie> movieList = new ArrayList<>();
            List<Movie> temp = new ArrayList<>();
            movieList.addAll(phimmoiList);

            //check duplicate
            for (Movie vkoolMovie : vkoolList) {
                boolean found = false;
                for (Movie movie : movieList) {
                    if (vkoolMovie.getTitle().equals(movie.getTitle())) {
                        movie.setVkoolRate(vkoolMovie.getVkoolRate());
                        movie.setVkoolLink(vkoolMovie.getVkoolLink());
                        if (movie.getBiluRate() == 0
                                || vkoolMovie.getVkoolRate() == 0) {
                            movie.setSelfRate(0);
                        } else {
                            movie.setSelfRate((movie.getBiluRate() + vkoolMovie.getVkoolRate()) / 2);
                        }
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    temp.add(vkoolMovie);
                }
            }

            movieList.addAll(temp);

            // check if existed in db
            List<Movie> foundMovie = new ArrayList<>();
            for (Movie movie : movieList) {
                Movie singleMovie = movieService.findByTitle(movie.getTitle());
                if (singleMovie != null) {
                    singleMovie.setTitle(movie.getTitle());
                    singleMovie.setActors(movie.getActors());
                    singleMovie.setDirector(movie.getDirector());
                    singleMovie.setYearPublic(movie.getYearPublic());
                    singleMovie.setQuality(movie.getQuality());
                    singleMovie.setPosterLink(movie.getPosterLink());
                    singleMovie.setBiluRate(movie.getBiluRate());
                    singleMovie.setVkoolRate(movie.getVkoolRate());
                    singleMovie.setBiluLink(movie.getBiluLink());
                    singleMovie.setVkoolLink(movie.getVkoolLink());
                    singleMovie.setSelfRate(movie.getSelfRate());

                    movieService.save(singleMovie);
                    foundMovie.add(movie);
                }
            }

            movieList.removeAll(foundMovie);

            Set<Movie> setMovie = new HashSet<>();
            for (Movie movie : movieList) {
                setMovie.add(movie);
            }

            System.out.println("LIST MOVIE");
            for (Movie movieDetail : setMovie) {

                System.out.println("Title: " + movieDetail.getTitle());
                System.out.println("Director: " + movieDetail.getDirector());
                System.out.println("Actor: " + movieDetail.getActors());
                System.out.println("Vkool Rate: " + movieDetail.getVkoolRate());
                System.out.println("Phimmoi Rate: " + movieDetail.getBiluRate());
                System.out.println("Phimmoi Link: " + movieDetail.getBiluLink());
                System.out.println("Vkool Link: " + movieDetail.getVkoolLink());
                System.out.println("Poster: " + movieDetail.getPosterLink());
                System.out.println("Quality: " + movieDetail.getQuality());
                System.out.println("Year: " + movieDetail.getYearPublic());
                System.out.println("Rate: " + movieDetail.getSelfRate());
                System.out.println("-------------------------");
            }

//            movieService.saveAll(setMovie);
            for (Movie movie : setMovie) {
                movie.setId(BigInteger.valueOf(0));
                try {
                    if (movie.getVkoolLink() == null) {
                        movie.setVkoolLink("");
                    }
                    if (movie.getBiluLink() == null) {
                        movie.setBiluLink("");
                    }
                    validate.validateSchema(movie, SCHEMA);
                    movie.setId(null);
                    movieService.save(movie);
                } catch (SAXException | JAXBException | IOException e) {
                    System.out.println("Failed to validate " + movie.getTitle() + " because of quality: " + movie.getQuality());
                } catch (Exception e) {
                    System.out.println("lỗi db");
                }
            }
            System.out.println("SUCCESS");
            flag = false;
        }
    }
}
