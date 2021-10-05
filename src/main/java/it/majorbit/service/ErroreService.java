package it.majorbit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.majorbit.model.Errore;
import it.majorbit.repositories.ErroreRepository;

@Service
public class ErroreService {

	
	@Autowired
	private ErroreRepository erroreRepository;


	public Errore readErrore(String code) {
		return erroreRepository.findById(code).orElse(null);
	}
}
