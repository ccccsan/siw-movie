package it.uniroma3.siw.controller;

import java.util.List;

import javax.validation.Valid;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.service.CredentialsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.controller.validator.ReviewValidator;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.service.MovieService;
import it.uniroma3.siw.service.ReviewService;

@Controller
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private MovieService movieService;

    @Autowired
    private ReviewValidator reviewValidator;

    @Autowired
    private CredentialsService credentialsService;

    @PostMapping("/user/review/{movieId}")
    public String newReview(@Valid @ModelAttribute Review review, BindingResult bindingResult, 
    		 @PathVariable("movieId") Long movieId, Model model) {
        this.reviewValidator.validate(review, bindingResult);
        Review newReview = this.reviewService.newReview(review, movieId);
        if (!bindingResult.hasErrors()) {
        	Movie movie = this.movieService.addReviewToMovie(movieId, newReview.getId());
            model.addAttribute(newReview);
        	model.addAttribute(movie);
            return "user/reviewSuccessful.html";
        } else {
            model.addAttribute("movie", this.movieService.getMovieById(movieId));
            return "user/formNewReview.html";
        }
    }

    @GetMapping("/admin/deleteReview/{reviewId}/{movieId}")
    public String deleteReview(@PathVariable("reviewId")Long reviewId, @PathVariable("movieId") Long movieId,
                               Model model) {
        this.reviewService.deleteReview(reviewId);
        Movie movie = this.movieService.getMovieById(movieId);
        model.addAttribute("movie", movie );
        model.addAttribute("reviews", movie.getReviews());
        return "admin/manageReviews.html";
    }

    @GetMapping("/reviews/reviewsOrdered/{id}")
    public String orderReviews(@PathVariable("id") Long id, Model model) {
        List<Review> reviews = this.reviewService.getReviewsByRateAsc();
        Movie movie = this.movieService.getMovieById(id);
        model.addAttribute("movie", movie);
        model.addAttribute("reviews", reviews);
        return "reviews.html";
    }
}
