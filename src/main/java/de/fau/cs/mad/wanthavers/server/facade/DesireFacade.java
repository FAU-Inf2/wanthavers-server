package de.fau.cs.mad.wanthavers.server.facade;

import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.server.dao.DesireDAO;

public class DesireFacade {
    private final DesireDAO dao;

    public DesireFacade(DesireDAO dao){
        this.dao = dao;
    }

    public Desire getDesireByID(long id){
        return dao.findById(id);
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
}
