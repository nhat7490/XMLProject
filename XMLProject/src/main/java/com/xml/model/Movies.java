package com.xml.model;


import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Movies", propOrder = {
        "movie"
})
@XmlRootElement(name = "movies")
public class Movies {

    @XmlElement(name = "movie", required = true)
    protected List<Movie> movie;

    public List<Movie> getMovie() {
        if (movie == null) {
            movie = new ArrayList<>();
        }
        return this.movie;
    }

    public void setMovie(List<Movie> movie) {
        this.movie = movie;
    }
}
