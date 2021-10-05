package it.majorbit.util;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

@Component
public class Auth {

	private static String MS_AUTH;

	public static final String KEY = "ProvolaMambarella";

	/** ENCRYPTION KEY **/
	private static final String KEY_ALGORITHM = "AES";
	private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
	private static final Integer LENGTH = 128;

	@Value("${ip.msauth}")
	public void setAuth(String msAuth) {
		this.MS_AUTH=msAuth;
	}
	
	public static String crypt(String strClearText) throws Exception{
		String strData="";

		try {
			SecretKeySpec skeyspec=new SecretKeySpec(KEY.getBytes("ASCII"),"Blowfish");
			Cipher cipher=Cipher.getInstance("Blowfish");
			cipher.init(Cipher.ENCRYPT_MODE, skeyspec);
			byte[] encrypted=cipher.doFinal(strClearText.getBytes());
			strData= Base64.encodeBase64String(encrypted);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
		return strData;
	}



	public static String decrypt(String strEncrypted) throws Exception{
		String strData="";

		try {
			SecretKeySpec skeyspec=new SecretKeySpec(KEY.getBytes("ASCII"),"Blowfish");
			Cipher cipher=Cipher.getInstance("Blowfish");
			cipher.init(Cipher.DECRYPT_MODE, skeyspec);
			byte [] strToDecrypt= Base64.decodeBase64(strEncrypted);
			byte[] decrypted=cipher.doFinal(strToDecrypt);
			strData=new String(decrypted);

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
		return strData;
	}

	public static String cryptByEncryptionKey(Object toEncrypt, String encryptionKey) {
		String strData="";
		String toEncryptJson = new Gson().toJson(toEncrypt);

		try {
			SecretKeySpec skeyspec=new SecretKeySpec(encryptionKey.getBytes("ASCII"),"Blowfish");
			Cipher cipher=Cipher.getInstance("Blowfish");
			cipher.init(Cipher.ENCRYPT_MODE, skeyspec);
			byte[] encrypted=cipher.doFinal(toEncryptJson.getBytes());
			strData= Base64.encodeBase64String(encrypted);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strData;
	}

	public static String decryptByEncryptionKey(String strEncrypted, String encryptionKey) {
		String strData="";

		try {
			SecretKeySpec skeyspec=new SecretKeySpec(encryptionKey.getBytes("ASCII"),"Blowfish");
			Cipher cipher=Cipher.getInstance("Blowfish");
			cipher.init(Cipher.DECRYPT_MODE, skeyspec);
			byte [] strToDecrypt= Base64.decodeBase64(strEncrypted);
			byte[] decrypted=cipher.doFinal(strToDecrypt);
			strData=new String(decrypted);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return strData;
	}

	private static SecretKeySpec getSecretKey(final String password) {
		KeyGenerator kg = null;
		try {
			kg = KeyGenerator.getInstance(KEY_ALGORITHM);
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			random.setSeed(password.getBytes());
			kg.init(LENGTH, random);
			SecretKey secretKey = kg.generateKey();
			return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}


	public static boolean isAuthorized(Map<String, String> header) {

		// header contiene username e token di chi ha effettuato il login e vuole
		// effettuare delle operazioni. Username e token sono
		// contenuti nella stessa Stringa (del tipo "username token") che è il valore
		// della chiave 'authorization' della mappa header. Lo username sarà mandato
		// all'applicazione auth, al metodo getTokenByUsername che mi restituisce il
		// token associato all'username. La response, che
		// contiene questo token, deve essere confrontata con il token presente
		// nell'authorization. Se sono uguali, allora chi è
		// loggato è autorizzato ad effettuare le operazioni richieste

		// header è l'intestazione della richiesta. Spring la mette in automatico grazie
		// all'annotazione

		// Authorization rientra nel protocollo Kerberos

		String authorization = header.get("authorization");
		if (authorization!=null && !authorization.isEmpty()){
			if (authorization.trim().equals(authorization)) {  //Controllo che non vi siano degli spazi agli estremi della mia Stringa


				if (authorization.contains(" ")) {

					String tokenAndEncryptionKey = authorization.split(" ")[1];
					String email = authorization.split(" ")[0];
					String nomeAuth="social_mb";

					String token = tokenAndEncryptionKey.split("&:")[0];
					try {
						String decryptedEmail= Auth.decrypt(email);

						Map<String, Object> request = new HashMap<>();
						request.put("email", decryptedEmail);
						request.put("nomeAuth",nomeAuth);

						String response = Request.post("http://" + MS_AUTH + ":8084/readIndexInRegister", request, null);

						if (!response.equals(token)) {
							return false;
						}
						// response contiene il token risposta della mia richiesta relativo allo
						// username inserito
					} catch (Exception e1) {
						return false;
					}

					return true;
				}


			}

			return false;
		}

		return false;

	}

	// restituisce l encryptionKey
	public static String getEncryptionKey(Map<String, String> header) {
		return header.get("authorization").split("&:")[1];
	}
}

