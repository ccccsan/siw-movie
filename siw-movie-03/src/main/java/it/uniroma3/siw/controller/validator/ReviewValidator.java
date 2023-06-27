package it.uniroma3.siw.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.repository.ReviewRepository;


@Component
public class ReviewValidator implements Validator {
	@Autowired
	private ReviewRepository reviewRepository;
	
	@Override
	public boolean supports(Class<?> aClass) {
		return Review.class.equals(aClass);
	}

	@Override
	public void validate(Object o, Errors errors) {
		Review review = (Review)o;
		if (review.getReviewer()!=null &&
			review.getMovie()!= null &&
			reviewRepository.existsByReviewerAndMovie(review.getReviewer(), review.getMovie())) {
			errors.reject("review.duplicate");
		}
			
	}
}
