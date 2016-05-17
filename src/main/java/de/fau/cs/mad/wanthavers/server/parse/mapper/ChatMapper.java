package de.fau.cs.mad.wanthavers.server.parse.mapper;


import de.fau.cs.mad.wanthavers.common.Chat;
import de.fau.cs.mad.wanthavers.server.parse.models.ParseChat;

import java.util.ArrayList;
import java.util.List;

public class ChatMapper {

    public static Chat get(ParseChat pc){
        Chat c = new Chat();
        c.setDesireId(pc.getDesireId());
        c.setUser2(pc.getUser2());
        c.setUser1(pc.getUser1());
        c.setCreatedAt(pc.getCreatedAt());
        c.setUpdatedAt(pc.getUpdatedAt());
        c.setObjectId(pc.getObjectId());

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
