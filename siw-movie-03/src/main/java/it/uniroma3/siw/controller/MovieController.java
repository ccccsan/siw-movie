package it.uniroma3.siw.controller;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

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
		byte[] photo = movie.getPhoto();
		if(photo != null) {
			String image = java.util.Base64.getEncoder().encodeToString(photo);
			model.addAttribute("image", image);
		}
		model.addAttribute("movie", movie);
		return "admin/adminMovie.html";
	}
	
	@GetMapping(value="/admin/manageMovies")
	public String manageMovies(Model model) {
		model.addAttribute("movies", this.movieService.getAllMoviesByAsc());
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
		byte[] photo = movie.getPhoto();
		if(photo != null) {
			String image = java.util.Base64.getEncoder().encodeToString(photo);
			model.addAttribute("image", image);
		}
		model.addAttribute("movie", movie);
		return "movie.html";
	}

	@GetMapping("/movie")
	public String getMovies(Model model) {
		//model.addAttribute("movies", this.movieService.getAllMovies());
		model.addAttribute("movies", this.movieService.getAllMoviesByAsc());
		return "movies.html";
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
		model.addAttribute("movies", this.movieService.getAllMoviesByAsc());
		return "user/moviesUser.html";
	}

	@GetMapping(value="/user/movieUser/{id}")
	public String getUserMovie(@PathVariable("id") Long id, Model model) {
		Movie movie = this.movieService.getMovieById(id);
//		byte[] photo = movie.getImage();
//		if(photo != null) {
//			String image = java.util.Base64.getEncoder().encodeToString(photo);
//			model.addAttribute("image", image);
//		}
		model.addAttribute("movie", movie);
		return "user/movieUser.html";
	}

	@GetMapping("/user/addReview/{id}")
	public String addReview(@PathVariable("id") Long id, Model model) {
		model.addAttribute("movie", this.movieService.getMovieById(id));
		model.addAttribute("review", new Review());
		return "user/formNewReview.html";
	}

	@GetMapping("/movie/reviews/{reviewId}")
	public String getReviews(@PathVariable("id")Long id, Model model) {
		Movie movie = this.movieService.getMovieById(id);
		List<Review> reviews = movie.getReviews();
		model.addAttribute("reviews", reviews);
		model.addAttribute("movie", movie);
		return "reviews.html";
	}

	@GetMapping(value="/admin/deleteMovie/{movieId}")
	public String deleteMovie(@PathVariable("movieId")Long movieId, Model model) {
		this.movieService.deleteMovie(movieId);
		model.addAttribute("movies", this.movieService.getAllMoviesByAsc());
		return "admin/manageMovies.html";
	}

	@GetMapping(value="/admin/updateMovie/{movieId}")
	public String updateMovie(@ModelAttribute("movie") Movie newMovie, @PathVariable("movieId")Long movieId, Model model) {
		Movie oldMovie = this.movieService.getMovieById(movieId);
		Movie movie = this.movieService.updateMovie(oldMovie, newMovie);
		this.movieService.saveMovie(movie);
		model.addAttribute("movie", movie);
		return "admin/formUpdateMovie.html";
	}
}
