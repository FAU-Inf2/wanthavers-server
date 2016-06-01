package de.fau.cs.mad.wanthavers.server.dao;

import de.fau.cs.mad.wanthavers.common.Desire;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

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
        stored.setColorIndex(modified.getColorIndex());
        stored.setImage(modified.getImage());
        stored.setStatus(modified.getStatus());
        //TODO: super.update(stored);

        persist(stored);
        return stored;
    }

    public Desire updateDesireStatus(long desireId, int status) {
        Desire stored = findById(desireId);

        if(stored == null) {
            return null;
        }

        stored.setStatus(status);

        return persist(stored);
    }

    public boolean delete(Desire Desire){
        if(Desire == null){
            return false;
        }

        currentSession().delete(Desire);

        return true;
    }

    //TODO: change DesireResource to expect a Desire object for simple get call
    public List<Desire> findAll(){
        //TODO: if param Desire object == null -> call super.findAllNew(0), so that it returns every Desire
        Query query = super.currentSession().createQuery("SELECT d FROM Desire d");
        List<Desire> result = super.list(query);
        return result;
    }

    public List<Desire> findAllByLocation(double lat, double lon, double radius){
        double latDiff = getLatDiff(radius);
        double lonDiff = getLonDiff(lon, radius);
        double latMin = lat - latDiff;
        double latMax = lat + latDiff;
        double lonMin = lon - lonDiff;
        double lonMax = lon + lonDiff;
        Query query = super.currentSession().createQuery("SELECT d FROM Desire d WHERE dropzone_lat BETWEEN "+latMin+" AND "+latMax+" AND dropzone_long BETWEEN "+lonMin+" AND "+lonMax);
        List<Desire> result = super.list(query);
        return result;
    }

    private double getLatDiff(double radius){
        double tmp = radius * (360. / 40075.);
        return tmp;
    }

    private double getLonDiff(double lon, double radius){
        double tmp = radius * 360./(Math.cos(lon) * 40075.);
        return tmp;
    }


}
