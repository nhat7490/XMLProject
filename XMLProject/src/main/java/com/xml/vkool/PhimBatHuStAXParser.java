package com.xml.vkool;

import com.xml.crawler.Crawler;
import com.xml.model.Category;
import com.xml.model.Movie;
import com.xml.model.MovieHasCategory;
import com.xml.service.CategoryService;
import com.xml.service.MovieHasCategoryService;
import com.xml.service.MovieService;
import com.xml.validator.Validate;
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

public class PhimBatHuStAXParser {

    private final MovieService movieService;
    private final CategoryService categoryService;
    private final MovieHasCategoryService movieHasCategoryService;

    private final Validate validate;
    private static final String SCHEMA = "static/xslt/Movies.xsd";
    public static List<String> categories = null;

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PhimBatHuStAXParser.class);

    public PhimBatHuStAXParser(MovieService movieService, CategoryService categoryService, MovieHasCategoryService movieHasCategoryService, Validate validate) {
        this.movieService = movieService;
        this.categoryService = categoryService;
        this.movieHasCategoryService = movieHasCategoryService;
        this.validate = validate;
    }

    public String subDomainStAXCursorParser(InputStream is) throws XMLStreamException {

        String begin = "adspruce-bannerspot";
        String end = "</ul>";
        String url = "";

        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.IS_VALIDATING, false);

        XMLStreamReader reader = factory.createXMLStreamReader(is);

        while (reader.hasNext()) {
            int cursor = reader.next();
            if (cursor == XMLStreamConstants.START_ELEMENT) {
                String tagName = reader.getLocalName();
                if (tagName.equals("a")) {
                    String aTagValue = getNodeStaXValue(reader, "a", "", "title");
                    if (aTagValue.equals("Phim lẻ")) {
                        url = "http://phimbathu.com" + getNodeStaXValue(reader, "a", "", "href");
                    }
                }
            }
        }
        return url;
    }

    public void movieStAXCursorParser(InputStream is) throws XMLStreamException {

        String begin = "list-film";
        String end = "pagination";
        String url = "";

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
                if (tagName.equals("span")) {
                    String spanTagValue = getNodeStaXValue(reader, "span", "", "class");
                    if (spanTagValue != null && spanTagValue.equals("label")) {
                        reader.next();
                        quality = reader.getText().trim();
                        if (quality.contains("+")){
                            quality=quality.split("\\+")[0].trim();
                        }else if (quality.contains("-")){
                            quality=quality.split("-")[0].trim();
                        }
                        movie.setQuality(quality);
                        foundQuality = true;
                    }
                }

                //title, link
                if (tagName.equals("a")) {
                    title = getNodeStaXValue(reader, "a", "", "title").trim();
                    url = "http://phimbathu.com" + getNodeStaXValue(reader, "a", "", "href").trim();

                    movie.setTitle(title);
                    movie.setVkoolLink(url);

                    foundLink = true;
                    foundTitle = true;
                }

                //image
                if (tagName.equals("img")) {
                    image = getNodeStaXValue(reader, "img", "", "src");
                    movie.setPosterLink(image);
                    foundPoster = true;
                }

                //go to info
                if (foundQuality && foundTitle && foundLink && foundPoster) {
//                    System.out.println(url);
                    Crawler.parseHTML(url, "dinfo", "class=\"clear\"");
                    String htmlInfo = Crawler.htmlContent;
                    htmlInfo = htmlInfo.split("<div class=\"box-btn clear\">")[0]
                            .replaceAll("itemscope", "")
                            .replaceAll("<input id=\"film_id\" type=\"hidden\" value=\"[0-9]+\">", "");

//                    System.out.println(htmlInfo);

                    InputStream ist = new ByteArrayInputStream(htmlInfo.getBytes(StandardCharsets.UTF_8));
                    categories = new ArrayList<>();

                    try {
                        movieInfo = movieInfoStAXCursorParser(ist);
                        movie.setDirector(movieInfo.getDirector());
                        movie.setVkoolRate(movieInfo.getVkoolRate());
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
                                        if (categoryService.findCateByNameLike(cate) == null) {
                                            Category category = new Category();
                                            category.setName(cate.trim());
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
                                    e.printStackTrace();
                                    LOGGER.error("Failed to save to db");
                                }
                            } else {
                                existedMovie.setPosterLink(movie.getPosterLink());
                                existedMovie.setVkoolLink(movie.getVkoolLink());
                                existedMovie.setVkoolRate(movie.getVkoolRate());
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
                        LOGGER.error("PHIMBATHU MOVIE " + movie.getTitle() + " DETAIL WELLFORM ERROR");
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

    public Movie movieInfoStAXCursorParser(InputStream is) throws XMLStreamException {

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
                        categories.add(category.trim());
                    }
                }

                //year
                if (tagName.equals("dt")) {
                    reader.next();
                    String value = reader.getText();
                    if (value.equals("Ngày xuất bản:")) {
                        reader.nextTag();
                        reader.nextTag();
                        reader.next();
                        int length = reader.getText().split("/").length;
                        if (length > 2) {
                            year = new BigInteger(reader.getText().split("/")[2].trim());
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
                        movie.setVkoolRate(rate);
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
