package de.fau.cs.mad.wanthavers.server.cloudmessaging;


import com.amazonaws.util.IOUtils;
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
    public static final String API_KEY = "AIzaSyBuv6KnEMboI37f0oEnkFQo4uElzNRVAT0";

    /**
     * Sends message to all registered tokens of a user
     * @param message CloudMessage object to send
     */
    public static void sendMessage(CloudMessage message) {
        try {
            CloudMessageTokenDAO tokenDAO = (CloudMessageTokenDAO) SingletonManager.get(CloudMessageTokenDAO.class);
            List<CloudMessageToken> tokens = tokenDAO.findAll(message.getUserId());
            List<JSONObject> sendDatas = new ArrayList<>();

            for (CloudMessageToken token : tokens) {
                JSONObject sendData = new JSONObject();
                sendData.put("to", token.getToken());
                sendData.put("notification", message.getNotification());
                sendData.put("data", message.getData());
                sendDatas.add(sendData);
            }

            URL url = new URL(FIREBASE_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", "key=" + API_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            OutputStream outputStream = conn.getOutputStream();
            InputStream inputStream = conn.getInputStream();

            for (JSONObject data : sendDatas) {
                outputStream.write(data.toString().getBytes());

                String resp = IOUtils.toString(inputStream);
                System.out.println("Send CloudMessage to User "+message.getUserId());
                System.out.println("with token "+data.getString("to"));
                System.out.println("Answer: " + resp);
            }

            inputStream.close();
            outputStream.close();
            conn.disconnect();

        } catch (IOException e) {
            System.out.println("Unable to send CloudMessage to User "+message.getUserId());
            e.printStackTrace();
        }
    }
}
