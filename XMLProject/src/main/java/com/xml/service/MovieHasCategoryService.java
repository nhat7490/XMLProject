package com.xml.service;

import com.xml.model.MovieHasCategory;
import com.xml.repository.MovieHasCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieHasCategoryService {
    public final MovieHasCategoryRepository movieHasCategoryRepository;

    public MovieHasCategoryService(MovieHasCategoryRepository movieHasCategoryRepository) {
        this.movieHasCategoryRepository = movieHasCategoryRepository;
    }

    public void save(MovieHasCategory movieHasCategory) {
        this.movieHasCategoryRepository.save(movieHasCategory);
    }

    public List<MovieHasCategory> findAllByMovieId(int id) {
        return this.movieHasCategoryRepository.findAllByMovieId(id);
    }
}
