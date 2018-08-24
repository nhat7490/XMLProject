package com.xml.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import com.xml.repository.MovieRepository;
import com.xml.model.Movie;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;

@Component
public class MovieService {

    public final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public void saveAll(Set<Movie> movies) {
        this.movieRepository.saveAll(movies);
    }

    public Movie findByTitle(String title) {
        return this.movieRepository.findByTitle(title);
    }

    public void save(Movie movie) {
        this.movieRepository.save(movie);
    }

    public List<Movie> findAll() {
        return this.movieRepository.findAll();
    }

    public Page<Movie> findMoviePerPage(int page, int moviePerPage) {
        int actualPage = page - 1;
        Pageable pageable = PageRequest.of(actualPage, moviePerPage);
        return movieRepository.findAll(pageable);
    }

    public Movie getById(BigInteger id) {
        return movieRepository.findById(id);
    }

    public Page<Movie> findMovieByTitle(int page, int moviePerPage, String search) {
        int actualPage = page - 1;
        Pageable pageable = PageRequest.of(actualPage, moviePerPage);
        return movieRepository.findByTitleLike(pageable, "%" + search + "%");
    }

    public void flush(){
        this.movieRepository.flush();
    }

}
