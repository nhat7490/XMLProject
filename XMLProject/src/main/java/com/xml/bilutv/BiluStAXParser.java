/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xml.bilutv;

import com.xml.crawler.Crawler;
import com.xml.model.Category;
import com.xml.model.Movie;
import com.xml.model.MovieHasCategory;
import com.xml.service.CategoryService;
import com.xml.service.MovieHasCategoryService;
import com.xml.service.MovieService;
import com.xml.validator.Validate;
import com.xml.vkool.PhimBatHuStAXParser;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author JunBu
 */
public class BiluStAXParser {

    private final MovieService movieService;
    private final CategoryService categoryService;
    private final MovieHasCategoryService movieHasCategoryService;

    private final Validate validate;
    private static final String SCHEMA = "static/xslt/Movies.xsd";
    public static List<String> categories = null;

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(BiluStAXParser.class);

    public BiluStAXParser(MovieService movieService, CategoryService categoryService, MovieHasCategoryService movieHasCategoryService, Validate validate) {
        this.movieService = movieService;
        this.categoryService = categoryService;
        this.movieHasCategoryService = movieHasCategoryService;
        this.validate = validate;
    }

    public static String subDomainStAXCursorParser(InputStream is) throws XMLStreamException {

        String begin = "list-film";
        String end = "pagination";
        String url = "";

        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.IS_VALIDATING, false);

        XMLStreamReader reader = factory.createXMLStreamReader(is);

