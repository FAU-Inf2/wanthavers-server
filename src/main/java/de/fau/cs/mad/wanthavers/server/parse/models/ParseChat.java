package de.fau.cs.mad.wanthavers.server.parse.models;

import de.fau.cs.mad.wanthavers.common.Chat;
import org.parse4j.ParseClassName;
import org.parse4j.ParseObject;

@ParseClassName("Chat")
public class ParseChat extends ParseObject {
    public static String user1 = "user1";
    public static String user2 = "user2";
    public static String desireId = "desireId";
    //public static String chatId = "chatId";

    public void setUser1(long value) {
        put(user1, value);
    }

    public long getUser1() {
        return getLong(user1);
    }

    public void setUser2(long value) {
        put(user2, value);
    }

    public long getUser2() {
        return getLong(user2);
    }

    public void setDesireId(long value) {
        put(desireId, value);
    }

    public long getDesireId() {
        return getLong(desireId);
    }
/*
    public void setChatId(long value) {
        put(chatId, value);
    }

    public long getChatId() {
        return getLong(chatId);
    }
*/
}