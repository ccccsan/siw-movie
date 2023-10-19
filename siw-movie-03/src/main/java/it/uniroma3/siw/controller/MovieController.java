package it.uniroma3.siw.controller;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

import javax.validation.Valid;

import it.uniroma3.siw.service.ReviewService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.controller.validator.MovieValidator;
import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.service.ArtistService;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.MovieService;

@Controller
public class MovieController {
	@Autowired 
	private MovieService movieService;
	
	@Autowired 
	private ArtistService artistService;

	@Autowired 
	private MovieValidator movieValidator;

	@Autowired 
	private CredentialsService credentialsService;

	@Autowired
	private ReviewService reviewService;

	@GetMapping(value="/admin/formNewMovie")
	public String formNewMovie(Model model) {
		model.addAttribute("movie", new Movie());
		return "admin/formNewMovie.html";
	}

	@GetMapping(value="/admin/formUpdateMovie/{id}")
	public String formUpdateMovie(@PathVariable("id") Long id, Model model) {
		model.addAttribute("movie", movieService.getMovieById(id));
		return "admin/formUpdateMovie.html";
	}

	@GetMapping(value="/admin/indexMovie")
	public String indexMovie() {
		return "admin/indexMovie.html";
	}
	
	@GetMapping(value="/admin/adminMovie/{id}")
	public String adminMovie(@PathVariable("id") Long id, Model model) {
		Movie movie = this.movieService.getMovieById(id);
		model.addAttribute("movie", movie);
		return "admin/adminMovie.html";
	}
	
	@GetMapping(value="/admin/manageMovies")
	public String manageMovies(Model model) {
		model.addAttribute("movies", this.movieService.getMovies());
		return "admin/manageMovies.html";
	}
	
	@GetMapping(value="/admin/setDirectorToMovie/{directorId}/{movieId}")
	public String setDirectorToMovie(@PathVariable("directorId") Long directorId, @PathVariable("movieId") Long movieId, Model model) {
		
		Movie movie = this.movieService.setDirectorToMovie(directorId, movieId);
		
		model.addAttribute("movie", movie);
		return "admin/formUpdateMovie.html";
	}
	
	
	@GetMapping(value="/admin/addDirector/{id}")
	public String addDirector(@PathVariable("id") Long id, Model model) {
		model.addAttribute("artists", artistService.getAllArtists());
		model.addAttribute("movie", movieService.getMovieById(id));
		return "admin/directorsToAdd.html";
	}

	@PostMapping("/admin/movie")
	public String newMovie(@Valid @ModelAttribute("movie") Movie movie, BindingResult bindingResult, Model model,
			@RequestParam("image") MultipartFile multipartFile) throws IOException {
		this.movieValidator.validate(movie, bindingResult);
		if (!bindingResult.hasErrors()) {
			this.movieService.createNewMovie(movie, multipartFile);
			model.addAttribute("movie", movie);
			return "movie.html";
		} else {
			return "admin/formNewMovie.html"; 
		}
	}

	@GetMapping("/movie/{id}")
	public String getMovie(@PathVariable("id") Long id, Model model) {
		Movie movie = this.movieService.getMovieById(id);
		model.addAttribute("movie", movie);
		return "movie.html";
	}

	@GetMapping("/movie")
	public String getMovies(Model model) {
		model.addAttribute("movies", this.movieService.getMovies());
		return "movies.html";
	}

	@PostMapping("/admin/addScenes/{id}")
	public String addScenes(@PathVariable("id") Long id, Model model,
							@RequestParam("image") MultipartFile multipartFile) throws IOException {
		Movie movie = this.movieService.getMovieById(id);
		String movieImg = Base64.getEncoder().encodeToString(multipartFile.getBytes());
		movie.getScenes().add(movieImg);
		this.movieService.saveMovie(movie);
		model.addAttribute("movie", movie);
		return "admin/formUpdateMovie.html";
	}
	
	@GetMapping("/formSearchMovies")
	public String formSearchMovies() {
		return "formSearchMovies.html";
	}

	@PostMapping("/searchMovies")
	public String searchMovies(Model model, @RequestParam int year) {
		model.addAttribute("movies", this.movieService.getMoviesByYear(year));
		return "foundMovies.html";
	}
	
