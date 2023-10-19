package it.uniroma3.siw.service;

import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.MovieRepository;
import it.uniroma3.siw.repository.ReviewRepository;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CredentialsService credentialsService;

    @Transactional
    public void addReview(Review review) {
        this.reviewRepository.save(review);
    }

    @Transactional
    public Review getReviewById(Long id) {
        return this.reviewRepository.findById(id).get();
    }

    @Transactional
    public List<Review> getReviewsByRateAsc() {
        return this.reviewRepository.findByOrderByRateAsc();
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        Review review = this.getReviewById(reviewId);
        Movie movie = review.getMovie();
        review.getReviewer().getReviews().remove(review);
        review.getMovie().getReviews().remove(review);
        this.movieRepository.save(movie);
        this.reviewRepository.delete(review);
    }

    public Review saveReviewToUser(Long userId, Long reviewId) {
        Review review = this.getReviewById(reviewId);
        User user = this.userService.getUser(userId);
        List<Review> reviews = user.getReviews();
        reviews.add(review);
        user.setReviews(reviews);
        review.setReviewer(user);
        return this.reviewRepository.save(review);
    }

    @Transactional
    public Review newReview(@Valid Review review, Long movieId) {
        Movie movie = this.movieRepository.findById(movieId).get();
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Credentials credentials = credentialsService.getCredentials(user.getUsername());
        review.setMovie(movie);
        review.setReviewer(credentials.getUser());
        this.reviewRepository.save(review);
//        List<Review> reviews = movie.getReviews();
//        reviews.add(review);
//        this.movieRepository.save(movie);

        return review;
    }

    @Transactional
    public List<Review> findAllByMovieId(Long movieId) {
    	return this.reviewRepository.findAllByMovieId(this.movieRepository.findById(movieId).get().getId());
    }

}
