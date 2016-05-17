package de.fau.cs.mad.wanthavers.server.parse.mapper;


import de.fau.cs.mad.wanthavers.common.Chat;
import de.fau.cs.mad.wanthavers.common.Message;
import de.fau.cs.mad.wanthavers.server.parse.models.ParseChat;
import de.fau.cs.mad.wanthavers.server.parse.models.ParseMessage;

import java.util.ArrayList;
import java.util.List;

public class MessageMapper {

    public static Message get(ParseMessage pm){
        Message m = new Message();
        m.setBody(pm.getBody());
        m.setFrom(pm.getFrom());
        m.setCreatedAt(pm.getCreatedAt());
        m.setUpdatedAt(pm.getUpdatedAt());
        m.setObjectId(pm.getObjectId());

        return m;
    }

    public static List<Message> get(List<ParseMessage> list){
        if(list == null){
            return new ArrayList<>();
        }
        List<Message> tmp = new ArrayList<>();
        for(ParseMessage pm : list){
            tmp.add(get(pm));
        }
        return tmp;
    }

}
