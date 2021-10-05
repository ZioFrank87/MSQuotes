package it.majorbit.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.majorbit.model.Errore;

@Repository
public interface ErroreRepository extends CrudRepository<Errore,String> {

	

}
