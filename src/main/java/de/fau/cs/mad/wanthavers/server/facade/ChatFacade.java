package de.fau.cs.mad.wanthavers.server.facade;

import de.fau.cs.mad.wanthavers.common.Chat;
import de.fau.cs.mad.wanthavers.common.Message;
import de.fau.cs.mad.wanthavers.common.User;
import de.fau.cs.mad.wanthavers.server.dao.ChatDAO;

import java.util.List;

public class ChatFacade {
    private final ChatDAO dao;

    public ChatFacade(ChatDAO dao){
        this.dao = dao;
    }

    public List<Chat> getChatsByUser(User u){
        return this.dao.getChatsByUser(u);
    }

    public Chat createChat(long u1, long u2, long desireId){
        return this.dao.createChat(u1, u2, desireId);
    }

    public List<Message> getMessagesByChat(String chatId, User user){
        return this.dao.getMessages(chatId, user);
    }

    public Message createMessage(String chatId, User user, String body){
        return this.dao.createMessage(chatId, user, body);
    }
}
