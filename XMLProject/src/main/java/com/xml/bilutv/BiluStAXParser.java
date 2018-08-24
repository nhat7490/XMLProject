/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.parse;

import sample.crawl.Crawler;
import sample.crawl.MainCrawl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;

/**
 * @author JunBu
 */
public class BiluStAXParser {

    public static final List categories = null;

    public static void subDomainStAXCursorParser(InputStream is) throws XMLStreamException {

        String begin = "list-film";
        String end = "pagination";

        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.IS_VALIDATING, false);

        XMLStreamReader reader = factory.createXMLStreamReader(is);

        while (reader.hasNext()) {
            int cursor = reader.next();
            if (cursor == XMLStreamConstants.START_ELEMENT) {
                String tagName = reader.getLocalName();
                if (tagName.equals("a") && getNodeStaXValue(reader, "a", "", "title").contains("phim lẻ")) {
                    for (int i = 1; i < 3; i++) {
                        String url = "http://bilutv.com"
                                + getNodeStaXValue(reader, "a", "", "href") + "?page="
                                + i;
//                        System.out.println(url);

                        Crawler.parseHTML(url, begin, end);
                        String html = Crawler.htmlContent;

                        html = html.replace("</form>", "").
                                replace("<div class=\"clear\"></div>", "");

                        InputStream ist = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));

                        try {
                            movieStAXCursorParser(ist);
                        } catch (XMLStreamException ex) {
                            Logger.getLogger(MainCrawl.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        }

    }

    public static void movieStAXCursorParser(InputStream is) throws XMLStreamException {

        String begin = "list-film";
        String end = "pagination";

        boolean foundTitle = false;
        boolean foundDirector = false;
        boolean foundActor = false;
        boolean foundRate = false;
        boolean foundLink = false;
        boolean foundPoster = false;
        boolean foundQuality = false;
        boolean foundYear = false;

        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.IS_VALIDATING, false);

        XMLStreamReader reader = factory.createXMLStreamReader(is);
        String uri = "";

        while (reader.hasNext()) {
            int cursor = reader.next();
            if (cursor == XMLStreamConstants.START_ELEMENT) {
                String tagName = reader.getLocalName();

                //quality
                if (tagName.equals("label")) {
                    String quality = getTextStaXValue(reader, "label");
                    System.out.println(quality);
                    foundQuality = true;
                }

                //uri
                if (tagName.equals("a")) {
                    uri = "http://bilutv.com" + getNodeStaXValue(reader, "a", "", "href");
                    System.out.println(uri);

                    String image = getNodeStaXValue(reader, "img", "", "src");
                    System.out.println(image);
                    foundLink = true;
                    foundPoster = true;
                }

                //title
                if (tagName.equals("p")
                        && getNodeStaXValue(reader, "p", "", "class").equals("name")) {
                    String title = getTextStaXValue(reader, "p").split(":")[0];
                    System.out.println(title);
                    foundTitle = true;
                }

                //got to info
                if (foundQuality && foundLink && foundPoster && foundTitle) {
                    Crawler.parseHTML(uri, "meta-data", "/<ul>");
                    String htmlInfo = Crawler.htmlContent;
                    htmlInfo = htmlInfo.split("<div class=\"clear\"></div>")[0]
                            .replaceAll("itemscope", "")
                            .split("<span id=\"hint\"></span>")[0]
                            + "</div></div></li></ul>";

//                    System.out.println(htmlInfo);
                    InputStream ist = new ByteArrayInputStream(htmlInfo.getBytes(StandardCharsets.UTF_8));

                    try {
                        movieInfoStAXCursorParser(ist);

                        foundDirector = true;
                        foundRate = true;
                        foundActor = true;
                        foundYear = true;

                        if (foundActor && foundDirector && foundLink && foundPoster
                                && foundRate && foundTitle && foundYear && foundQuality) {

                            foundTitle = false;
                            foundDirector = false;
                            foundActor = false;
                            foundRate = false;
                            foundLink = false;
                            foundPoster = false;
                            foundYear = false;
                            foundQuality = false;
                        }
                    } catch (XMLStreamException ex) {
                        Logger.getLogger(MainCrawl.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

            }
        }

    }


    public static void movieInfoStAXCursorParser(InputStream is) throws XMLStreamException {

        List<String> actors = new ArrayList<>();
        List<String> directors = new ArrayList<>();
        List<String> categories = new ArrayList<>();

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
                if (tagName.equals("label")) {
                    reader.next();
                    String value = reader.getText();
                    if (value.equals("Năm xuất bản:")) {
                        reader.nextTag();
                        reader.nextTag();
                        reader.next();
                        String year = reader.getText();
                        System.out.println(year);
                    }
                }

                //rate
                if (tagName.equals("span")) {
                    if (getNodeStaXValue(reader, "span", "", "itemprop") != null
                            && getNodeStaXValue(reader, "span", "", "itemprop").equals("ratingValue")) {
                        reader.next();
                        String rate = reader.getText();
                        System.out.println(rate);
                    }
                }
            }
        }
        System.out.println(String.join(",", directors));
        System.out.println(String.join(",", actors));
        System.out.println(String.join(",", categories));
        System.out.println("--------------------------------");

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
