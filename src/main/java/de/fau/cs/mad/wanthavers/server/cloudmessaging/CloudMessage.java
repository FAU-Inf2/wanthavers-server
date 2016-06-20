package de.fau.cs.mad.wanthavers.server.cloudmessaging;


import org.json.JSONObject;


public class CloudMessage {
    private long userId;
    private JSONObject data;
    private JSONObject notification;

    /**
     * @param userId: id of receiver
     * @param subject: data field used for storing a String to identify further procedure of the app for this notification
     * @param title: title to display in notification center
     * @param message: message to display in notification center
     */
    public CloudMessage(long userId, String subject, String title, String message) {
        this.userId = userId;

        notification = new JSONObject();
        notification.put("body", message);
        notification.put("title", subject);
        notification.put("icon", "");

        data = new JSONObject();
        data.put("subject", subject);
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void addKeyValue(String key, Object value) {
        data.put(key, value);
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