        while (reader.hasNext()) {
            int cursor = reader.next();
            if (cursor == XMLStreamConstants.START_ELEMENT) {
                String tagName = reader.getLocalName();
                if (tagName.equals("a")
                        && getNodeStaXValue(reader, "a", "", "title").contains("phim lẻ")) {
                    url = "http://bilutv.com"
                            + getNodeStaXValue(reader, "a", "", "href");
                }
            }
        }
        return url;
    }

    public void movieStAXCursorParser(InputStream is) throws XMLStreamException {

        boolean foundTitle = false;
        boolean foundDirector = false;
        boolean foundActor = false;
        boolean foundRate = false;
        boolean foundLink = false;
        boolean foundPoster = false;
        boolean foundQuality = false;
        boolean foundYear = false;
        Movie movie = new Movie();
        Movie movieInfo = new Movie();
        List<Movie> movies = new ArrayList<Movie>();
        String url = "";

        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.IS_VALIDATING, false);

        XMLStreamReader reader = factory.createXMLStreamReader(is);

        while (reader.hasNext()) {
            String title = "";
            String image = "";
            String quality = "";
            if (!foundActor && !foundDirector && !foundLink && !foundPoster
                    && !foundQuality && !foundRate && !foundTitle && !foundYear) {
                movie = new Movie();
                movieInfo = new Movie();
            }
            int cursor = reader.next();
            if (cursor == XMLStreamConstants.START_ELEMENT) {
                String tagName = reader.getLocalName();

                //quality
                if (tagName.equals("label")) {
                    quality = getTextStaXValue(reader, "label").trim();
                    if (quality.contains("+")){
                        quality=quality.split("\\+")[0].trim();
                    }else if (quality.contains("-")){
                        quality=quality.split("-")[0].trim();
                    }
                    movie.setQuality(quality);
                    foundQuality = true;
                }

                //uri,poster
                if (tagName.equals("a")) {
                    url = "http://bilutv.com" + getNodeStaXValue(reader, "a", "", "href").trim();

                    image = getNodeStaXValue(reader, "img", "", "src").trim();
                    movie.setPosterLink(image);
                    movie.setBiluLink(url);
                    foundLink = true;
                    foundPoster = true;
                }

                //title
                if (tagName.equals("p")
                        && getNodeStaXValue(reader, "p", "", "class").equals("name")) {
                    title = getTextStaXValue(reader, "p").split(":")[0];
                    movie.setTitle(title);
                    foundTitle = true;
                }

                //go to info
                if (foundQuality && foundLink && foundPoster && foundTitle) {
                    Crawler.parseHTML(url, "meta-data", "/<ul>");
                    String htmlInfo = Crawler.htmlContent;
                    htmlInfo = htmlInfo.split("<div class=\"clear\"></div>")[0]
                            .replaceAll("itemscope", "")
                            .split("<span id=\"hint\"></span>")[0]
                            + "</div></div></li></ul>";

//                    System.out.println(htmlInfo);
                    InputStream ist = new ByteArrayInputStream(htmlInfo.getBytes(StandardCharsets.UTF_8));
                    categories = new ArrayList<>();

                    try {
                        movieInfo = movieInfoStAXCursorParser(ist);
                        movie.setDirector(movieInfo.getDirector());
                        movie.setBiluRate(movieInfo.getBiluRate());
                        movie.setActors(movieInfo.getActors());
                        movie.setYearPublic(movieInfo.getYearPublic());

                        foundDirector = true;
                        foundRate = true;
                        foundActor = true;
                        foundYear = true;

                        if (foundActor && foundDirector && foundLink && foundPoster
                                && foundQuality && foundRate && foundTitle && foundYear) {

                            Movie existedMovie = movieService.findByTitle(movie.getTitle());
                            List<Category> temp = new ArrayList<>();
                            if (existedMovie == null) {
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
                                    for (String cate : categories) {
                                        cate = cate.replaceAll("  "," ");
                                        if (categoryService.findCateByNameLike(cate) == null) {
                                            Category category = new Category();
                                            category.setName(cate);
                                            categoryService.save(category);
                                            int categorId = categoryService.findCateByNameLike(category.getName()).getId();
                                            MovieHasCategory movieHasCategory = new MovieHasCategory();
                                            movieHasCategory.setCategoryId(categorId);
                                            movieHasCategory.setMovieId(movieService.findByTitle(movie.getTitle()).getId().intValue());
                                            movieHasCategoryService.save(movieHasCategory);
                                        } else {
                                            int categoryId = categoryService.findCateByNameLike(cate).getId();
                                            MovieHasCategory movieHasCategory = new MovieHasCategory();
                                            movieHasCategory.setMovieId(movieService.findByTitle(movie.getTitle()).getId().intValue());
                                            movieHasCategory.setCategoryId(categoryId);
                                            movieHasCategoryService.save(movieHasCategory);
                                        }
                                    }
                                } catch (SAXException | JAXBException | IOException e) {
                                    LOGGER.error("Failed to validate " + movie.getTitle() + " because of quality: " + movie.getQuality());
                                } catch (Exception e) {
                                    LOGGER.error("Failed to save to db");
                                }
                            } else {
//                                existedMovie.setPosterLink(movie.getPosterLink());
                                existedMovie.setBiluLink(movie.getBiluLink());
                                existedMovie.setBiluRate(movie.getBiluRate());
                                existedMovie.setDirector(movie.getDirector());
                                existedMovie.setActors(movie.getActors());
                                existedMovie.setYearPublic(movie.getYearPublic());
                                existedMovie.setSelfRate((existedMovie.getBiluRate() + existedMovie.getVkoolRate()) / 2);

                                movieService.save(existedMovie);
                                categories = null;
                            }
                            foundTitle = false;
                            foundDirector = false;
                            foundActor = false;
                            foundRate = false;
                            foundLink = false;
                            foundPoster = false;
                            foundQuality = false;
                            foundYear = false;
                        }
                    } catch (XMLStreamException ex) {
                        LOGGER.error("BILU MOVIE " + movie.getTitle() + " DETAIL WELLFORM ERROR");
                        if (foundActor && foundDirector && foundLink && foundPoster
                                && foundQuality && foundRate && foundTitle && foundYear) {

                            foundTitle = false;
                            foundDirector = false;
                            foundActor = false;
                            foundRate = false;
                            foundLink = false;
                            foundPoster = false;
                            foundQuality = false;
                            foundYear = false;
                        }
                    }
                }

            }
        }

    }


    public static Movie movieInfoStAXCursorParser(InputStream is) throws XMLStreamException {

        List<String> actors = new ArrayList<>();
        List<String> directors = new ArrayList<>();
        Movie movie = new Movie();
        double rate = 0.0;
        BigInteger year = new BigInteger("0");

        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.IS_VALIDATING, false);

        XMLStreamReader reader = factory.createXMLStreamReader(is);

        while (reader.hasNext()) {
            int cursor = reader.next();
            if (cursor == XMLStreamConstants.START_ELEMENT) {
                String tagName = reader.getLocalName();

                //actor, director, category
                if (tagName.equals("a")) {
                    String aTag = getNodeStaXValue(reader, "a", "", "href");
                    if (aTag.contains("dien-vien")) {
                        reader.next();
                        String actorName = reader.getText();
                        actors.add(actorName.trim());
                    } else if (aTag.contains("dao-dien")) {
                        reader.next();
                        String directorName = reader.getText();
                        directors.add(directorName.trim());
                    } else if (aTag.contains("the-loai")) {
                        reader.next();
                        String category = reader.getText();
                        for (String cate : category.split("-")) {
                            categories.add(("Phim " + cate.toLowerCase())
                                    .replaceAll("  "," ")
                                    .replaceAll("phim","")
                                    .trim());
                        }
                    }
                }

                //year
                if (tagName.equals("label")) {
                    reader.next();
                    String value = reader.getText();
                    if (value.equals("Năm xuất bản:")) {
                        reader.nextTag();
                        reader.nextTag();
                        if (reader.next() != XMLStreamConstants.END_ELEMENT) {
                            try {
                                year = new BigInteger(reader.getText().trim());
                            } catch (NumberFormatException e) {
                                LOGGER.error("WRONG FORMAT FOR YEAR");
                            }
                        }

                        movie.setYearPublic(year);
                    }
                }

                //rate
                if (tagName.equals("span")) {
                    if (getNodeStaXValue(reader, "span", "", "itemprop") != null
                            && getNodeStaXValue(reader, "span", "", "itemprop").equals("ratingValue")) {
                        reader.next();
                        rate = Double.parseDouble(reader.getText().trim());
                        movie.setBiluRate(rate);
                    }
                }
            }
        }
        movie.setActors(String.join(",", actors));
        movie.setDirector(String.join(",", directors));
        return movie;
    }

    public static String getNodeStaXValue(XMLStreamReader reader,
                                          String elementName,
                                          String namespaceURI,
                                          String attrName) throws XMLStreamException {
        if (reader != null) {
            while (reader.hasNext()) {
                int cursor = reader.getEventType();
                if (cursor == XMLStreamConstants.START_ELEMENT) {
                    String tagName = reader.getLocalName();
                    if (tagName.equals(elementName)) {
                        String result = reader.getAttributeValue(namespaceURI, attrName);

                        return result;
                    }
                }
                reader.next();
            }
        }
        return null;
    }

    public static String getTextStaXValue(XMLStreamReader reader, String elementName) throws XMLStreamException {
        if (reader != null) {
            while (reader.hasNext()) {
                int cursor = reader.getEventType();
                if (cursor == XMLStreamConstants.START_ELEMENT) {
                    String tagName = reader.getLocalName();
                    if (tagName.equals(elementName)) {
                        reader.next();
                        String result = reader.getText();
                        reader.nextTag();

                        return result;
                    }
                }
                reader.next();
            }
        }
        return null;
    }
}
