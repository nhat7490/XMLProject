package com.xml.bilutv;

import com.xml.crawler.Crawler;
import com.xml.model.Movie;
import com.xml.vkool.StAXParserVkool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class StAXParserBilutv {

    private static final Logger LOGGER = LoggerFactory.getLogger(StAXParserBilutv.class);

    private static String begin = "class=\"movie-info\"";
    private static String end = "<!-- / Thông tin phim -->";

    public static List<Movie> StAXCursorParserBilutv(InputStream is) throws XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.IS_VALIDATING, false);

        XMLStreamReader reader = factory.createXMLStreamReader(is);

        List<Movie> movies = new ArrayList<Movie>();

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

        while (reader.hasNext()) {

            if (!foundActor && !foundDirector && !foundLink && !foundPoster
                    && !foundQuality && !foundRate && !foundTitle && !foundYear) {
                movie = new Movie();
                movieInfo = new Movie();
            }
            int cursor = reader.next();
            if (cursor == XMLStreamConstants.START_ELEMENT) {
                String tagName = reader.getLocalName();

                //link, name
                if (tagName.equals("a")) {
                    String uri = getNodeStaXValue(reader, "a", "", "href");

                    movie.setBiluLink(uri.trim());
//                    System.out.println(movie.getBiluLink());
                    String name = getNodeStaXValue(reader, "a", "", "title")
                            .split("-")[0];
                    movie.setTitle(name.trim());

//                    System.out.println(movie.getTitle());

                    foundLink = true;
                    foundTitle = true;
                }

                //image
                if (tagName.equals("div")
                        && getNodeStaXValue(reader, "div", "", "class") != null
                        && getNodeStaXValue(reader, "div", "", "class")
                        .equals("movie-thumbnail")) {
                    String background = getNodeStaXValue(reader, "div", "", "style");
                    String image = background.split(";")[0].split("[(]")[1].replaceAll("[)]", "");
                    movie.setPosterLink(image.trim());
                    foundPoster = true;
                }

                if (foundLink && foundPoster && foundTitle) {

                    Crawler.parseHTML(movie.getBiluLink(), begin, end);
                    String htmlContent = Crawler.htmlContent;

//                    System.out.println("-----------PHIM MOI-----------------");
//
                    InputStream info = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));

                    movieInfo = StAXCursorParserInfo(info);

                    movie.setDirector(movieInfo.getDirector());
                    movie.setBiluRate(movieInfo.getBiluRate());
                    movie.setActors(movieInfo.getActors());
                    movie.setYearPublic(movieInfo.getYearPublic());
                    movie.setQuality(movieInfo.getQuality());


                    foundDirector = true;
                    foundRate = true;
                    foundActor = true;
                    foundYear = true;


                    if (foundActor && foundDirector && foundLink && foundPoster
                            && foundRate && foundTitle && foundYear) {

                        movies.add(movie);
                        foundTitle = false;
                        foundDirector = false;
                        foundActor = false;
                        foundRate = false;
                        foundLink = false;
                        foundPoster = false;
                        foundYear = false;
                    }

                }

            }

        }
