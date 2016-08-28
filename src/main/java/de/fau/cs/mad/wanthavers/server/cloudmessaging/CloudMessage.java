package de.fau.cs.mad.wanthavers.server.cloudmessaging;


import org.json.JSONObject;


public class CloudMessage {
    private long userId;
    private JSONObject data;


    public CloudMessage(long userId, String subject, String message) {
        this(userId, subject, message, null);
    }
    /**
     * @param userId: id of receiver
     * @param subject: data field used for storing a String to identify further procedure of the app for this notification
     * @param message: message to display in notification center
     * @param title: title to display in notification center
     */
    public CloudMessage(long userId, String subject, String message, String title) {
        this.userId = userId;

        data = new JSONObject();
        data.put("subject", subject);
        data.put("message", message);
        data.put("title", title);
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getSubject() {
        return data.getString("subject");
    }

    public String getMessage() {
        return data.getString("message");
    }

    public String getTitle() {
        return data.getString("title");
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

    public JSONObject getDataWithoutHeader() {
        JSONObject dataCopy = new JSONObject(data);
        dataCopy.remove("subject");
        dataCopy.remove("message");
        dataCopy.remove("title");
        return dataCopy;
    }

}
