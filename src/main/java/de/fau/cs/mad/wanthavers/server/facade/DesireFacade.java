package de.fau.cs.mad.wanthavers.server.facade;

import de.fau.cs.mad.wanthavers.common.CloudMessageSubject;
import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.DesireStatus;
import de.fau.cs.mad.wanthavers.common.User;
import de.fau.cs.mad.wanthavers.server.SingletonManager;
import de.fau.cs.mad.wanthavers.server.cloudmessaging.CloudMessage;
import de.fau.cs.mad.wanthavers.server.cloudmessaging.CloudMessageSender;
import de.fau.cs.mad.wanthavers.server.dao.DesireDAO;
import de.fau.cs.mad.wanthavers.server.dao.HaverDAO;

import java.util.List;

public class DesireFacade {
    private final DesireDAO dao;

    public DesireFacade(DesireDAO dao){
        this.dao = dao;
    }

    public Desire getDesireByID(long id){
        return dao.findById(id);
    }

    public List<Desire> getAllDesiresByFilter(Long categoryId, Double price_min, Double price_max, Double reward_min, Float rating_min, Double lat, Double lon, Double radius, List<Integer> status, Long lastDesireId, Integer limit, Long creatorId, Long haverId, List<Integer> haverStatus) {
        return dao.findAllByFilter(categoryId, price_min, price_max, reward_min, rating_min, lat, lon, radius, status, lastDesireId, limit, creatorId, haverId, haverStatus);
    }

    public Desire createNewDesire(Desire desire){
        return dao.create(desire);
    }

    public Desire updateDesire(long id, Desire desire){
        return dao.update(id, desire);
    }

    public boolean deleteDesire(long id){
        return dao.delete(dao.findById(id));
    }

    public Desire updateDesireStatus(long desireId, int status) {
        Desire ret =  this.dao.updateDesireStatus(desireId, status);

        if(status == DesireStatus.STATUS_DONE) {
            Desire desire = getDesireByID(desireId);
            HaverDAO haverDAO = (HaverDAO) SingletonManager.get(HaverDAO.class);

            User[] users = new User[]{
                    desire.getCreator(),
                    haverDAO.getAccepted(desireId).getUser()
            };

            for (User user : users) {
                CloudMessage message = new CloudMessage(user.getId(), CloudMessageSubject.DESIRECOMPLETE, "Desire completed!", "Desire " + desire.getTitle() + " was completed. Rate now.");
                message.addKeyValue(CloudMessageSubject.DESIRECOMPLETE_DESIREID, desireId);
                message.addKeyValue(CloudMessageSubject.DESIRECOMPLETE_DESIRETITLE, desire.getTitle());
                CloudMessageSender.sendMessage(message);
            }
        }

        return ret;
    }

}
