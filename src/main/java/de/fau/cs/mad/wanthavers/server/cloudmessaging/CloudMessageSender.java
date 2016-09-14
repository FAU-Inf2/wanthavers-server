package de.fau.cs.mad.wanthavers.server.cloudmessaging;


import com.amazonaws.util.IOUtils;
import com.relayrides.pushy.apns.ApnsClient;
import com.relayrides.pushy.apns.util.ApnsPayloadBuilder;
import com.relayrides.pushy.apns.util.SimpleApnsPushNotification;
import de.fau.cs.mad.wanthavers.common.CloudMessageToken;
import de.fau.cs.mad.wanthavers.server.SingletonManager;
import de.fau.cs.mad.wanthavers.server.dao.CloudMessageTokenDAO;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class CloudMessageSender {
    public static final String FIREBASE_URL = "https://fcm.googleapis.com/fcm/send";
    public static final String API_KEY = "AIzaSyDozjC6AMa0O9btFvHGfHwFm7yo-_qrKRQ";
    public static final String IOS_PACKAGE_NAME = "de.fau.cs.mad.wanthaver.ios";

    /**
     * Sends message to all registered tokens of a user
     * @param message CloudMessage object to send
     */
    public static void sendMessage(CloudMessage message) {
        try {
            CloudMessageTokenDAO tokenDAO = (CloudMessageTokenDAO) SingletonManager.get(CloudMessageTokenDAO.class);
            List<CloudMessageToken> tokens = tokenDAO.findAll(message.getUserId());

            if(tokens.isEmpty())
                return;

            for(CloudMessageToken token : tokens) {
                if(token.getTokenType().equals(CloudMessageToken.ANDROID_TOKEN) || token.getTokenType() == null) {
                    // For documentation of sendData see CloudMessageSubject
                    JSONObject sendData = new JSONObject();
                    sendData.put("data", message.getData());
                    sendData.put("to", token.getToken());

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
                } else if(token.getTokenType().equals(CloudMessageToken.IOS_TOKEN)) {
                    SimpleApnsPushNotification pushNotification;

                    ApnsPayloadBuilder payloadBuilder = new ApnsPayloadBuilder()
                            .setBadgeNumber(1)
                            .setAlertTitle(message.getTitle())
                            .setAlertBody(message.getTitle() + ": " +message.getMessage())
                            .setSoundFileName("default");

                    for(String key : message.getDataWithoutHeader().keySet()) {
                        payloadBuilder.addCustomProperty(key, message.getData().get(key));
                    }

                    String payload = payloadBuilder.buildWithDefaultMaximumLength();
                    String tokenKey = token.getToken();

                    pushNotification = new SimpleApnsPushNotification(tokenKey, IOS_PACKAGE_NAME, payload);

                    System.out.println("Sending Push message: "+payload+" to user "+message.getUserId()+" with token "+token.getToken());

                    final ApnsClient apnsClient = (ApnsClient) SingletonManager.get(ApnsClient.class);
                    if(apnsClient == null) {
                        System.out.println("Sending push to Apple servers not possible, as there is no connection!");
                        continue;
                    }
                    apnsClient.sendNotification(pushNotification);
                }
            }


        } catch (IOException e) {
            System.out.println("Unable to send CloudMessage to User "+message.getUserId());

            e.printStackTrace();
        }
    }
}
