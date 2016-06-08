package de.fau.cs.mad.wanthavers.server.facade;

import de.fau.cs.mad.wanthavers.common.*;
import de.fau.cs.mad.wanthavers.server.cloudmessaging.CloudMessage;
import de.fau.cs.mad.wanthavers.server.cloudmessaging.CloudMessageSender;
import de.fau.cs.mad.wanthavers.server.dao.ChatDAO;
import de.fau.cs.mad.wanthavers.server.dao.CloudMessageTokenDAO;
import io.dropwizard.auth.Auth;

import java.util.List;

public class ChatFacade {
    private final ChatDAO dao;
    private final CloudMessageTokenDAO tokenDAO;

    public ChatFacade(ChatDAO dao, CloudMessageTokenDAO tokenDAO){
        this.dao = dao;
        this.tokenDAO = tokenDAO;
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
        Message ret = this.dao.createMessage(chatId, user, body);
        /*
        Chat chat = this.dao.getChatByChatId(chatId, user);

        User reciever = user;//chat.getUser1() != user.getID() ? chat.getUserObject1() : chat.getUserObject2();

        List<CloudMessageToken> tokens = tokenDAO.findAll(reciever.getID());

        CloudMessage message = new CloudMessage("to", CloudMessageSubject.NEWMESSAGE, ret.getFrom()+": "+ret.getBody());
        CloudMessageSender.sendMessage(message);
        */
        return ret;
    }

    public Chat getChat(long user1, long user2, long desireId) {
        return this.dao.getOrCreateChat(user1, user2, desireId);
    }
}
