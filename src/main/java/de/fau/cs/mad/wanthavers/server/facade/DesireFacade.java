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
import de.fau.cs.mad.wanthavers.server.misc.DynamicStringParser;

import java.util.List;

import static de.fau.cs.mad.wanthavers.server.misc.TranslationHelper.getTranslatedString;

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

            User user1 = desire.getCreator();
            System.out.println("UpdateDesireStatus: user1: "+user1+user1.getName());
            User user2 = haverDAO.getAccepted(desireId).getUser();
            System.out.println("UpdateDesireStatus: user2: "+user2+user2.getName());



            User[] users = new User[]{
                    user1,
                    user2
            };

            for (User user : users) {
                DynamicStringParser cloudMessageStr = DynamicStringParser.parse(
                        getTranslatedString("DESIRE_COMPLETE_NOTIFICATION_BODY", desire.getCreator().getLangCode()));
                cloudMessageStr = cloudMessageStr.set("desire", desire.getTitle());

                CloudMessage message = new CloudMessage(user.getId(), CloudMessageSubject.DESIRECOMPLETE,
                        cloudMessageStr.getValue());
                message.addKeyValue(CloudMessageSubject.DESIRECOMPLETE_DESIREID, desireId);
                message.addKeyValue(CloudMessageSubject.DESIRECOMPLETE_DESIRETITLE, desire.getTitle());
                CloudMessageSender.sendMessage(message);
            }
        }

        return ret;
    }

}
