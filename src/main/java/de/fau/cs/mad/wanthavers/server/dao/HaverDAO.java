package de.fau.cs.mad.wanthavers.server.dao;

import de.fau.cs.mad.wanthavers.common.Haver;
import de.fau.cs.mad.wanthavers.common.HaverStatus;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class HaverDAO extends AbstractSuperDAO<Haver> {

    public HaverDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }


    /**
     * @param desireId
     * @return list of found havers with given desireId
     */
    private List<Haver> getHaversByDesireId(long desireId) {
        final Session session = currentSession();

        Criteria criteria = session.createCriteria(Haver.class)
                .add(Restrictions.eq("desireId", desireId));
//                .add(Restrictions.ne("status", HaverStatus.DELETED));

        List<Haver> havers = criteria.list();

        return havers;
    }

    /**
     * @param desireId
     * @param haver
     * @return null if desireId of haver does not match param desireId
     */
    private Haver checkForCorrectDesireId(long desireId, Haver haver) {
        return haver.getDesireId() == desireId ? haver : null;
    }

    public List<Haver> findAll(long desireId) {
        return getHaversByDesireId(desireId);
    }

    public Haver findById(long desireId, long id) {
        Haver haver = super.findById(id);
        return checkForCorrectDesireId(desireId, haver);
    }

    public Haver update(long desireId, long haverId, Haver newHaver) {
        Haver stored = findById(desireId, haverId);
        if (stored == null || checkForCorrectDesireId(desireId, stored) == null) return null;

        newHaver.setId(stored.getId());
        newHaver.setUser(stored.getUser());
        newHaver.setCreationDate(stored.getCreationDate());
        currentSession().merge(newHaver);

        //persist(newHaver);
        return newHaver;
    }

    public Haver getAccepted(long desireId) {
        final Session session = currentSession();

        Criteria criteria = session.createCriteria(Haver.class)
                .add(Restrictions.eq("desireId", desireId))
                .add(Restrictions.eq("status", HaverStatus.ACCEPTED));

        return  (Haver) criteria.uniqueResult();
    }

    public Haver getHaverByUserId(long desireId, long userId) {
        final Session session = currentSession();

        Criteria criteria = session.createCriteria(Haver.class)
                .add(Restrictions.eq("desireId", desireId))
                .add(Restrictions.eq("user.id", userId))
                .add(Restrictions.ne("status", HaverStatus.DELETED));

        return (Haver) criteria.uniqueResult();
    }
}
