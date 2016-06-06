package de.fau.cs.mad.wanthavers.server.dao;

import de.fau.cs.mad.wanthavers.common.Chat;
import de.fau.cs.mad.wanthavers.common.Message;
import de.fau.cs.mad.wanthavers.common.User;
import de.fau.cs.mad.wanthavers.server.parse.mapper.ChatMapper;
import de.fau.cs.mad.wanthavers.server.parse.mapper.MessageMapper;
import de.fau.cs.mad.wanthavers.server.parse.models.ParseChat;
import de.fau.cs.mad.wanthavers.server.parse.models.ParseMessage;
import io.dropwizard.auth.Auth;
import org.parse4j.Parse;
import org.parse4j.ParseException;
import org.parse4j.ParseObject;
import org.parse4j.ParseQuery;
import org.parse4j.util.ParseRegistry;

import java.util.ArrayList;
import java.util.List;

public class ChatDAO{

    public ChatDAO() {
        ParseRegistry.registerSubclass(ParseChat.class);
        ParseRegistry.registerSubclass(ParseMessage.class);
        Parse.initialize("wanthavers", "", "http://chat:8090/parse");
    }

    public List<Chat> getChatsByUser(User u) {
        ParseQuery<ParseChat> query = ParseQuery.getQuery(ParseChat.class);
        query.whereEqualTo(ParseChat.user1, u.getID());

        List<Chat> list = new ArrayList<>();
        try {
            list.addAll(ChatMapper.get(query.find()));
        } catch (ParseException e) {}

        ParseQuery<ParseChat> query2 = ParseQuery.getQuery(ParseChat.class);
        query2.whereEqualTo(ParseChat.user2, u.getID());

        try {
            list.addAll(ChatMapper.get(query2.find()));
        } catch (ParseException e) {}

        return list;
    }

    public Chat createChat(long u1, long u2, long desireId){
        ParseChat c = new ParseChat();
        c.setDesireId(desireId);
        c.setUser1(u1);
        c.setUser2(u2);
        c.setChatId(Long.valueOf(c.getChatId()));

        try {
            c.save();
            return ChatMapper.get(c);
        } catch (ParseException e) {
            return null;
        }
    }

    public List<Message> getMessages(String chatId, User user){
        ParseQuery<ParseMessage> query = ParseQuery.getQuery(ParseMessage.class);
        query.whereEqualTo(ParseMessage.chatId, ParseObject.createWithoutData("Chat", chatId));
        try {
            return MessageMapper.get(query.find());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Message createMessage(String chatId, User user, String body){
        ParseMessage m = new ParseMessage();
        m.setBody(body);
        m.setFrom(user.getID());
        m.setChatId(chatId);

        try {
            m.save();
            return MessageMapper.get(m);
        } catch (ParseException e) {
            return null;
        }
    }

    public Chat getChatByChatId(String chatId, User user) {
        ParseQuery<ParseChat> query = ParseQuery.getQuery(ParseChat.class);
        query.whereEqualTo(ParseChat.chatId, chatId);

        List<Chat> list = new ArrayList<>();
        try {
            list.addAll(ChatMapper.get(query.find()));
            return list.get(0);
        } catch (ParseException e) {
            return null;
        }
    }


}
