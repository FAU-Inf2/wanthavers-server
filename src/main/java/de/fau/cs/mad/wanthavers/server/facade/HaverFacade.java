package de.fau.cs.mad.wanthavers.server.facade;

import de.fau.cs.mad.wanthavers.common.*;
import de.fau.cs.mad.wanthavers.server.SingletonManager;
import de.fau.cs.mad.wanthavers.server.cloudmessaging.CloudMessage;
import de.fau.cs.mad.wanthavers.server.cloudmessaging.CloudMessageSender;
import de.fau.cs.mad.wanthavers.server.dao.DesireDAO;
import de.fau.cs.mad.wanthavers.server.dao.HaverDAO;
import de.fau.cs.mad.wanthavers.server.misc.DynamicStringParser;

import java.util.List;

import static de.fau.cs.mad.wanthavers.server.misc.TranslationHelper.getTranslatedString;

public class HaverFacade {
    private final HaverDAO dao;

    public HaverFacade(HaverDAO dao) {
        this.dao = dao;
    }

    public List<Haver> getAllHavers(long desireId) {
        return this.dao.findAll(desireId);
    }

    public Haver getHaverByID(long desireId, long id) {
        return this.dao.findById(desireId, id);
    }

    public Haver createNewHaver(long desireId, Haver newHaver) {
        Haver ret = this.dao.create(newHaver);

        DesireDAO desireDAO = (DesireDAO) SingletonManager.get(DesireDAO.class);
        Desire desire = desireDAO.findById(desireId);

        DynamicStringParser cloudMessageStr = DynamicStringParser.parse(getTranslatedString("HAVER_NEW_NOTIFICATION_BODY", desire.getCreator().getLangCode()));
        cloudMessageStr = cloudMessageStr.set("desire", desire.getTitle());

        CloudMessage message = new CloudMessage(desire.getCreator().getId(),
                CloudMessageSubject.NEWHAVER,
                cloudMessageStr.getValue(),
                getTranslatedString("HAVER_NEW_NOTIFICATION_TITLE", desire.getCreator().getLangCode()));
        message.addKeyValue(CloudMessageSubject.NEWHAVER_DESIREID, desireId);
        message.addKeyValue(CloudMessageSubject.NEWHAVER_DESIRETITLE, desire.getTitle());
        CloudMessageSender.sendMessage(message);

        return ret;
    }

    public Haver updateHaver(long desireId, long haverId, Haver haver) {
        Haver ret = this.dao.update(desireId, haverId, haver);

        /* Send push notifications on accept */
        if(haver.getStatus() == HaverStatus.ACCEPTED) {
            DesireDAO desireDAO = (DesireDAO) SingletonManager.get(DesireDAO.class);
            Desire desire = desireDAO.findById(desireId);

            DynamicStringParser cloudMessageStr = DynamicStringParser.parse(
                    getTranslatedString("HAVER_ACCEPTED_NOTIFICATION_BODY", haver.getUser().getLangCode()));
            cloudMessageStr = cloudMessageStr.set("desireTitle", desire.getTitle());
            cloudMessageStr = cloudMessageStr.set("desireCreator", desire.getCreator().getName());

            CloudMessage message = new CloudMessage(haver.getUser().getId(),
                    CloudMessageSubject.HAVERACCEPTED,
                    cloudMessageStr.getValue(),
                    getTranslatedString("HAVER_ACCEPTED_NOTIFICATION_TITLE", haver.getUser().getLangCode()));
            message.addKeyValue(CloudMessageSubject.HAVERACCEPTED_DESIREID, desireId);
            message.addKeyValue(CloudMessageSubject.HAVERACCPETED_DESIRETITLE, desire.getTitle());
            CloudMessageSender.sendMessage(message);

            List<Haver> rejected = this.dao.findAll(desireId);
            for(Haver h : rejected) {
                if(h.getStatus() == HaverStatus.ACCEPTED)
                    continue;

                cloudMessageStr = DynamicStringParser.parse(
                        getTranslatedString("HAVER_REJECTED_NOTIFICATION_BODY", h.getUser().getLangCode()));
                cloudMessageStr = cloudMessageStr.set("desireTitle", desire.getTitle());
                cloudMessageStr = cloudMessageStr.set("desireCreator", desire.getCreator().getName());

                message = new CloudMessage(h.getUser().getId(),
                        CloudMessageSubject.HAVERREJECTED,
                        cloudMessageStr.getValue(),
                        getTranslatedString("HAVER_REJECTED_NOTIFICATION_TITLE", h.getUser().getLangCode()));
                message.addKeyValue(CloudMessageSubject.HAVERREJECTED_DESIREID, desireId);
                message.addKeyValue(CloudMessageSubject.HAVERREJECTED_DESIRETITLE, desire.getTitle());
                CloudMessageSender.sendMessage(message);
            }
        }

        return ret;
    }

    public boolean deleteHaver(long desireId, long id) {
        return this.dao.delete(dao.findById(desireId, id));
    }

    public Haver getAccepted(long desireId){return  this.dao.getAccepted(desireId);}

    public Haver getHaverByUserId(long desireId, long userId) { return dao.getHaverByUserId(desireId, userId); }
}
