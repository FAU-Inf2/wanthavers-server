package de.fau.cs.mad.wanthavers.server.cloudmessaging;


import org.json.JSONObject;


public class CloudMessage {
    private String to;
    private JSONObject data;
    private JSONObject notification;

    /**
     *
     * @param to: API token, which identifies reciever
     * @param subject: data field used for storing a String to identify furhter procedure of the app for this notification
     * @param title: title to display in notification center
     * @param message: message to display in notification center
     */
    public CloudMessage(String to, String subject, String title, String message) {
        this.to = to;

        notification = new JSONObject();
        notification.put("body", message);
        notification.put("title", subject);
        notification.put("icon", "");

        data = new JSONObject();
        data.put("subject", subject);
    }
    
    public void addKeyValue(String key, String value) {
        data.put(key, value);
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    public void setNotification(JSONObject notification) {
        this.notification = notification;
    }

    public JSONObject getNotification() {
        return notification;
    }

}
