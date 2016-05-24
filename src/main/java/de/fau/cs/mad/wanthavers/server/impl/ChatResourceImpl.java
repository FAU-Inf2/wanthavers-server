package de.fau.cs.mad.wanthavers.server.impl;

import de.fau.cs.mad.wanthavers.common.Chat;
import de.fau.cs.mad.wanthavers.common.Message;
import de.fau.cs.mad.wanthavers.common.User;
import de.fau.cs.mad.wanthavers.common.rest.api.ChatResource;
import de.fau.cs.mad.wanthavers.server.facade.ChatFacade;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;


import javax.ws.rs.WebApplicationException;
import java.util.List;

public class ChatResourceImpl implements ChatResource {
    private final ChatFacade facade;

    public ChatResourceImpl(ChatFacade facade) {
        this.facade = facade;
    }


    @UnitOfWork
    @Override
    public List<Chat> get(@Auth User user) {
        List<Chat> tmp = this.facade.getChatsByUser(user);
        if(tmp == null){
            throw new WebApplicationException(404);
        }
        return tmp;
    }


    @UnitOfWork
    @Override
    public List<Message> getMessages(String id, @Auth User user) {
        List<Message> tmp = this.facade.getMessagesByChat(id, user);
        if(tmp == null){
            throw new WebApplicationException(404);
        }
        return tmp;
    }


    @UnitOfWork
    @Override
    public Chat createChat(@Auth User user, Chat chat) {
        Chat tmp = this.facade.createChat(chat.getUser1(), chat.getUser2(), chat.getDesireId());
        if(tmp == null){
            throw new WebApplicationException(400);
        }
        return tmp;
    }


    @UnitOfWork
    @Override
    public Message createMessage(@Auth User user, String id, Message msg) {
        Message tmp = this.facade.createMessage(id, user, msg.getBody());
        if(tmp == null){
            throw new WebApplicationException(400);
        }
        return tmp;
    }
}
