package it.majorbit.controller;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import it.majorbit.util.Auth;
import it.majorbit.model.Quote;
import it.majorbit.service.QuoteService;
import it.majorbit.service.ErroreService;

@Controller
public class QuotesController {

	@Autowired
	public QuoteService quoteService;

	@Autowired
	public ErroreService erroreService;


	@PostMapping("register_quote")
	public @ResponseBody ResponseEntity<Object> registerQuote(@RequestBody Map<String,String> params,@RequestHeader Map<String, String> header){

		String authorization = Auth.isAuthorized(header);
		
			if (authorization != null) {	

			String encryptedString = (String)params.get("r");

			String encryptionKey = Auth.getEncryptionKey(header);

			String decryptedString = Auth.decryptByEncryptionKey(encryptedString,encryptionKey);

			Map<String,String> map = new Gson().fromJson(decryptedString,Map.class);

			Quote newQuote = new Quote();

			newQuote.setQuote(map.get("quote"));

			quoteService.registerQuote(newQuote);
			String messageToBeCrypted = "Quote creato con successo";
			String cryptedMessage = Auth.cryptByEncryptionKey(messageToBeCrypted,encryptionKey);
			return ResponseEntity.status(HttpStatus.OK).header("Authorization", authorization)
					.header("Access-Control-Expose-Headers", "authorization").body(cryptedMessage);

		}

		else {
			Map<String,Object> error = new HashMap<String,Object>(); //mappa di errore generata nel caso in cui l'utente non abbia effettuato il login
			error.put("hasError", true);
			error.put("message", erroreService.readErrore("QUOTES_UNAUTHORIZED_ERROR").getTextIta());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
		}
	}



	@GetMapping("read_quote")
	public @ResponseBody ResponseEntity<Object> readQuote(@RequestParam String id, @RequestHeader Map<String, String> header){

		String authorization = Auth.isAuthorized(header);
		
			if (authorization != null)  {

			String encryptionKey = Auth.getEncryptionKey(header);

			id = id.replace(" ","+");

			String decryptedId = Auth.decryptByEncryptionKey(id,encryptionKey);

			Integer integerId = Integer.parseInt(decryptedId);

			Quote quoteToBeRetrieved = quoteService.readQuote(integerId);

			if(quoteToBeRetrieved!=null) { 

				String CryptedJsonString = Auth.cryptByEncryptionKey(quoteToBeRetrieved,encryptionKey);

				return ResponseEntity.status(HttpStatus.OK).header("Authorization", authorization)
						.header("Access-Control-Expose-Headers", "authorization").body(CryptedJsonString);
			}

			else {
				Map<String,Object> error = new HashMap<String,Object>(); //mappa di errore generata nel caso in cui lo sfondo cercato non esista
				error.put("hasError", true);
				error.put("message", erroreService.readErrore("QUOTES_NOT_EXISTING_ERROR").getTextIta());
				return ResponseEntity.status(HttpStatus.NOT_FOUND).header("Authorization", authorization)
						.header("Access-Control-Expose-Headers", "authorization").body(error);

			}

		} else {
			Map<String,Object> error = new HashMap<String,Object>(); //mappa di errore generata nel caso in cui l'utente non abbia effettuato il logIn
			error.put("hasError", true);
			error.put("message", erroreService.readErrore("QUOTES_UNAUTHORIZED_ERROR").getTextIta()); 
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
		}
	}




	@DeleteMapping("delete_quote")
	public @ResponseBody ResponseEntity<Object> deleteQuote(@RequestParam String id, @RequestHeader Map<String, String> header){

		String authorization = Auth.isAuthorized(header);
		
			if (authorization != null)  {  //controlla se l'utente è loggato

			String encryptionKey = Auth.getEncryptionKey(header);

			id = id.replace(" ","+");

			String decryptedId = Auth.decryptByEncryptionKey(id,encryptionKey);

			Integer integerId = Integer.parseInt(decryptedId);

			Quote quoteToBeDeleted = quoteService.readQuote(integerId);

			if(quoteToBeDeleted!=null) {

				quoteService. deleteQuote(quoteToBeDeleted);
				String messageToBeCrypted = "Quote eliminato";
				String cryptedMessage = Auth.cryptByEncryptionKey(messageToBeCrypted,encryptionKey); 
				return ResponseEntity.status(HttpStatus.OK).header("Authorization", authorization)
						.header("Access-Control-Expose-Headers", "authorization").body(cryptedMessage);
			}	

			else {

				Map<String,Object> error = new HashMap<String,Object>(); //mappa di errore generata nel caso in cui il gruppo non esiste
				error.put("hasError", true);
				error.put("message", erroreService.readErrore("QUOTES_NOT_EXISTING_ERROR").getTextIta());
				return ResponseEntity.status(HttpStatus.NOT_FOUND).header("Authorization", authorization)
						.header("Access-Control-Expose-Headers", "authorization").body(error);
			}

		} else {

			Map<String,Object> error = new HashMap<String,Object>(); //mappa di errore generata nel caso in cui l'utente non è loggato
			error.put("hasError", true);
			error.put("message", erroreService.readErrore("QUOTES_UNAUTHORIZED_ERROR").getTextIta());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
		}

	}



	@PutMapping("update_quote")
	public @ResponseBody ResponseEntity<Object> updateQuote(@RequestBody Map<String,String> params, @RequestHeader Map<String, String> header) { 
		
		String authorization = Auth.isAuthorized(header);
		
			if (authorization != null)  { //verifica che l'utente sia loggato

			String encryptedString = (String)params.get("r");

			String encryptionKey = Auth.getEncryptionKey(header);

			String decryptedString = Auth.decryptByEncryptionKey(encryptedString,encryptionKey);

			Map<String,String> map = new Gson().fromJson(decryptedString,Map.class);

			String decryptedId = (String)map.get("id");

			Integer integerId = Integer.parseInt(decryptedId);

			Quote quoteToBeUpdated = quoteService.readQuote(integerId);
			
			if(quoteToBeUpdated!=null) {

				quoteToBeUpdated.setQuote((String)map.get("new quote"));
				quoteService.registerQuote(quoteToBeUpdated);
				String messageToBeCrypted = "Quote aggiornato";
				String cryptedMessage = Auth.cryptByEncryptionKey(messageToBeCrypted,encryptionKey); 
				return ResponseEntity.status(HttpStatus.OK).header("Authorization", authorization)
						.header("Access-Control-Expose-Headers", "authorization").body(cryptedMessage);	
			}	

			else {
				Map<String,Object> error = new HashMap<String,Object>(); //mappa di errore generata nel caso in cui il gruppo non esista
				error.put("hasError", true);
				error.put("message", erroreService.readErrore("QUOTES_NOT_EXISTING_ERROR").getTextIta());
				return ResponseEntity.status(HttpStatus.NOT_FOUND).header("Authorization", authorization)
						.header("Access-Control-Expose-Headers", "authorization").body(error);			
			}
		}

		else {
			Map<String,Object> error = new HashMap<String,Object>(); //mappa di errore generata nel caso in cui l'utente non abbia effettuato il login
			error.put("hasError", true);
			error.put("message", erroreService.readErrore("QUOTES_UNAUTHORIZED_ERROR").getTextIta());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
		}
	}
}	