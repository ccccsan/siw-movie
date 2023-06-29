package it.uniroma3.siw.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
	private String title;
    
    @NotNull
    @Min(1900)
    @Max(2023)
	private Integer year;
    
    @Column(nullable = true, length = 64)
    private ArrayList<String> photos;

	@ManyToOne
	private Artist director;
	
	@ManyToMany
	private Set<Artist> actors;

	@OneToMany(mappedBy="movie")
	private List<Review> reviews;

	
	public Movie() {
		this.photos = new ArrayList<>();
		this.reviews = new ArrayList<>();
	}

	@Transient
	public String getPhotosImagePath() {
		if (photos.isEmpty() || id == null) return null;
		return "/movie-photos/" + id + "/";
	}

	public List<String> getPhotos() {
		return photos;
	}

	public void addPhotos(String filename) {
		this.photos.add(filename);
	}

	public void setPhotos(ArrayList<String> photos) {
		this.photos = photos;
	}

	public List<Review> getReviews() {
		return reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Artist getDirector() {
		return director;
	}

	public void setDirector(Artist director) {
		this.director = director;
	}

	public Set<Artist> getActors() {
		return actors;
	}

	public void setActors(Set<Artist> actors) {
		this.actors = actors;
	}

	@Override
	public int hashCode() {
		return Objects.hash(title, year);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Movie other = (Movie) obj;
		return Objects.equals(title, other.title) && year.equals(other.year);
	}
}
