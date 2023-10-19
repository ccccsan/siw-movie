package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.model.User;

public interface ReviewRepository extends CrudRepository<Review, Long> {

    //public boolean existsByMovie(Movie movie);

//    public List<Review> getReviewsByReviewerId(Long reviewerId);

//    public List<Review> getReviewsByMovieId(Long movieId);
	
	public boolean existsByReviewerAndMovie(User reviewer, Movie movie);

    public Review findByMovieId(Long movieId);

    public List<Review> findByOrderByRateAsc();

    public List<Review> findAllByMovieId(Long movieId);
}
