package de.fau.cs.mad.wanthavers.server.dao;

import de.fau.cs.mad.wanthavers.common.Desire;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

import java.util.List;

public class DesireDAO extends AbstractDAO<Desire>{

    private final SessionFactory sessionFactory;

    /**
     * Creates a new DAO with a given session provider.
     *
     * @param sessionFactory a session provider
     */
    public DesireDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    public Desire findById(long id) {
        return super.get(id);
    }

    public Desire create(Desire desire){
        return persist(desire);
    }

    public Desire update(long id, Desire modified){
        Desire stored = findById(id);

        stored.setCreation_time(modified.getCreation_time());
        stored.setCreator(modified.getCreator());
        stored.setDescription(modified.getDescription());
        stored.setDropzone_lat(modified.getDropzone_lat());
        stored.setDropzone_long(modified.getDropzone_long());
        stored.setDropzone_string(modified.getDropzone_string());
        stored.setPrice(modified.getPrice());
        stored.setReward(modified.getReward());
        stored.setTitle(modified.getTitle());

        persist(stored);
        return stored;
    }

    public boolean delete(Desire Desire){
        if(Desire == null){
            return false;
        }

        currentSession().delete(Desire);

        return true;
    }

    public List<Desire> findAll(){
        Query query = super.currentSession().createQuery("SELECT d FROM Desire d");
        List<Desire> result = super.list(query);
        return result;
    }
}
