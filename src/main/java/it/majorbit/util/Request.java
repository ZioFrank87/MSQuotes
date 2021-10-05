package it.majorbit.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class Request {

	public static String post(String endpoint, Map<String, Object> data, Map<String, String> headers)
			throws IOException {
		URL url = new URL(endpoint);

		String inputData = "{";
		for (String key : data.keySet()) {
			inputData += "\"" + key + "\":\"" + data.get(key) + "\",";
		}
		inputData = inputData.substring(0, inputData.length() - 1);
		inputData += "}";

		System.out.println(inputData);

		HttpURLConnection con = (HttpURLConnection) url.openConnection();

		con.setRequestMethod("POST");

		con.setRequestProperty("Content-Type", "application/json; utf-8");

		con.setRequestProperty("Accept", "application/json");

		if (headers != null) {
			for (String s : headers.keySet()) {
				con.setRequestProperty(s, headers.get(s));
			}
		}
		con.setDoOutput(true);

		try (OutputStream os = con.getOutputStream()) {
			byte[] input = inputData.getBytes("utf-8");
			os.write(input, 0, input.length);
		}

		try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
			StringBuilder response = new StringBuilder();
			String responseLine = null;
			while ((responseLine = br.readLine()) != null) {
				response.append(responseLine.trim());
			}

			if (response != null && response.length() > 0 && response.toString().charAt(0) == '&'
					&& response.toString().charAt(1) == '4') { // Errore restituito dal server
				return null;
			}

			return (response.toString());
		}
	}

}
