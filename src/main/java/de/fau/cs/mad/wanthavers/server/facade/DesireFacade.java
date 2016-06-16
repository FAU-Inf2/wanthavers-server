package de.fau.cs.mad.wanthavers.server.facade;

import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.server.dao.DesireDAO;

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
        return this.dao.updateDesireStatus(desireId, status);
    }

}
