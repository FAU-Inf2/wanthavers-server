package de.fau.cs.mad.wanthavers.server.dao;

import de.fau.cs.mad.wanthavers.common.Category;
import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.server.SingletonManager;
import de.fau.cs.mad.wanthavers.server.facade.CategoryFacade;
import de.fau.cs.mad.wanthavers.server.facade.UserFacade;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.List;

public class DesireDAO extends AbstractSuperDAO<Desire> {

    public DesireDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }


    public Desire update(long id, Desire modified) {
        Desire stored = super.findById(id);
        modified.setId(stored.getId());
        currentSession().merge(modified);

        //persist(modified);
        return modified;
    }

    public Desire updateDesireStatus(long desireId, int status) {
        Desire stored = super.findById(desireId);

        if (stored == null) {
            return null;
        }

        stored.setStatus(status);

        return persist(stored);
    }

    public List<Desire> findAllByFilter(Long categoryId, Double price_min, Double price_max, Double reward_min, Float rating_min, Double lat, Double lon, Double radius, List<Integer> status, Long lastDesireId, Integer limit, Long creatorId, Long haverId, List<Integer> haverStatus) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Desire.class, "d");

        if (categoryId != null && categoryId != 0) {
            CategoryFacade categoryFacade = (CategoryFacade) SingletonManager.get(CategoryFacade.class);
            List<Category> categories = categoryFacade.getSubCategoriesDeep(categoryId);
            ArrayList<Long> ids = new ArrayList<>();
            for (Category c : categories) {
                ids.add(c.getId());
            }
            if (!ids.isEmpty()) {
                criteria.add(Restrictions.in("categoryId", ids));
            } else {
                return new ArrayList<>(); //return empty desire list if a non-existing category is given
            }
        }

        if (price_min != null) {
            criteria.add(Restrictions.ge("price", price_min));
        }

        if (price_max != null) {
            criteria.add(Restrictions.le("price", price_max));
        }

        if (reward_min != null) {
            criteria.add(Restrictions.ge("reward", reward_min));
        }

        if (rating_min != null) {
            criteria.createAlias("creator", "u");
            criteria.add(Restrictions.ge("u.avgRating", rating_min));
        }

        if (radius != null && lon != null && lat != null) {
            double latDiff = getLatDiff(radius);
            double lonDiff = getLonDiff(lat, radius);
            double latMin = lat - latDiff;
            double latMax = lat + latDiff;
            double lonMin = lon - lonDiff;
            double lonMax = lon + lonDiff;

            /*
            Coordinate top = calcEndPoint(new Coordinate(lat, lon), radius.intValue(), 0);
            Coordinate bottom = calcEndPoint(new Coordinate(lat, lon), radius.intValue(), 180);
            Coordinate left = calcEndPoint(new Coordinate(lat, lon), radius.intValue(), 270);
            Coordinate right = calcEndPoint(new Coordinate(lat, lon), radius.intValue(), 90);

            latMin = left.lat;
            latMax = right.lat;
            lonMin = top.lon;
            lonMax = bottom.lon;
            */

            criteria.add(Restrictions.between("dropzone_lat", latMin, latMax));
            criteria.add(Restrictions.between("dropzone_long", lonMin, lonMax));
        }

        if (status != null && !status.isEmpty()) {
            criteria.add(Restrictions.in("status", status));
        }

        if (lastDesireId != null) {
            criteria.add(Restrictions.lt("id", lastDesireId));
        }

        if (limit != null && limit > 0) {
            criteria.setMaxResults(limit);
        }

        if (creatorId != null) {
            criteria.add(Restrictions.eq("creator.id", creatorId));
        }

        if (haverId != null) {
            UserFacade userFacade = (UserFacade) SingletonManager.get(UserFacade.class);
            List<Desire> desiresAsHaver = userFacade.getDesiresAsHaver(haverId, haverStatus);

            ArrayList<Long> ids = new ArrayList<>();
            for (Desire d : desiresAsHaver) {
                ids.add(d.getId());
            }

            if (!ids.isEmpty()) {
                criteria.add(Restrictions.in("id", ids));
            } else {
                return new ArrayList<>(); //return empty desire list if a user has no desires as Haver
            }
        }

        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria.addOrder(Order.desc("id"));

        List<Desire> retList = criteria.list();

        if (lon != null && lat != null) {
            for(int i = 0; i < retList.size(); i++) {
                Desire d = retList.get(i);
                double desireLat = d.getDropzone_lat();
                double desireLon = d.getDropzone_long();
                long distanceToUserPosition = Math.round(distanceBetween(lat, lon, desireLat, desireLon));
                retList.get(i).setDistanceToUserPosition(distanceToUserPosition);
            }
        }

        return retList;
    }

    private double getLatDiff(double radius) {
        double tmp = radius * (360. / 40075.);
        return tmp;
    }

    private double getLonDiff(double lon, double radius) {
        double tmp = radius * 360. / (Math.cos(lon) * 40075.);
        return tmp;
    }

    private double distanceBetween(double lat1, double lon1, double lat2, double lon2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c;
    }

    private  static Coordinate calcEndPoint(Coordinate center , int distance, double  bearing)
    {
        Coordinate gp=null;

        double R = 6371000; // meters , earth Radius approx
        double PI = 3.1415926535;
        double RADIANS = PI/180;
        double DEGREES = 180/PI;

        double lat2;
        double lon2;

        double lat1 = center.lat * RADIANS;
        double lon1 = center.lon * RADIANS;
        double radbear = bearing * RADIANS;

        lat2 = Math.asin( Math.sin(lat1)*Math.cos(distance / R) +
                Math.cos(lat1)*Math.sin(distance/R)*Math.cos(radbear) );
        lon2 = lon1 + Math.atan2(Math.sin(radbear)*Math.sin(distance / R)*Math.cos(lat1),
                Math.cos(distance/R)-Math.sin(lat1)*Math.sin(lat2));

        gp = new Coordinate( lon2*DEGREES, lat2*DEGREES);

        return(gp);
    }

    private static class Coordinate {
        public double lat;
        public double lon;
        public Coordinate(double lat, double lon){
            this.lat = lat;
            this.lon = lon;
        }
    }
}
