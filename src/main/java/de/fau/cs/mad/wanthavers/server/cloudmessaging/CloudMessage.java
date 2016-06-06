package de.fau.cs.mad.wanthavers.server.cloudmessaging;


import org.json.JSONObject;


public class CloudMessage {
    private String to;
    private JSONObject data;
    
    public CloudMessage(String to, String subject, String message) {
        this.to = to;
        data = new JSONObject();
        data.put("message", message);
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
}
