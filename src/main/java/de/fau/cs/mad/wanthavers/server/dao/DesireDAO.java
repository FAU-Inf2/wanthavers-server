package de.fau.cs.mad.wanthavers.server.dao;

import de.fau.cs.mad.wanthavers.common.Desire;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import javax.swing.*;
import java.util.List;

public class DesireDAO extends AbstractDAO<Desire> { //TODO: extends AbstractTimestampDAO<Desire> {

    private final SessionFactory sessionFactory; //TODO: remove

    /**
     * Creates a new DAO with a given session provider.
     *
     * @param sessionFactory a session provider
     */
    public DesireDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.sessionFactory = sessionFactory; //TODO: remove

    }

    public Desire findById(long id) {
        return super.get(id);
    }

    public Desire create(Desire desire) {
        return persist(desire);
    }

    public Desire update(long id, Desire modified) {
        Desire stored = findById(id);
        modified.setId(stored.getID());
        //TODO: super.update(stored);
        persist(modified);
        return modified;
    }

    public Desire updateDesireStatus(long desireId, int status) {
        Desire stored = findById(desireId);

        if (stored == null) {
            return null;
        }

        stored.setStatus(status);

        return persist(stored);
    }

    public boolean delete(Desire Desire) {
        if (Desire == null) {
            return false;
        }

        currentSession().delete(Desire);

        return true;
    }

    //TODO: change DesireResource to expect a Desire object for simple get call
    public List<Desire> findAll() {
        //TODO: if param Desire object == null -> call super.findAllNew(0), so that it returns every Desire
        Query query = super.currentSession().createQuery("SELECT d FROM Desire d");
        List<Desire> result = super.list(query);
        return result;
    }

    public List<Desire> findAllByFilter(Double price_min, Double price_max, Double reward_min, Double lat, Double lon, Double radius, List<Integer> status) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Desire.class);
        if (price_min != null)
            criteria.add(Restrictions.ge("price", price_min));

        if (price_max != null && price_max > price_min)
            criteria.add(Restrictions.le("price", price_max));

        if (reward_min != null)
            criteria.add(Restrictions.ge("reward", reward_min));

        if (radius != null && lon != null && lat != null) {
            double latDiff = getLatDiff(radius);
            double lonDiff = getLonDiff(lon, radius);
            double latMin = lat - latDiff;
            double latMax = lat + latDiff;
            double lonMin = lon - lonDiff;
            double lonMax = lon + lonDiff;
            criteria.add(Restrictions.between("dropzone_lat", latMin, latMax));
            criteria.add(Restrictions.between("dropzone_long", lonMin, lonMax));
        }

        if(status != null){
            criteria.add(Restrictions.in("status", status));
        }

        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        return criteria.list();
    }

    private double getLatDiff(double radius) {
        double tmp = radius * (360. / 40075.);
        return tmp;
    }

    private double getLonDiff(double lon, double radius) {
        double tmp = radius * 360. / (Math.cos(lon) * 40075.);
        return tmp;
    }

}
