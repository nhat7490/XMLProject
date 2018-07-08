
package com.xml.model;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.math.BigInteger;
import java.util.Objects;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Movie", propOrder = {
        "id",
        "title",
        "quality",
        "yearPublic",
        "actors",
        "director",
        "posterLink",
        "vkoolLink",
        "biluLink",
        "vkoolRate",
        "biluRate",
        "selfRate"
})
@XmlRootElement(name = "movie")
@Entity
public class Movie {

    @XmlElement(required = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private BigInteger id;

    @Column(name = "title", nullable = false, length = 255)
    @XmlElement(required = true)
    private String title;

    @Column(name = "quality", nullable = true, length = 255)
    @XmlElement(required = true)
    private String quality;

    @Column(name = "year_public", nullable = true)
    @XmlElement(name = "year_public", required = true)
    private BigInteger yearPublic;

    @Column(name = "actors", nullable = true, length = 255)
    @XmlElement(required = true)
    private String actors;

    @Column(name = "director", nullable = true, length = 255)
    @XmlElement(required = true)
    private String director;

    @Column(name = "poster_link", nullable = true, length = 255)
    @XmlElement(name = "poster_link", required = true)
    private String posterLink;

    @Column(name = "vkool_link", nullable = true, length = 255)
    @XmlElement(name = "vkool_link", required = true)
    private String vkoolLink;

    @Column(name = "bilutv_link", nullable = true, length = 255)
    @XmlElement(name = "bilu_link", required = true)
    private String biluLink;

    @Column(name = "vkool_rate", nullable = true, precision = 0)
    @XmlElement(name = "vkool_rate")
    private double vkoolRate;

    @Column(name = "bilutv_rate", nullable = true, precision = 0)
    @XmlElement(name = "bilu_rate")
    private double biluRate;

    @Column(name = "self_rate", nullable = true, precision = 0)
    @XmlElement(name = "self_rate")
    private double selfRate;

    /**
     * Gets the value of the id property.
     *
     * @return possible object is
     * {@link BigInteger }
     */
    public BigInteger getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     *
     * @param value allowed object is
     *              {@link BigInteger }
     */
    public void setId(BigInteger value) {
        this.id = value;
    }

    /**
     * Gets the value of the title property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the value of the title property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTitle(String value) {
        this.title = value;
    }

    /**
     * Gets the value of the quality property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getQuality() {
        return quality;
    }

    /**
     * Sets the value of the quality property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setQuality(String value) {
        this.quality = value;
    }

    /**
     * Gets the value of the yearPublic property.
     *
     * @return possible object is
     * {@link BigInteger }
     */
    public BigInteger getYearPublic() {
        return yearPublic;
    }

    /**
     * Sets the value of the yearPublic property.
     *
     * @param value allowed object is
     *              {@link BigInteger }
     */
    public void setYearPublic(BigInteger value) {
        this.yearPublic = value;
    }

    /**
     * Gets the value of the actors property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getActors() {
        return actors;
    }

    /**
     * Sets the value of the actors property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setActors(String value) {
        this.actors = value;
    }

    /**
     * Gets the value of the director property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getDirector() {
        return director;
    }

    /**
     * Sets the value of the director property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDirector(String value) {
        this.director = value;
    }

    /**
     * Gets the value of the posterLink property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getPosterLink() {
        return posterLink;
    }

    /**
     * Sets the value of the posterLink property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPosterLink(String value) {
        this.posterLink = value;
    }

    /**
     * Gets the value of the vkoolLink property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getVkoolLink() {
        return vkoolLink;
    }

    /**
     * Sets the value of the vkoolLink property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setVkoolLink(String value) {
        this.vkoolLink = value;
    }

    /**
     * Gets the value of the biluLink property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getBiluLink() {
        return biluLink;
    }

    /**
     * Sets the value of the biluLink property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setBiluLink(String value) {
        this.biluLink = value;
    }

    /**
     * Gets the value of the vkoolRate property.
     */
    public double getVkoolRate() {
        return vkoolRate;
    }

    /**
     * Sets the value of the vkoolRate property.
     */
    public void setVkoolRate(double value) {
        this.vkoolRate = value;
    }

    /**
     * Gets the value of the biluRate property.
     */
    public double getBiluRate() {
        return biluRate;
    }

    /**
     * Sets the value of the biluRate property.
     */
    public void setBiluRate(double value) {
        this.biluRate = value;
    }

    /**
     * Gets the value of the selfRate property.
     */
    public double getSelfRate() {
        return selfRate;
    }

    /**
     * Sets the value of the selfRate property.
     */
    public void setSelfRate(double value) {
        this.selfRate = value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return id == movie.id &&
                Objects.equals(title, movie.title) &&
                Objects.equals(quality, movie.quality) &&
                Objects.equals(yearPublic, movie.yearPublic) &&
                Objects.equals(actors, movie.actors) &&
                Objects.equals(director, movie.director) &&
                Objects.equals(posterLink, movie.posterLink) &&
                Objects.equals(vkoolLink, movie.vkoolLink) &&
                Objects.equals(biluLink, movie.biluLink) &&
                Objects.equals(vkoolRate, movie.vkoolRate) &&
                Objects.equals(biluRate, movie.biluRate) &&
                Objects.equals(selfRate, movie.selfRate);
    }

}
