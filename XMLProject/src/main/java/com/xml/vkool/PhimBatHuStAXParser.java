package sample.parse;

import sample.crawl.Crawler;
import sample.crawl.MainCrawl;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PhimBatHuStAXParser {

    public static void subDomainStAXCursorParser(InputStream is, int page) throws XMLStreamException {

        String begin = "adspruce-bannerspot";
        String end = "</ul>";

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
                        String url = "http://phimbathu.com" + getNodeStaXValue(reader, "a", "", "href") + "?page=" + page;
                        System.out.println(url);
                        Crawler.parseHTML(url, begin, end);
                        String html = Crawler.htmlContent;
                        html = html.split("<div class=\"adspruce-bannerspot\"></div>")[1] + "</ul>";

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
        String url = "";

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

        while (reader.hasNext()) {
            int cursor = reader.next();
            if (cursor == XMLStreamConstants.START_ELEMENT) {
                String tagName = reader.getLocalName();

                //quality
                if (tagName.equals("span")) {
                    String spanTagValue = getNodeStaXValue(reader, "span", "", "class");
                    if (spanTagValue != null && spanTagValue.equals("label")) {
                        reader.next();
                        String quality = reader.getText();
                        System.out.println(quality);
                        foundQuality = true;
                    }
                }

                //title, link
                if (tagName.equals("a")) {
                    String title = getNodeStaXValue(reader, "a", "", "title");
                    url = "http://phimbathu.com" + getNodeStaXValue(reader, "a", "", "href");

                    System.out.println(title);
                    System.out.println(url);

                    foundLink = true;
                    foundTitle = true;
                }

                //image
                if (tagName.equals("img")) {
                    String img = getNodeStaXValue(reader, "img", "", "src");
                    System.out.println(img);
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

                    try {
                        movieInfoStAXCursorParser(ist);
                    } catch (XMLStreamException ex) {
                        Logger.getLogger(MainCrawl.class.getName()).log(Level.SEVERE, null, ex);
                        System.out.println(url);
                    }
                    foundQuality = false;
                    foundTitle = false;
                    foundLink = false;
                    foundPoster = false;
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
                if (tagName.equals("dt")) {
                    reader.next();
                    String value = reader.getText();
                    if (value.equals("Ngày xuất bản:")) {
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
