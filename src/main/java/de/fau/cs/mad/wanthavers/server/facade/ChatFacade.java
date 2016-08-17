package de.fau.cs.mad.wanthavers.server.facade;

import de.fau.cs.mad.wanthavers.common.*;
import de.fau.cs.mad.wanthavers.server.SingletonManager;
import de.fau.cs.mad.wanthavers.server.cloudmessaging.CloudMessage;
import de.fau.cs.mad.wanthavers.server.cloudmessaging.CloudMessageSender;
import de.fau.cs.mad.wanthavers.server.dao.ChatDAO;
import de.fau.cs.mad.wanthavers.server.dao.CloudMessageTokenDAO;
import io.dropwizard.auth.Auth;

import java.util.List;

public class ChatFacade {
    private final ChatDAO dao;

    public ChatFacade(ChatDAO dao) {
        this.dao = dao;
    }

    public List<Chat> getChatsByUser(User u) {
        return this.dao.getChatsByUser(u);
    }

    public Chat createChat(long u1, long u2, long desireId) {
        return this.dao.createChat(u1, u2, desireId);
    }

    public List<Message> getMessagesByChat(String chatId, User user, Long lastCreationTime, Integer limit) {
        return this.dao.getMessages(chatId, user, lastCreationTime, limit);
    }

    public Message createMessage(String chatId, User user, String body) {
        Message ret = this.dao.createMessage(chatId, user, body);

        CloudMessageTokenDAO tokenDAO = (CloudMessageTokenDAO) SingletonManager.get(CloudMessageTokenDAO.class);
        Chat chat = this.dao.getChatByChatId(chatId);

        // Send message to sender for testing
        User reciever = chat.getUser1() != user.getId() ? chat.getUserObject1() : chat.getUserObject2();

        CloudMessage message = new CloudMessage(reciever.getId(), CloudMessageSubject.NEWMESSAGE, ret.getBody());
        message.addKeyValue(CloudMessageSubject.NEWMESSAGE_SENDER, user.getName());
        message.addKeyValue(CloudMessageSubject.NEWMESSAGE_SENDERID, user.getId());
        message.addKeyValue(CloudMessageSubject.NEWMESSAGE_CHATID, chatId);
        CloudMessageSender.sendMessage(message);

        return ret;
    }

    public Chat getChat(long user1, long user2, long desireId) {
        return this.dao.getOrCreateChat(user1, user2, desireId);
    }

    public Chat getChatByChatId(String chatId) {
        return dao.getChatByChatId(chatId);
    }
}
