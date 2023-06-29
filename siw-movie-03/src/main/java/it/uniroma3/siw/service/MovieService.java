package it.uniroma3.siw.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.repository.ArtistRepository;
import it.uniroma3.siw.repository.MovieRepository;

import javax.validation.Valid;

@Service
public class MovieService {

	@Autowired
	private MovieRepository movieRepository;

	@Autowired
	private ArtistRepository artistRepository;

	@Autowired
	private ArtistService artistService;

	@Autowired
	private ReviewService reviewService;

    @Transactional
    public void saveMovie(@Valid Movie movie) {
        this.movieRepository.save(movie);
    }

//	@Transactional
//	public void createNewMovie(@Valid Movie movie, MultipartFile image) {
//		try {
//			Image movieImg = new Image(image.getBytes());
//			this.imageRepository.save(movieImg);
//			movie.setImage(movieImg);
//			this.movieRepository.save(movie);
//		} catch (Exception e) {
//			movie.setImage(null);
//		}
//
//	}

	public Movie getMovieById(Long id) {
		return this.movieRepository.findById(id).get();
	}

	public Iterable<Movie> getMovies() {
		return this.movieRepository.findAll();
	}

	public List<Movie> getAllMoviesByAsc() {
		return this.movieRepository.findByOrderByYearAsc();
	}

	public List<Movie> getMoviesByYear(int year) {
		return this.movieRepository.findByYear(year);
	}

	@Transactional
	public Movie setDirectorToMovie(Long directorId, Long movieId) {
		Artist director = this.artistRepository.findById(directorId).get();
		Movie movie = this.movieRepository.findById(movieId).get();
		movie.setDirector(director);
		this.movieRepository.save(movie);
		return movie;
	}

	@Transactional
	public Movie addActorToMovie(Long movieId, Long actorId) {
		Movie movie = this.movieRepository.findById(movieId).get();
		Artist actor = this.artistRepository.findById(actorId).get();
		Set<Artist> actors = movie.getActors();
		actors.add(actor);
		this.movieRepository.save(movie);
		return movie;
	}

	@Transactional
	public Movie removeActorFromMovie(Long movieId, Long actorId) {
		Movie movie = this.getMovieById(movieId);
		Artist actor = this.artistService.getActorById(actorId);
		Set<Artist> actors = movie.getActors();
		actors.remove(actor);
		this.movieRepository.save(movie);
		return movie;
	}

	public List<Artist> findActorsNotInMovie(Long movieId) {
		List<Artist> actorsToAdd = new ArrayList<>();

		for (Artist a : artistService.findActorsNotInMovie(movieId)) {
			actorsToAdd.add(a);
		}
		return actorsToAdd;
	}

	@Transactional
	public void deleteMovie(Long movieId) {
		Movie movie = this.getMovieById(movieId);
		Set<Artist> actors = movie.getActors();
		for (Artist actor : actors) {
			actor.getActorOf().remove(movie);
		}
		List<Review> reviews = movie.getReviews();
		for (Review review : reviews) {
			review.setMovie(null);
		}
		this.movieRepository.delete(movie);
	}

	@Transactional
	public Movie addReviewToMovie(Long movieId, Long reviewId) {
		Movie movie = this.getMovieById(movieId);
		Review review = this.reviewService.getReviewById(reviewId);
		List<Review> reviews = movie.getReviews();
		reviews.add(review);
		movie.setReviews(reviews);
		review.setMovie(movie);
		return this.movieRepository.save(movie);
	}

//	@Transactional
//	public void addImage(Movie movie, MultipartFile image) throws IOException {
//		if (this.imageValidator.isImage(image) || image.getSize() < ImageValidator.MAX_IMAGE_SIZE) {
//			Image movieImg = new Image(image.getBytes());
//			this.imageRepository.save(movieImg);
//			movie.getImages().add(movieImg);
//			this.movieRepository.save(movie);
//		}
//
//	}
//
//	@Transactional
//	public void removeImage(Long movieId, Long imageId) {
//		Image image = this.imageRepository.findById(imageId).get();
//		Movie movie = this.getMovieById(movieId);
//		movie.getImages().remove(image);
//		this.movieRepository.save(movie);
//	}
//
//	@Transactional
//	public void addLocandina(Movie movie, MultipartFile image) throws IOException {
//		if (this.imageValidator.isImage(image) || image.getSize() < ImageValidator.MAX_IMAGE_SIZE) {
//			Image movieImg = new Image(image.getBytes());
//			this.imageRepository.save(movieImg);
//			movie.setImage(movieImg);
//			this.movieRepository.save(movie);
//		}
//	}
}