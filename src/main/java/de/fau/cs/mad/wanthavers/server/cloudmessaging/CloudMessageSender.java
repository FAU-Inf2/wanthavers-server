package de.fau.cs.mad.wanthavers.server.cloudmessaging;


import com.amazonaws.util.IOUtils;
import com.amazonaws.util.StringUtils;
import de.fau.cs.mad.wanthavers.common.CloudMessageSubject;
import de.fau.cs.mad.wanthavers.common.CloudMessageToken;
import de.fau.cs.mad.wanthavers.server.SingletonManager;
import de.fau.cs.mad.wanthavers.server.dao.CloudMessageTokenDAO;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CloudMessageSender {
    public static final String FIREBASE_URL = "https://fcm.googleapis.com/fcm/send";
    public static final String API_KEY = "AIzaSyDozjC6AMa0O9btFvHGfHwFm7yo-_qrKRQ";

    /**
     * Sends message to all registered tokens of a user
     * @param message CloudMessage object to send
     */
    public static void sendMessage(CloudMessage message) {
        try {
            CloudMessageTokenDAO tokenDAO = (CloudMessageTokenDAO) SingletonManager.get(CloudMessageTokenDAO.class);
            List<CloudMessageToken> tokens = tokenDAO.findAll(message.getUserId());

            // For documentation of sendData see CloudMessageSubject
            JSONObject sendData = new JSONObject();
            sendData.put("data", message.getData());
            if(tokens.size() == 1) {
                sendData.put("to", tokens.get(0).getToken());
            } else {
                for (int i = 0; i < tokens.size(); i++)
                    sendData.accumulate("registration_ids", tokens.get(i).getToken());
            }

            URL url = new URL(FIREBASE_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", "key=" + API_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            System.out.println("Sending CloudMessage: "+ sendData.toString());
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(sendData.toString().getBytes());

            InputStream inputStream = conn.getInputStream();
            String resp = IOUtils.toString(inputStream);
            System.out.println("Send CloudMessage to User "+message.getUserId());
            System.out.println("Answer: " + resp);

            inputStream.close();
            outputStream.close();
            conn.disconnect();


        } catch (IOException e) {
            System.out.println("Unable to send CloudMessage to User "+message.getUserId());

            e.printStackTrace();
        }
    }
}
