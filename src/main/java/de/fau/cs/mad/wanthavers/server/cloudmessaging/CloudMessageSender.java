package de.fau.cs.mad.wanthavers.server.cloudmessaging;


import com.amazonaws.util.IOUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CloudMessageSender {
    public static final String FIREBASE_URL = "https://fcm.googleapis.com/fcm/send";
    public static final String API_KEY = "AIzaSyBuv6KnEMboI37f0oEnkFQo4uElzNRVAT0";

    public static void sendMessage(CloudMessage message) {
        try {
            JSONObject sendData = new JSONObject();
            sendData.put("to", message.getTo());
            sendData.put("notification", message.getNotification());
            sendData.put("data", message.getData());

            URL url = new URL(FIREBASE_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", "key=" + API_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(sendData.toString().getBytes());

            InputStream inputStream = conn.getInputStream();
            String resp = IOUtils.toString(inputStream);
            System.out.println("Send CloudMessage to "+message.getTo());
            System.out.println("Answer: "+resp);

            inputStream.close();
            outputStream.close();
            conn.disconnect();

        } catch (IOException e) {
            System.out.println("Unable to send CloudMessage to "+message.getTo());
            e.printStackTrace();
        }
    }
}
