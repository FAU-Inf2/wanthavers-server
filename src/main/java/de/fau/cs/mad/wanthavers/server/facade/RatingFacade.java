package de.fau.cs.mad.wanthavers.server.facade;

import de.fau.cs.mad.wanthavers.common.Rating;
import de.fau.cs.mad.wanthavers.server.dao.RatingDAO;

import java.util.List;

public class RatingFacade {
    private final RatingDAO dao;

    public RatingFacade(RatingDAO dao) {
        this.dao = dao;
    }

    public List<Rating> getAllRatings(long userId) {
        return this.dao.findAll(userId);
    }

    public Rating getRatingByID(long userId, long id) {
        return this.dao.findById(userId, id);
    }

    public Rating createNewRating(long userId, Rating newRating) {
        return this.dao.create(newRating);
    }

    public Rating updateRating(long userId, long id, float stars, String comment) {
        return this.dao.update(userId, id, stars, comment);
    }

    public boolean deleteRating(long userId, long id) {
        return this.dao.delete(dao.findById(userId, id));
    }

    public Rating avgRating(long userId) {
        return this.dao.average(userId);
    }
}
