package com.oliweb.S_SMS;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SendSms {

    private static final String api_mblox_token = "c0f8612fa5e94f03b6f0d1d0d6158091";
    private static final String api_mblox_link = "https://api.mblox.com/xms/v1/oliweb12/batches";
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public static String send(String from, String to, String body) {

        String message = null;

        try {
            URL url = new URL(api_mblox_link);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer ".concat(api_mblox_token));
            conn.setRequestProperty("Content-Type", "application/json");

            String input = "{\"from\": \"" + from + "\", \"to\": [\"" + to + "\"], \"body\": \"" + body + "\" }";

            OutputStream os = conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();

            BufferedReader br;

            if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
                // Quelque chose n'a pas march�
                br = new BufferedReader(new InputStreamReader(
                        (conn.getErrorStream())));
            } else {
                // Tout s'est bien d�roul�
                // R�cup�ration du message comme quoi tout � fonctionner
                br = new BufferedReader(new InputStreamReader(
                        (conn.getInputStream())));
            }

            String output;
            while ((output = br.readLine()) != null) {
                message = message + output;
            }
            conn.disconnect();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }

        return message;
    }
}
