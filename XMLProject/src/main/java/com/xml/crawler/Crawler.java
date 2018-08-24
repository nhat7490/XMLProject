/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xml.crawler;

import com.sun.org.apache.xerces.internal.util.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

/**
 * @author JunBu
 */
public class Crawler {

    public static String htmlContent = "";
    public static int pageCount = 0;

    private static final Logger LOGGER = LoggerFactory.getLogger(Crawler.class);

    public static void parseHTML(String uri, String beginSign, String endSign) {
        htmlContent = "";
        boolean isInside = false;
        int count = 0;
        try {
            URL url = new URL(uri);
            URLConnection con = url.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            InputStream is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

            String inputLine = null;
            while ((inputLine = br.readLine()) != null) {
//                System.out.println(inputLine);
                if (inputLine.contains(beginSign)) {
                    if (count == 0) {
                        isInside = true;
                    }
                    count++;
                }
                if (inputLine.contains(endSign)) {
                    isInside = false;
                }
                if (isInside) {
                    inputLine = inputLine.replaceAll("&", "");
                    inputLine = inputLine.replaceAll("itemscope", "");
                    htmlContent = htmlContent + inputLine + "\n";
//                    System.out.println(inputLine);
                }
            }
            is.close();
        } catch (URI.MalformedURIException ex) {
            LOGGER.error("CRAWLING " + uri + " ERROR");
        } catch (IOException ex) {
            LOGGER.error("CRAWLING " + uri + " ERROR");
        }
    }


}