	@GetMapping("/admin/updateActors/{id}")
	public String updateActors(@PathVariable("id") Long id, Model model) {

		List<Artist> actorsToAdd = this.movieService.findActorsNotInMovie(id);
		model.addAttribute("actorsToAdd", actorsToAdd);
		model.addAttribute("movie", this.movieService.getMovieById(id));

		return "admin/actorsToAdd.html";
	}

	@GetMapping(value="/admin/addActorToMovie/{actorId}/{movieId}")
	public String addActorToMovie(@PathVariable("actorId") Long actorId, @PathVariable("movieId") Long movieId, Model model) {
		Movie movie = this.movieService.addActorToMovie(movieId, actorId);
		List<Artist> actorsToAdd = this.movieService.findActorsNotInMovie(movieId);

		model.addAttribute("movie", movie);
		model.addAttribute("actorsToAdd", actorsToAdd);

		return "admin/actorsToAdd.html";
	}
	
	@GetMapping(value="/admin/removeActorFromMovie/{actorId}/{movieId}")
	public String removeActorFromMovie(@PathVariable("actorId") Long actorId, @PathVariable("movieId") Long movieId, Model model) {
		Movie movie = this.movieService.removeActorFromMovie(movieId, actorId);
		List<Artist> actorsToAdd = this.movieService.findActorsNotInMovie(movieId);
		
		model.addAttribute("movie", movie);
		model.addAttribute("actorsToAdd", actorsToAdd);

		return "admin/actorsToAdd.html";
	}

	@GetMapping(value="/user/moviesUser")
	public String getUserMovies(Model model) {
		UserDetails user = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(user.getUsername());
		model.addAttribute("user", credentials.getUser());
		model.addAttribute("movies", this.movieService.getMovies());
		return "user/moviesUser.html";
	}

	@GetMapping(value="/user/movieUser/{id}")
	public String getUserMovie(@PathVariable("id") Long id, Model model) {
		Movie movie = this.movieService.getMovieById(id);
		model.addAttribute("movie", movie);
		return "user/movieUser.html";
	}

	@GetMapping("/user/formNewReview/{id}")
	public String addReview(@PathVariable("id") Long id, Model model) {
		model.addAttribute("movie", this.movieService.getMovieById(id));
		model.addAttribute("review", new Review());
		return "user/formNewReview.html";
	}

	@PostMapping("/user/review/{movieId}")
	public String newReview(@Valid @ModelAttribute Review review, BindingResult bindingResult,
							@PathVariable("movieId") Long movieId, Model model) {
		Review newReview = this.reviewService.newReview(review, movieId);
		if (!bindingResult.hasErrors()) {
			Movie movie = this.movieService.addReviewToMovie(movieId, newReview.getId());
//			model.addAttribute(newReview);
			model.addAttribute(movie);
			return "user/movieUser.html";
		} else {
			model.addAttribute("movie", this.movieService.getMovieById(movieId));
			return "user/formNewReview.html";
		}
	}

	@GetMapping(value = "/admin/deleteReview/{reviewId}/{movieId}")
	public String deleteReview(@PathVariable("reviewId") Long reviewId, @PathVariable("movieId") Long movieId,
							   Model model) {
		this.reviewService.deleteReview(reviewId);
		Movie movie = this.movieService.getMovieById(movieId);
		model.addAttribute("movie", movie);
		model.addAttribute("reviews", movie.getReviews());
		return "admin/formUpdateMovie.html";
	}

	@GetMapping(value="/admin/deleteMovie/{movieId}")
	public String deleteMovie(@PathVariable("movieId")Long movieId, Model model) {
		this.movieService.deleteMovie(movieId);
		model.addAttribute("movies", this.movieService.getMovies());
		return "admin/manageMovies.html";
	}

	@PostMapping(value="/admin/updateMovie/{movieId}")
	public String updateMovie(@ModelAttribute("movie") Movie newMovie, @PathVariable("movieId")Long movieId, Model model) {
		Movie oldMovie = this.movieService.getMovieById(movieId);
		Movie movie = this.movieService.updateMovie(oldMovie, newMovie);
		this.movieService.saveMovie(movie);
		model.addAttribute("movie", movie);
		return "admin/formUpdateMovie.html";
	}
}
