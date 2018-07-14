package com.xml.repository;

import com.xml.model.Movie;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Integer> {

    Movie findByTitle(String title);

    Page<Movie> findAll(Pageable pageable);

    Movie findById(BigInteger id);

    Page<Movie> findByTitleLike(Pageable pageable, String search);
}
