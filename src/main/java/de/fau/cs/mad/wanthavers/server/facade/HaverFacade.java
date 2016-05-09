package de.fau.cs.mad.wanthavers.server.facade;

import de.fau.cs.mad.wanthavers.common.Haver;
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
        return this.dao.create(desireId, newHaver);
    }

    public Haver updateHaver(long desireId, long id, Haver haver) {
        return this.dao.update(desireId, id, haver);
    }

    public boolean deleteHaver(long desireId, long id) {
        return this.dao.delete(dao.findById(desireId, id));
    }

}
