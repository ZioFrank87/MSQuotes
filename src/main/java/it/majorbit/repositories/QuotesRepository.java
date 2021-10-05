package it.majorbit.repositories;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import it.majorbit.model.Quote;

@Repository
public interface QuotesRepository extends CrudRepository<Quote,String> {
	

	@Query("SELECT q FROM Quote q WHERE q.id =?1")
	public Optional <Quote> getQuoteById (Integer id);

}