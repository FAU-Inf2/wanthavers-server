package de.fau.cs.mad.wanthavers.server.impl;

import de.fau.cs.mad.wanthavers.common.Chat;
import de.fau.cs.mad.wanthavers.common.Message;
import de.fau.cs.mad.wanthavers.common.User;
import de.fau.cs.mad.wanthavers.common.rest.api.ChatResource;
import de.fau.cs.mad.wanthavers.server.SingletonManager;
import de.fau.cs.mad.wanthavers.server.facade.ChatFacade;
import de.fau.cs.mad.wanthavers.server.facade.DesireFacade;
import de.fau.cs.mad.wanthavers.server.facade.UserFacade;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;


import javax.ws.rs.WebApplicationException;
import java.util.List;

public class ChatResourceImpl implements ChatResource {
    private final ChatFacade chatFacade;
    private UserFacade userFacade;
    private final DesireFacade desireFacade;


    public ChatResourceImpl(ChatFacade chatFacade, UserFacade userFacade, DesireFacade desireFacade) {
        this.chatFacade = chatFacade;
        this.userFacade = userFacade;
        this.desireFacade = desireFacade;
    }


    @UnitOfWork
    @Override
    public List<Chat> get(@Auth User user) {
        List<Chat> tmp = this.chatFacade.getChatsByUser(user);
        if (tmp == null) {
            throw new WebApplicationException(404);
        }

        return tmp;
    }

    @UnitOfWork
    @Override
    public List<Message> getMessages(@Auth User user, String id, Long lastCreationTime, Integer limit) {
        List<Message> tmp = this.chatFacade.getMessagesByChat(id, user, lastCreationTime, limit);
        if (tmp == null) {
            throw new WebApplicationException(404);
        }
        return tmp;
    }

    @Override
    @UnitOfWork
    public User getOtherUser(@Auth User user, String id) {
        Chat chat = chatFacade.getChatByChatId(id);

        if (chat == null)
            throw new WebApplicationException(404);

        if (user.getId() == chat.getUserObject1().getId()) {
            return chat.getUserObject2();
        }

        return chat.getUserObject1();
    }

    @UnitOfWork
    @Override
    public Chat createChat(@Auth User user, Chat chat) {
        Chat tmp = this.chatFacade.createChat(chat.getUser1(), chat.getUser2(), chat.getDesireId());
        if (tmp == null) {
            throw new WebApplicationException(400);
        }

        return tmp;
    }


    @UnitOfWork
    @Override
    public Message createMessage(@Auth User user, String id, Message msg) {

        ChatResourceImpl chatResource = (ChatResourceImpl) SingletonManager.get(ChatResourceImpl.class);
        User otherUser = chatResource.getOtherUser(user, id);

        if(userFacade.isUserBlocked(otherUser.getId(), user.getId()) || userFacade.isUserBlocked(user.getId(), otherUser.getId())){
            throw new WebApplicationException(423); //Status Code: Locked
        }

        Message tmp = this.chatFacade.createMessage(id, user, msg.getBody());
        if (tmp == null) {
            throw new WebApplicationException(400);
        }
        return tmp;
    }
}
