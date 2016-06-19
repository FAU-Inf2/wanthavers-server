package de.fau.cs.mad.wanthavers.server.parse.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.parse4j.ParseClassName;
import org.parse4j.ParseObject;

@ParseClassName("Message")
public class ParseMessage extends ParseObject {

    public static String from      = "from";
    public static String chatId    = "chatId";
    public static String body      = "body";

    public void setFrom(Long value) {
        put(from, value);
    }

    @JsonProperty
    public Long getFrom() {
        return getLong(from);
    }

    public void setChatId(String value) {
        put(chatId, ParseObject.createWithoutData("Chat", value));
    }

    public ParseChat getChat(){
        return (ParseChat) getParseObject(chatId);
    }

    public void setBody(String value) {
        put(body, value);
    }

    @JsonProperty
    public String getBody() {
        return getString(body);
    }
}