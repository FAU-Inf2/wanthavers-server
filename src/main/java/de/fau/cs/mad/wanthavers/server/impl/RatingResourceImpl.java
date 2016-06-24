package de.fau.cs.mad.wanthavers.server.impl;

import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.Haver;
import de.fau.cs.mad.wanthavers.common.Rating;
import de.fau.cs.mad.wanthavers.common.User;
import de.fau.cs.mad.wanthavers.common.rest.api.RatingResource;
import de.fau.cs.mad.wanthavers.server.SingletonManager;
import de.fau.cs.mad.wanthavers.server.dummy.Dummies;
import de.fau.cs.mad.wanthavers.server.facade.DesireFacade;
import de.fau.cs.mad.wanthavers.server.facade.HaverFacade;
import de.fau.cs.mad.wanthavers.server.facade.RatingFacade;
import de.fau.cs.mad.wanthavers.server.facade.UserFacade;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.ApiParam;

import javax.ws.rs.WebApplicationException;
import java.util.Date;
import java.util.List;


public class RatingResourceImpl implements RatingResource {
    private static int dummyExecuted = 0;

    private final RatingFacade ratingFacade;
    private final UserFacade userFacade;

    public RatingResourceImpl(RatingFacade ratingFacade, UserFacade userFacade) {
        this.ratingFacade = ratingFacade;
        this.userFacade = userFacade;
    }

    @Override
    @UnitOfWork
    public List<Rating> getAllRatings(@ApiParam(value = "id of the desired user", required = true) long userId) {
        return this.ratingFacade.getAllRatings(userId);
    }

    @Override
    @UnitOfWork
    public Rating createRating(@Auth User rater, long userId, long desireId, float stars, String comment) {
        checkIfAlreadyRated(rater, desireId);

        Desire desire = getDesire(desireId);

        Haver haver = getHaver(desireId);

        checkPermission(rater.getId(), userId, desire, haver);

        Rating newRating = new Rating(userId, new Date(System.currentTimeMillis()), stars, comment, rater, desire);
        Rating ret = ratingFacade.createNewRating(userId, newRating);

        if (rater.getId() == desire.getCreator().getId()) {
            desire.setCreatorHasRated(true);
        }

        if (rater.getId() == haver.getUser().getId()) {
            desire.setHaverHasRated(true);
        }

        ((DesireFacade) SingletonManager.get(DesireFacade.class)).updateDesire(desireId, desire);

        updateUserAvgRating(userId);
        return ret;
    }

    @Override
    @UnitOfWork
    public Rating get(@ApiParam(value = "id of the desired user", required = true) long userId, @ApiParam(value = "id of the rating", required = true) long id) {
        Rating rating = ratingFacade.getRatingByID(userId, id);

        if (rating == null)
            throw new WebApplicationException(404);

        return rating;
    }

    @Override
    @UnitOfWork
    public Rating updateRating(@Auth User rater, @ApiParam(value = "id of the desired user", required = true) long userId, @ApiParam(value = "id of the rating", required = true) long id, float stars, String comment) {
        getAndCheckRating(rater, userId, id);

        Rating ret = ratingFacade.updateRating(userId, id, stars, comment);

        if (ret == null) {
            throw new WebApplicationException(400);
        }

        updateUserAvgRating(userId);
        return ret;
    }

    @Override
    @UnitOfWork
    public void deleteRating(@ApiParam(value = "id of the desired user", required = true) long userId, @ApiParam(value = "id of the to be deleted rating", required = true) long id) {
        ratingFacade.deleteRating(userId, id);
        updateUserAvgRating(userId);
    }

    @Override
    @UnitOfWork
    public Rating avgRating(@ApiParam(value = "id of the desired user", required = true) long userId) {
        return ratingFacade.avgRating(userId);
    }

    @Override
    @UnitOfWork
    public void createDummies(@ApiParam(value = "id of the desired user", required = true) long userId) {
        if (dummyExecuted > 1) {
            return;
        }

        Rating[] ratings = Dummies.getRatings(userId);

        for (Rating r : ratings) {
            ratingFacade.createNewRating(userId, r);
        }

        updateUserAvgRating(userId);

        dummyExecuted++;
    }

    private void updateUserAvgRating(long userId) {
        Rating avgRating = ratingFacade.avgRating(userId);
        User user = userFacade.getUserByID(userId);
        user.setRating(avgRating.getStars());
        userFacade.updateUser(userId, user);
    }

    private void checkPermission(long raterId, long rateeId, Desire desire, Haver haver) throws WebApplicationException {
        if (raterId != desire.getCreator().getId() && raterId != haver.getUser().getId()) {
            throw new WebApplicationException(409);
        }

        if (rateeId != desire.getCreator().getId() && rateeId != haver.getUser().getId()) {
            throw new WebApplicationException(409);
        }
    }

    private Desire getDesire(long desireId) throws WebApplicationException {
        Desire desire = ((DesireFacade) SingletonManager.get(DesireFacade.class)).getDesireByID(desireId);
        if (desire == null) {
            throw new WebApplicationException("desire not found", 404);
        }

        return desire;
    }

    private Haver getHaver(long desireId) throws WebApplicationException {
        Haver haver = ((HaverFacade) SingletonManager.get(HaverFacade.class)).getAccepted(desireId);
        if (haver == null) {
            throw new WebApplicationException("accepted haver not found", 404);
        }

        return haver;
    }

    private Rating getAndCheckRating(User rater, long userId, long id) throws WebApplicationException {
        Rating stored = ratingFacade.getRatingByID(userId, id);
        if (stored == null) {
            throw new WebApplicationException("rating not found", 404);
        }

        if (stored.getRater().getId() != rater.getId()) {
            throw new WebApplicationException("auth user is not the rater of rating", 403);
        }

        return stored;
    }

    private void checkIfAlreadyRated(User rater, long desireId) {
        List<Rating> ratings = ratingFacade.getRatingsForDesire(desireId);
        for (Rating r : ratings) {
            if (rater.getId() == r.getRater().getId()) {
                throw new WebApplicationException("already rated", 409);
            }
        }
    }
}
