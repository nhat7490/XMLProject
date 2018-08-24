package com.xml.repository;

import com.xml.model.MovieHasCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieHasCategoryRepository extends JpaRepository<MovieHasCategory, Integer> {

    List<MovieHasCategory> findAllByMovieId(int movieId);
}
