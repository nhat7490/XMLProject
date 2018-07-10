/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xml.vkool;

import com.xml.model.Movie;
import com.xml.crawler.Crawler;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author JunBu
 */

public class StAXParserVkool {

    private static String begin = "movie-meta-info";
    private static String end = "<script type=\"text/javascript\">";

    private static final Logger LOGGER = LoggerFactory.getLogger(StAXParserVkool.class);

    public static List<Movie> StAXCursorParserVkool(InputStream is) throws XMLStreamException {
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
            String uri = "";
            String name = "";
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


                //link,name
                if (tagName.equals("a")) {
                    uri = getNodeStaXValue(reader, "a", "", "href");

                    movie.setVkoolLink(uri.trim());
//                    System.out.println("Link: " + movie.getVkoolLink());

                    name = getNodeStaXValue(reader, "a", "", "title");
                    movie.setTitle(name.split("-")[0].trim());
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
                    image = background.split(";")[0].split("[(]")[1].replaceAll("[)]", "");
                    movie.setPosterLink(image.trim());
//                    System.out.println(movie.getPosterLink());
//                        System.out.println("Poster: " + movie.getPosterLink());
                    foundPoster = true;
                }

                //quality
                if (tagName.equals("span")
                        && getNodeStaXValue(reader, "span", "", "class") != null
                        && getNodeStaXValue(reader, "span", "", "class").equals("ribbon")) {
                    quality = getTextStaXValue(reader, "span");
                    movie.setQuality(quality.trim());
//                    System.out.println("Quality: " + movie.getQuality());
                    foundQuality = true;
                }
            }

            if (foundLink && foundPoster && foundQuality && foundTitle) {
                try {
                    //get movie info
                    Crawler.parseHTML(movie.getVkoolLink(), begin, end);
//                    Crawler.parseHTML("http://vkool.net/info/the-gioi-khung-long-vuong-quoc-sup-do-16757.html", begin, end);
                    String htmlContent = Crawler.htmlContent
                            .split("<div class=\"movie-meta-info\">")[1]
                            .split(" <div class=\"clear\"></div>")[0]
                            .replaceAll("<!-- / Thông tin phim -->", "");
                    htmlContent = "<root>" + htmlContent + "</root>";
//                    System.out.println(htmlContent);
                    htmlContent = htmlContent.replaceAll("</div></div></div></div>", "</div>")
                            .replaceAll("</div></div>", "</div>")
                            .replaceAll("<input(.*?)*>", "<input/>")
                            .replaceAll("<div class=\"block-trailer\" id=\"trailer\">", "")
                            .replaceAll("<div class=\"plus-at-trailer\">", "")
                            .replaceAll("<div class=\"col-6 movie-image\">", "");
//                    System.out.println(htmlContent);

                    InputStream info = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));

                    movieInfo = StAXCursorParserInfo(info);
                    movie.setDirector(movieInfo.getDirector());
                    movie.setVkoolRate(movieInfo.getVkoolRate());
                    movie.setActors(movieInfo.getActors());
                    movie.setYearPublic(movieInfo.getYearPublic());

//                    System.out.println(movie.getDirector());

                    foundDirector = true;
                    foundRate = true;
                    foundActor = true;
                    foundYear = true;


                    if (foundActor && foundDirector && foundLink && foundPoster
                            && foundQuality && foundRate && foundTitle && foundYear) {

                        movies.add(movie);
                        foundTitle = false;
                        foundDirector = false;
                        foundActor = false;
                        foundRate = false;
                        foundLink = false;
                        foundPoster = false;
                        foundQuality = false;
                        foundYear = false;
                    }

                } catch (XMLStreamException e) {
                    LOGGER.error("VKOOL MOVIE " + movie.getTitle() + " DETAIL WELLFORM ERROR");
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

//        System.out.println("VKOOL MOVIE");
//        for (Movie movieDetail : model) {
//            System.out.println("Title: " + movieDetail.getTitle());
//            System.out.println("Director: " + movieDetail.getDirector());
//            System.out.println("Actor: " + movieDetail.getActors());
//            System.out.println("Vkool Rate: " + movieDetail.getVkoolRate());
//            System.out.println("Link: " + movieDetail.getVkoolLink());
//            System.out.println("Poster: " + movieDetail.getPosterLink());
//            System.out.println("Quality: " + movieDetail.getQuality());
//            System.out.println("Year: " + movieDetail.getYearPublic());
//            System.out.println("---------------------------------");
//        }
        return movies;
    }

    public static Movie StAXCursorParserInfo(InputStream is) throws XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.IS_VALIDATING, false);

        boolean foundQuality = false;
        boolean foundRate = false;
        double rate = 0.0;

        BigInteger year;

        List<String> actors = new ArrayList<>();
        List<String> directors = new ArrayList<>();

        Movie movie = new Movie();

        XMLStreamReader reader = factory.createXMLStreamReader(is);
        while (reader.hasNext()) {

            int cursor = reader.next();
            if (cursor == XMLStreamConstants.START_ELEMENT) {
                String tagName = reader.getLocalName();
//                System.out.println(tagName);
                //director
                if (tagName.equals("a")
                        && getNodeStaXValue(reader, "a", "", "href")
                        .contains("http://vkool.net/director")) {
                    String director = getTextStaXValue(reader, "a");
//                    System.out.println(director);
                    directors.add(director.trim());
                }

                //rate
                if (tagName.equals("dd")
                        && !foundRate
                        && getNodeStaXValue(reader, "dd", "", "class") != null
                        && getNodeStaXValue(reader, "dd", "", "class").equals("movie-dd imdb")) {
                    rate = Double.parseDouble(getTextStaXValue(reader, "dd").trim());
                    foundRate = true;
                    movie.setVkoolRate(rate);
                }

                //actor
                if (tagName.equals("a")
                        && getNodeStaXValue(reader, "a", "", "href")
                        .contains("http://vkool.net/actor")) {
                    String actorName = getTextStaXValue(reader, "a");
                    actors.add(actorName.trim());
                }


                //year
                if (tagName.equals("a")
                        && getNodeStaXValue(reader, "a", "", "class") != null
                        && getNodeStaXValue(reader, "a", "", "class").equals("nobr")) {
                    year = new BigInteger(getTextStaXValue(reader, "a").trim());
                    movie.setYearPublic(year);
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

    public static void main(String[] args) {
//
//        String a = "quan ca phe";
//        String b = "ca phe pho";
//
//        int n = a.length();
//        System.out.println("n: " + n);
//        int m = b.length();
//        System.out.println("m: " + m);
//
//        int dp[][] = new int[n + 1][m + 1];
//
//        for (int i = 0; i < n; i++) {
//            for (int j = 0; j < m; j++) {
//                if (a.charAt(i) == b.charAt(j)) {
//                    dp[i + 1][j + 1] = dp[i][j] + 1;
//                } else {
//                    dp[i + 1][j + 1] = Math.max(dp[i + 1][j], dp[i][j + 1]);
//                }
//            }
//
//        }
//        System.out.println(Math.max(5,10));
//        System.out.println(dp[n][m]);

        System.out.println("List values .....");
        List<String> list = new ArrayList<String>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("1");

        for (String temp : list) {
            System.out.println(temp);
        }

        Set<String> set = new HashSet<>(list);

        System.out.println("Set values .....");
        for (String temp : set) {
            System.out.println(temp);
        }

    }
}
