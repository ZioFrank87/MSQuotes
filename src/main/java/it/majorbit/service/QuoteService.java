package it.majorbit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.majorbit.model.Quote;
import it.majorbit.repositories.QuotesRepository;

@Service
public class QuoteService {

	@Autowired
	private QuotesRepository quotesRepository;


	public Quote readQuote(Integer id) {
		return quotesRepository.getQuoteById (id).orElse(null);
	}
	

	public void registerQuote(Quote quote) {
		quotesRepository.save(quote);
	}


	public void deleteQuote(Quote quote) {
		quotesRepository.delete(quote);
	}

}
