package com.oliweb.S_SMS;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SendSms {
	
	public static final String api_mblox_token = "c0f8612fa5e94f03b6f0d1d0d6158091";
	public static final String api_mblox_link = "https://api.mblox.com/xms/v1/oliweb12/batches";
	
	public static String send(String from, String to, String body, boolean retour) {
		
		String message = null;
		
		try {

			URL url = new URL(api_mblox_link);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Authorization", "Bearer ".concat(api_mblox_token));
			conn.setRequestProperty("Content-Type", "application/json");

			String input = "{\"from\": \"" + from + "\", \"to\": [\""+to+"\"], \"body\": \""+body+"\" }";

			System.out.println(input);
			
			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();
			
			BufferedReader br;
			
			if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
				// Quelque chose n'a pas march�
				retour = false;
				br = new BufferedReader(new InputStreamReader(
						(conn.getErrorStream())));					
			}else{
				// Tout s'est bien d�roul�
				// R�cup�ration du message comme quoi tout � fonctionner
				retour = true;
				br = new BufferedReader(new InputStreamReader(
						(conn.getInputStream())));
			}

			String output;
			while ((output = br.readLine()) != null) {
				message = message + output;
			}
			System.out.println(message);		
			
			conn.disconnect();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return message;
	}
}
