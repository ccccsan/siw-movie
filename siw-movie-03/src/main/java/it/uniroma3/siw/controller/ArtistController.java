package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.service.ArtistService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

@Controller
public class ArtistController {

	@Autowired
	private ArtistService artistService;

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
	public String newArtist(@ModelAttribute("artist") Artist artist, BindingResult bindingResult, Model model,
							@RequestParam("image") MultipartFile multipartFile) throws IOException {
		if (!this.artistService.existsByNameAndSurname(artist.getName(), artist.getSurname())) {
			byte[] photoBytes = multipartFile.getBytes();
			artist.setImage(photoBytes);
			this.artistService.saveArtist(artist);
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
		byte[] photo = artist.getImage();
		if(photo != null) {
			String image = java.util.Base64.getEncoder().encodeToString(photo);
			model.addAttribute("image", image);
		}
		model.addAttribute("artist", artist);
		return "artist.html";
	}

	@GetMapping("/artist")
	public String getArtists(Model model) {
		Iterable<Artist> artists = this.artistService.getAllArtistsByAsc();
		model.addAttribute("artists", artists);
		return "artists.html";
	}
	
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
	
	@GetMapping("/admin/formUpdateArtist/{id}")
	public String formUpdateArtist(@PathVariable("id") Long id, Model model) {
		Artist artist = this.artistService.getActorById(id);
		model.addAttribute("artist", artist);
		return "admin/formUpdateArtist.html";
	}

	@PostMapping("/admin/setDateOfDeath")
	public String setDateOfDeath(@RequestParam("dateOfDeath") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfDeath,
								 @RequestParam("artist") Long artistId, Model model) {
		Artist artist = this.artistService.getActorById(artistId);
		this.artistService.setDateOfDeath(artist, dateOfDeath);
		model.addAttribute("artist", artist);
		return "admin/formUpdateArtist.html";
	}
}