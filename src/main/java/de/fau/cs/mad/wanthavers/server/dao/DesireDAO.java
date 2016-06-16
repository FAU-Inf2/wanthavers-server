package de.fau.cs.mad.wanthavers.server.dao;

import de.fau.cs.mad.wanthavers.common.Category;
import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.server.SingletonManager;
import de.fau.cs.mad.wanthavers.server.facade.CategoryFacade;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.Date;
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

    public List<Desire> findAllByFilter(Long categoryId, Double price_min, Double price_max, Double reward_min, Float rating_min, Double lat, Double lon, Double radius, List<Integer> status, Long lastDesireId, Integer limit, Long creatorId) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Desire.class, "d");

        if(categoryId != null && categoryId!=0){
            CategoryFacade categoryFacade = (CategoryFacade)SingletonManager.get(CategoryFacade.class);
            List<Category> categories = categoryFacade.getSubCategoriesDeep(categoryId);
            ArrayList<Long> ids = new ArrayList<>();
            for (Category c: categories) {
                ids.add(c.getId());
                System.out.println(c.getId());
            }
            if(!ids.isEmpty()){
                criteria.add(Restrictions.in("categoryId", ids));
            }else{
                return new ArrayList<>(); //return empty desire list if a non-existing category is given
            }
        }

        if (price_min != null) {
            criteria.add(Restrictions.ge("price", price_min));
        }

        if (price_max != null && price_max > price_min) {
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
            double lonDiff = getLonDiff(lon, radius);
            double latMin = lat - latDiff;
            double latMax = lat + latDiff;
            double lonMin = lon - lonDiff;
            double lonMax = lon + lonDiff;
            criteria.add(Restrictions.between("dropzone_lat", latMin, latMax));
            criteria.add(Restrictions.between("dropzone_long", lonMin, lonMax));
        }

        if (status != null && !status.isEmpty()) {
            criteria.add(Restrictions.in("status", status));
        }

        if (lastDesireId != null) {
            criteria.add(Restrictions.gt("id", lastDesireId));
        }

        if (limit != null && limit > 0) {
            criteria.setMaxResults(limit);
        }

        if (creatorId != null) {
            criteria.add(Restrictions.eq("creator.id", creatorId));
        }

        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria.addOrder(Order.desc("creation_time"));

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
