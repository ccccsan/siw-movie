package it.uniroma3.siw.service;

import java.util.Set;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.repository.ArtistRepository;

@Service
public class ArtistService {

    @Autowired
    private ArtistRepository artistRepository;

    @Transactional
    public Iterable<Artist> getAllArtists() {
        return this.artistRepository.findAll();
    }

    @Transactional
    public Artist getActorById(Long actorId) {
        return artistRepository.findById(actorId).get();
    }

    @Transactional
    public Iterable<Artist> findActorsNotInMovie(Long movieId) {
        return this.artistRepository.findActorsNotInMovie(movieId);
    }

    @Transactional
    public boolean existsByNameAndSurname(String name, String surname) {
        return artistRepository.existsByNameAndSurname(name, surname);
    }

    @Transactional
    public void saveArtist(Artist artist) {

        this.artistRepository.save(artist);
    }
    
    @Transactional
    public void deleteArtist(Long artistId) {
    	Artist artist = this.getActorById(artistId);
		Set<Movie> movies = artist.getActorOf();
		for(Movie movie : movies) {
			movie.getActors().remove(artist);
		}
		this.artistRepository.delete(artist);
    }


}
