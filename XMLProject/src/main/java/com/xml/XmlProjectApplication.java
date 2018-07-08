package com.xml;

import com.xml.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class XmlProjectApplication {

    private static final String SCHEMA = "static/xslt/Movies.xsd";

    public static void main(String[] args) {
        SpringApplication.run(XmlProjectApplication.class, args);
    }

    @Bean
    @Autowired
    CommandLineRunner init(MovieService movieService) {
        return args -> {
//            String phimmoiHtml = "http://www.phimmoi.com/phim-le/";
//            String phimmoiBegin = "<ul class=\"list-movie\">";
//            String phimmoiEnd = "</ul>";
//
//            String vkoolHtml = "http://vkool.net/list/phim-le/";
//            String vkoolBegin = "ul class=\"list-movie\"";
//            String vkoolEnd = "ad_728";
//
//            List<Movie> vkoolList = new ArrayList<>();
//
//            List<Movie> phimmoiList = new ArrayList<>();
//
//
//            for (int i = 1; i <= 20; i++) {
//                String url = phimmoiHtml + "page/" + i;
//
//                Crawler.parseHTML(url, phimmoiBegin, phimmoiEnd);
//                String htmlContent = Crawler.htmlContent + "</ul>";
//                System.out.println("------------PHIM MOI INFO------------");
//                System.out.println(htmlContent);
//                System.out.println("Phimm moi page: " + i);
//                InputStream is = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
//                try {
//                    phimmoiList.addAll(StAXParserBilutv.StAXCursorParserBilutv(is));
//                } catch (XMLStreamException ex) {
//                    Logger.getLogger(com.xml.vkool.MainCrawl.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//
//            Set<Movie> list = new HashSet<>();
//
//            for (Movie movie : phimmoiList) {
//
//            }
//
//            for (int i = 1; i <= 20; i++) {
//                String url = vkoolHtml + "" + i;
//
//                Crawler.parseHTML(url, vkoolBegin, vkoolEnd);
//                String htmlContent = Crawler.htmlContent;
//                htmlContent = "<div>" + htmlContent;
//
//                InputStream is = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
//                try {
//                    vkoolList.addAll(StAXParserVkool.StAXCursorParserVkool(is));
//                } catch (XMLStreamException ex) {
//                    Logger.getLogger(com.xml.vkool.MainCrawl.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                System.out.println("Vkool page: " + i);
//            }
//
//            List<Movie> movieList = new ArrayList<>();
//            List<Movie> temp = new ArrayList<>();
//            movieList.addAll(phimmoiList);
//
//            //check duplicate
//            for (Movie vkoolMovie : vkoolList) {
//                boolean found = false;
//                for (Movie movie : movieList) {
//                    if (vkoolMovie.getTitle().equals(movie.getTitle())) {
//                        movie.setVkoolRate(vkoolMovie.getVkoolRate());
//                        movie.setVkoolLink(vkoolMovie.getVkoolLink());
//                        if (movie.getBiluRate() == 0
//                                || vkoolMovie.getVkoolRate() == 0) {
//                            movie.setSelfRate(0);
//                        } else {
//                            movie.setSelfRate((movie.getBiluRate() + vkoolMovie.getVkoolRate()) / 2);
//                        }
//                        found = true;
//                        break;
//                    }
//
//                }
//                if (!found) {
//                    temp.add(vkoolMovie);
//                }
//            }
//
//            movieList.addAll(temp);
//
//            // check if existed in db
//            List<Movie> foundMovie = new ArrayList<>();
//            for (Movie movie : movieList) {
//                Movie singleMovie = movieService.findByTitle(movie.getTitle());
//                if (singleMovie != null) {
//                    singleMovie.setTitle(movie.getTitle());
//                    singleMovie.setActors(movie.getActors());
//                    singleMovie.setDirector(movie.getDirector());
//                    singleMovie.setYearPublic(movie.getYearPublic());
//                    singleMovie.setQuality(movie.getQuality());
//                    singleMovie.setPosterLink(movie.getPosterLink());
//                    singleMovie.setBiluRate(movie.getBiluRate());
//                    singleMovie.setVkoolRate(movie.getVkoolRate());
//                    singleMovie.setBiluLink(movie.getBiluLink());
//                    singleMovie.setVkoolLink(movie.getVkoolLink());
//                    singleMovie.setSelfRate(movie.getSelfRate());
//
//                    movieService.save(singleMovie);
//                    foundMovie.add(movie);
//                }
//            }
//
//            movieList.removeAll(foundMovie);
//
//            Set<Movie> setMovie = new HashSet<>();
//            Validate validate = new Validate();
//            for (Movie movie : movieList) {
//                setMovie.add(movie);
//            }
//
//            System.out.println("LIST MOVIE");
//            for (Movie movieDetail : setMovie) {
//
//                System.out.println("Title: " + movieDetail.getTitle());
//                System.out.println("Director: " + movieDetail.getDirector());
//                System.out.println("Actor: " + movieDetail.getActors());
//                System.out.println("Vkool Rate: " + movieDetail.getVkoolRate());
//                System.out.println("Phimmoi Rate: " + movieDetail.getBiluRate());
//                System.out.println("Phimmoi Link: " + movieDetail.getBiluLink());
//                System.out.println("Vkool Link: " + movieDetail.getVkoolLink());
//                System.out.println("Poster: " + movieDetail.getPosterLink());
//                System.out.println("Quality: " + movieDetail.getQuality());
//                System.out.println("Year: " + movieDetail.getYearPublic());
//                System.out.println("Rate: " + movieDetail.getSelfRate());
//                System.out.println("-------------------------");
//            }
//
////            movieService.saveAll(setMovie);
//            for (Movie movie : setMovie) {
//                movie.setId(BigInteger.valueOf(0));
//                try {
//                    if (movie.getVkoolLink() == null) {
//                        movie.setVkoolLink("");
//                    }
//                    if (movie.getBiluLink() == null) {
//                        movie.setBiluLink("");
//                    }
//                    validate.validateSchema(movie, SCHEMA);
//                    movie.setId(null);
//                    movieService.save(movie);
//                } catch (SAXException | JAXBException | IOException e) {
//                    System.out.println("Failed to validate " + movie.getTitle() + " because of quality: " + movie.getQuality());
//                } catch (Exception e) {
//                    System.out.println("lỗi db");
//                }
//            }
//            System.out.println("success");


//            Movie movie = new Movie();
//            movie.setId(BigInteger.valueOf(1));
//            movie.setTitle("1");
//            movie.setQuality("SD");
//            movie.setVkoolRate(5.0);
//            movie.setBiluRate(5.0);
//            movie.setYearPublic(BigInteger.valueOf(1));
//            movie.setActors("");
//            movie.setDirector("");
//            movie.setPosterLink("");
//            movie.setVkoolLink("");
//            movie.setBiluLink("");
//
//            Movie movie1 = new Movie();
//            movie1.setId(BigInteger.valueOf(2));
//            movie1.setTitle("2");
//            movie1.setQuality("SD - FULL");
//            movie1.setVkoolRate(5.0);
//            movie1.setBiluRate(5.0);
//            movie1.setYearPublic(BigInteger.valueOf(1));
//            movie1.setActors("");
//            movie1.setDirector("");
//            movie1.setPosterLink("");
//            movie1.setVkoolLink("");
//            movie1.setBiluLink("");
//
//            Movie movie2 = new Movie();
//            movie2.setTitle("3");
//            movie2.setQuality("HD 720p");
//            movie2.setId(BigInteger.valueOf(3));
//            movie2.setVkoolRate(5.0);
//            movie2.setBiluRate(5.0);
//            movie2.setYearPublic(BigInteger.valueOf(1));
//            movie2.setActors("");
//            movie2.setDirector("");
//            movie2.setPosterLink("");
//            movie2.setVkoolLink("");
//            movie2.setBiluLink("");
//
//            Movie movie3 = new Movie();
//            movie3.setTitle("4");
//            movie3.setQuality("Phim Sắp Chiếu");
//            movie3.setId(BigInteger.valueOf(4));
//            movie3.setVkoolRate(5.0);
//            movie3.setBiluRate(5.0);
//            movie3.setYearPublic(BigInteger.valueOf(1));
//            movie3.setActors("");
//            movie3.setDirector("");
//            movie3.setPosterLink("");
//            movie3.setVkoolLink("");
//            movie3.setBiluLink("");
//
//
//            List<Movie>movieList=new ArrayList<>();
//            movieList.add(movie);
//            movieList.add(movie1);
//            movieList.add(movie2);
//            movieList.add(movie3);
//            Validate validate = new Validate();
//            int i = 0;
//            for (Movie movie4:movieList
//                 ) {
//                try {
//                    validate.validateSchema(movie4,SCHEMA);
//                } catch (SAXException e) {
////                    e.printStackTrace();
//                    System.out.println("Failed to validate movie " + movie4.getTitle());
//                } catch (JAXBException e) {
////                    e.printStackTrace();
//                    System.out.println("Failed to validate movie " + movie4.getTitle());
//                } catch (IOException e) {
////                    e.printStackTrace();
//                    System.out.println("Failed to validate movie " + movie4.getTitle());
//                }
//
//            }
//

        };
    }

}
