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



    @GetMapping("/admin/reviewsInMovie/{movieId}")
    public String reviewsInMovie(@PathVariable("id")Long id, Model model) {
        Movie movie = movieService.getMovieById(id);
        List<Review> reviews = this.reviewService.findAllByMovieId(movie.getId());
        if (!reviews.isEmpty()) {
            model.addAttribute("reviews", reviews);
            model.addAttribute("movie", movie);
            return "admin/manageReviews.html";
        } else {
            return "admin/formUpdateMovie.html";
        }
    }

    @GetMapping(value="/admin/deleteReview/{reviewId}/{movieId}")
    public String deleteReview(@PathVariable("reviewId")Long reviewId, @PathVariable("movieId") Long movieId,
                               Model model) {
        this.reviewService.deleteReview(reviewId);
        Movie movie = this.movieService.getMovieById(movieId);
        model.addAttribute("movie", movie );
        model.addAttribute("reviews", movie.getReviews());
        return "admin/reviewToDelete.html";
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
