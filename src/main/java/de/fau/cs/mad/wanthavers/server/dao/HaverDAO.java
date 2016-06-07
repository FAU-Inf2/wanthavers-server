package de.fau.cs.mad.wanthavers.server.dao;

import de.fau.cs.mad.wanthavers.common.Haver;
import de.fau.cs.mad.wanthavers.common.HaverStatus;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class HaverDAO extends AbstractDAO<Haver>{

    private final SessionFactory sessionFactory;

    public HaverDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.sessionFactory = sessionFactory;
    }


    /**
     *
     * @param desireId
     * @return list of found havers with given desireId
     */
    private List<Haver> getHaversByDesireId(long desireId) {
        final Session session = currentSession();

        Criteria criteria = session.createCriteria(Haver.class)
                .add(Restrictions.eq("desireId", desireId));

        List<Haver> havers = criteria.list();

        return havers;
    }

    /**
     *
     * @param desireId
     * @param haver
     * @return null if desireId of haver does not match param desireId
     */
    private Haver checkForCorrectDesireId(long desireId, Haver haver) {
        return haver.getDesireId()== desireId ? haver : null;
    }

    public List<Haver> findAll(long desireId) {
        return getHaversByDesireId(desireId);
    }

    public Haver findById(long desireId, long id) {
        Haver haver = super.get(id);
        return checkForCorrectDesireId(desireId, haver);
    }

    public Haver create(long desireId, Haver newHaver) {
        return persist(newHaver);
    }

    public Haver update(long desireId, long id, Haver newHaver) {
        Haver stored = findById(desireId, id);
        if(stored == null || checkForCorrectDesireId(desireId, stored) == null) return null;

        newHaver.setId(stored.getId());
        persist(newHaver);
        return newHaver;
    }

    public boolean delete(Haver haver) {
        if(haver == null)
            return false;

        currentSession().delete(haver);

        return true;
    }

    public Haver getAccepted(long desireId){
        final Session session = currentSession();

        Criteria criteria = session.createCriteria(Haver.class)
                .add(Restrictions.eq("desireId", desireId))
                .add(Restrictions.eq("status", HaverStatus.ACCEPTED));

        Haver haver = (Haver)criteria.uniqueResult();
        return haver;
    }

}
