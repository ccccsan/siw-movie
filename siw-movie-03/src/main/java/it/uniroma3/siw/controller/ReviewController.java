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

    @GetMapping("/user/formNewReview/{id}")
    public String formNewReview(@PathVariable("id") Long id, Model model) {
        model.addAttribute("movie", this.movieService.getMovieById(id));
        model.addAttribute("review", new Review());
        return "user/formNewReview.html";
    }

    @PostMapping("/user/review/{movieId}")
    public String newReview(@ModelAttribute Review review, BindingResult bindingResult,
                                   @PathVariable("movieId") Long movieId, Model model) {
        if (!bindingResult.hasErrors()) {
            Review newReview = this.reviewService.newReview(review, movieId);
            Movie movie = this.movieService.addReviewToMovie(movieId, newReview.getId());
            model.addAttribute("movie", movie);
            model.addAttribute("review", newReview);
            return "user/movieUser.html";
        } else {
            model.addAttribute("movie", this.movieService.getMovieById(movieId));
            return "user/formNewReview.html";
        }
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
