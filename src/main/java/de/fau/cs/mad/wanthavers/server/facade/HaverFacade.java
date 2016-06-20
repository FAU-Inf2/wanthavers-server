package de.fau.cs.mad.wanthavers.server.facade;

import de.fau.cs.mad.wanthavers.common.*;
import de.fau.cs.mad.wanthavers.server.SingletonManager;
import de.fau.cs.mad.wanthavers.server.cloudmessaging.CloudMessage;
import de.fau.cs.mad.wanthavers.server.cloudmessaging.CloudMessageSender;
import de.fau.cs.mad.wanthavers.server.dao.DesireDAO;
import de.fau.cs.mad.wanthavers.server.dao.HaverDAO;

import java.util.List;

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

        CloudMessage message = new CloudMessage(desire.getCreator().getId(), CloudMessageSubject.NEWHAVER, "New Haver!",
                "A new Haver would like to help you out with "+desire.getTitle()+".");
        message.addKeyValue(CloudMessageSubject.NEWHAVER_DESIREID, desireId);
        message.addKeyValue(CloudMessageSubject.NEWHAVER_DESIRETITLE, desire.getTitle());
        CloudMessageSender.sendMessage(message);

        return ret;
    }

    public Haver updateHaver(long desireId, long id, Haver haver) {
        Haver ret = this.dao.update(desireId, id, haver);

        if(haver.getStatus() == HaverStatus.ACCEPTED) {
            DesireDAO desireDAO = (DesireDAO) SingletonManager.get(DesireDAO.class);
            Desire desire = desireDAO.findById(desireId);

            CloudMessage message = new CloudMessage(haver.getUser().getId(), CloudMessageSubject.HAVERACCEPTED, "You were accepted!",
                    "You were accepted for "+desire.getTitle()+" by "+desire.getCreator().getName()+".");
            message.addKeyValue(CloudMessageSubject.HAVERACCEPTED_DESIREID, desireId);
            message.addKeyValue(CloudMessageSubject.HAVERACCPETED_DESIRETITLE, desire.getTitle());
            CloudMessageSender.sendMessage(message);
        }

        return ret;
    }

    public boolean deleteHaver(long desireId, long id) {
        return this.dao.delete(dao.findById(desireId, id));
    }

    public Haver getAccepted(long desireId){return  this.dao.getAccepted(desireId);}
}