//        for (Movie movieDetail : com.xml.model) {
//            System.out.println("Title: " + movieDetail.getTitle());
//            System.out.println("Director: " + movieDetail.getDirector());
//            System.out.println("Actor: " + movieDetail.getActors());
//            System.out.println("Vkool Rate: " + movieDetail.getBiluRate());
//            System.out.println("Link: " + movieDetail.getBiluLink());
//            System.out.println("Poster: " + movieDetail.getPosterLink());
//            System.out.println("Quality: " + movieDetail.getQuality());
//            System.out.println("Year: " + movieDetail.getYearPublic());
//            System.out.println("-------------------------");
//        }
        return movies;
    }

    public static Movie StAXCursorParserInfo(InputStream is) throws XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.IS_VALIDATING, false);

        boolean foundQuality = false;
        boolean foundRate = false;
        BigInteger year;


        List<String> actors = new ArrayList<>();
        List<String> directors = new ArrayList<>();

        Movie movie = new Movie();

        XMLStreamReader reader = factory.createXMLStreamReader(is);
        while (reader.hasNext()) {

            int cursor = reader.next();
            if (cursor == XMLStreamConstants.START_ELEMENT) {
                String tagName = reader.getLocalName();

                //actor
                if (tagName.equals("a")) {
                    String test = getNodeStaXValue(reader, "a", "", "class");
                    if (test != null && test.contains("actor-profile-item")) {
                        String actorName = getNodeStaXValue(reader, "a", "", "title");
                        actors.add(actorName.trim());
//                        System.out.println(actorName);
                    }
                }

                //director
                if (tagName.equals("dd")) {
                    String ddClass = getNodeStaXValue(reader, "dd", "", "class");
                    if (ddClass.contains("dd-director")) {
                        reader.nextTag(); //go to next tag
                        reader.next(); //go to text value
                        String directorName = reader.getText();
                        directors.add(directorName.trim());
//                        System.out.println(movie.getDirector());

                    }
                }

                //quality, year
                if (tagName.equals("dt")) {
                    String dtText = getTextStaXValue(reader, "dt");
                    if (dtText.contains("Độ phân giải")) {
                        reader.nextTag(); //go to next tag
                        reader.next(); //go to text value
                        String quality = reader.getText();
                        movie.setQuality(quality.trim());

//                        System.out.println(quality);
                    } else if (dtText.contains("Năm")) {
                        reader.nextTag(); //go to next tag
                        reader.next(); //go to text value
                        try {
                            year = new BigInteger(reader.getText().trim());
                            movie.setYearPublic(year);
                        } catch (Exception e) {
                            LOGGER.error("WONG FOR YEAR");
                        }

//                        System.out.println(movie.getYearPublic());
                    }
                }

                //rate
                double rate = 0.0;

                if (tagName.equals("img")) {
                    String imgTitle = getNodeStaXValue(reader, "img", "", "title");
                    if (imgTitle != null && imgTitle.equals("Tuyệt hay")) {
//                        System.out.println(imgTitle);

                        boolean flag = false;
                        while (!flag) {
                            reader.next();
                            cursor = reader.getEventType();
                            if (cursor == XMLStreamConstants.CHARACTERS) {
                                String voteFlag = reader.getText();
//                                System.out.println(votFlag);
                                if (voteFlag.contains("votes, average")) {
                                    reader.nextTag();
                                    reader.next();
                                    rate = Double.parseDouble(reader.getText().replaceAll(",", ".").trim());
                                    movie.setBiluRate(rate);
//                                    System.out.println(reader.getText());
                                    flag = true;
                                } else if (voteFlag.contains("No Ratings Yet")) {
                                    movie.setBiluRate(0.0);
//                                    System.out.println(reader.getText());
                                    flag = true;
                                }
                            }

                        }
                    }
                }
//                if (tagName.equals("div")) {
////                    reader.nextTag();
//                    String divClass = getNodeStaXValue(reader, "div", "", "class");
//                    String strongText = getTextStaXValue(reader, "strong");
//                    if (divClass != null && divClass.equals("post-rating") && strongText != null) {
//                        try {
//                            rate = Double.parseDouble(getTextStaXValue(reader, "strong").replaceAll(",", "."));
//                        } catch (NumberFormatException e) {
//                            rate = 0.0;
//                        }
//
//                    }
//                    movie.setBiluRate(rate);
//                }
//                if (tagName.equals("span")
//                        && getNodeStaXValue(reader, "span", "", "class") != null
//                        && getNodeStaXValue(reader, "span", "", "class").equals("average")) {
//                    rate = Double.parseDouble(getTextStaXValue(reader, "span"));
//                    movie.setBiluRate(rate);
//                    System.out.println(rate);
//                }
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
