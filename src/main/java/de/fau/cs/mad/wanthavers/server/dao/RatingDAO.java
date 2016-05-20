package de.fau.cs.mad.wanthavers.server.dao;

import de.fau.cs.mad.wanthavers.common.Rating;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;

import static de.fau.cs.mad.wanthavers.common.User.USER_ID;

public class RatingDAO extends AbstractDAO<Rating>{

    private final SessionFactory sessionFactory;

    public RatingDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.sessionFactory = sessionFactory;
    }


    /**
     *
     * @param userId
     * @return list of found ratings with given userId
     */
    private List<Rating> getRatingsByUserId(long userId) {
        final Session session = currentSession();

        Criteria criteria = session.createCriteria(Rating.class)
                .add(Restrictions.eq(USER_ID, userId));

        List<Rating> ratings = criteria.list();

        return ratings;
    }

    /**
     *
     * @param userId
     * @param rating
     * @return null if userId of rating does not match param userId
     */
    private Rating checkForCorrectUserId(long userId, Rating rating) {
        return rating.getUserId() == userId ? rating : null;
    }

    public List<Rating> findAll(long userId) {
        return getRatingsByUserId(userId);
    }

    public Rating findById(long userId, long id) {
        Rating rating = super.get(id);
        return checkForCorrectUserId(userId, rating);
    }

    public Rating create(long userId, Rating newRating) {
        return persist(newRating);
    }

    public Rating update(long userId, long id, Rating newRating) {
        Rating stored = findById(userId, id);
        if(stored == null || checkForCorrectUserId(userId, stored) == null) return null;

        stored.setRatedTime(newRating.getRatedTime());
        stored.setStars(newRating.getStars());
        stored.setComment(newRating.getComment());
        stored.setRater(newRating.getRater());
        stored.setRatedTransaction(newRating.getRatedTransaction());

        return stored;
    }

    public boolean delete(Rating rating) {
        if(rating == null)
            return false;

        currentSession().delete(rating);

        return true;
    }

    public Rating average(long userId) {
        List<Rating> ratings = getRatingsByUserId(userId);

        if(ratings.size() == 0)
            return new Rating(userId, 0);

        float stars = 0;
        for(Rating r : ratings)
            stars += r.getStars();

        return new Rating(userId, stars / (float) ratings.size());
    }

}
