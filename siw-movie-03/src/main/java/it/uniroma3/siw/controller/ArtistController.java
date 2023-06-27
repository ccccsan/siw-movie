package it.uniroma3.siw.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.controller.validator.ArtistValidator;
import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.service.ArtistService;

@Controller
public class ArtistController {

	@Autowired
	private ArtistService artistService;

	@Autowired
	private ArtistValidator artistValidator;

	@GetMapping(value="/admin/formNewArtist")
	public String formNewArtist(Model model) {
		model.addAttribute("artist", new Artist());
		return "admin/formNewArtist.html";
	}

	@GetMapping(value="/admin/indexArtist")
	public String indexArtist() {
		return "admin/indexArtist.html";
	}

	@PostMapping("/admin/artist")
	public String newArtist(@Valid @ModelAttribute("artist") Artist artist, BindingResult bindingResult, Model model) {
		this.artistValidator.validate(artist, bindingResult);
		if (!bindingResult.hasErrors()) {
			this.artistService.addArtist(artist);
			model.addAttribute("artist", artist);
			return "artist.html";
		} else {
			model.addAttribute("messaggioErrore", "Questo artista esiste già");
			return "admin/formNewArtist.html";
		}
	}

	@GetMapping("/artist/{id}")
	public String getArtist(@PathVariable("id") Long id, Model model) {
		Artist artist = this.artistService.getActorById(id);
		model.addAttribute("artist", artist);
		return "artist.html";
	}

	@GetMapping("/artist")
	public String getArtists(Model model) {
		Iterable<Artist> artists = this.artistService.getAllArtists();
		model.addAttribute("artists", artists);
		return "artists.html";
	}
	
//	@GetMapping("/admin/formUpdateArtist/{id}")
//	public String formUpdateArtist(@PathVariable("id") Long id, Model model) {
//		model.addAttribute("artist", this.artistService.getActorById(id));
//		return "admin/formUpdateArtist.html";
//	}
	
	@GetMapping("/admin/manageArtists")
	public String manageArtists(Model model) {
		model.addAttribute("artists", this.artistService.getAllArtists());
		return "admin/manageArtists.html";
	}
	
	@GetMapping("/admin/deleteArtist/{id}")
	public String deleteArtist(@PathVariable("id") Long id, Model model) {
		this.artistService.deleteArtist(id);
		model.addAttribute("artists", this.artistService.getAllArtists());
		return "admin/manageArtists.html";
	}
}