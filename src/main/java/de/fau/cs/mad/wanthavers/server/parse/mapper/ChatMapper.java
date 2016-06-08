package de.fau.cs.mad.wanthavers.server.parse.mapper;


import de.fau.cs.mad.wanthavers.common.Chat;
import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.server.SingletonManager;
import de.fau.cs.mad.wanthavers.server.dao.ChatDAO;
import de.fau.cs.mad.wanthavers.server.dao.DesireDAO;
import de.fau.cs.mad.wanthavers.server.dao.UserDAO;
import de.fau.cs.mad.wanthavers.server.facade.DesireFacade;
import de.fau.cs.mad.wanthavers.server.facade.UserFacade;
import de.fau.cs.mad.wanthavers.server.parse.models.ParseChat;

import java.util.ArrayList;
import java.util.List;

public class ChatMapper {

    public static Chat get(ParseChat pc){
        DesireFacade desireDAO = (DesireFacade) SingletonManager.get(DesireFacade.class);
        UserFacade userFacade  = (UserFacade) SingletonManager.get(UserFacade.class);

        Chat c = new Chat();
        c.setDesireId(pc.getDesireId());
        c.setUser2(pc.getUser2());
        c.setUser1(pc.getUser1());
        c.setCreatedAt(pc.getCreatedAt());
        c.setUpdatedAt(pc.getUpdatedAt());
        c.setObjectId(pc.getObjectId());
        c.setDesire(desireDAO.getDesireByID(c.getDesireId()));
        c.setUserObject1(userFacade.getUserByID(c.getUser1()));
        c.setUserObject2(userFacade.getUserByID(c.getUser2()));

        return c;
    }

    public static List<Chat> get(List<ParseChat> list){
        if(list == null){
            return new ArrayList<>();
        }
        List<Chat> tmp = new ArrayList<>();
        for(ParseChat pc : list){
            tmp.add(get(pc));
        }
        return tmp;
    }

}
