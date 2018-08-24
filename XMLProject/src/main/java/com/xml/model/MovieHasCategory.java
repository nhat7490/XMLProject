package com.xml.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "movie_has_category", schema = "xml_project", catalog = "")
@IdClass(MovieHasCategoryPK.class)
public class MovieHasCategory {
    private int movieId;
    private int categoryId;

    @Id
    @Column(name = "movie_id")
    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    @Id
    @Column(name = "category_id")
    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieHasCategory that = (MovieHasCategory) o;
        return movieId == that.movieId &&
                categoryId == that.categoryId;
    }

    @Override
    public int hashCode() {

        return Objects.hash(movieId, categoryId);
    }
}
