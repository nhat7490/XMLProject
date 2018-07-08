package com.xml.repository;

import com.xml.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface MovieRepository extends JpaRepository<Movie, Integer> {

    Movie findByTitle(String title);

    @Override
    Page<Movie> findAll(Pageable pageable);

    Movie findById(BigInteger id);
}
