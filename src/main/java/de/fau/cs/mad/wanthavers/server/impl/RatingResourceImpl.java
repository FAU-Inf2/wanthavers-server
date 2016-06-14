package de.fau.cs.mad.wanthavers.server.impl;

import de.fau.cs.mad.wanthavers.common.Rating;
import de.fau.cs.mad.wanthavers.common.User;
import de.fau.cs.mad.wanthavers.common.rest.api.RatingResource;
import de.fau.cs.mad.wanthavers.server.dummy.Dummies;
import de.fau.cs.mad.wanthavers.server.facade.RatingFacade;
import de.fau.cs.mad.wanthavers.server.facade.UserFacade;
import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.ApiParam;

import javax.ws.rs.WebApplicationException;
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
    public Rating get(@ApiParam(value = "id of the desired user", required = true) long userId, @ApiParam(value = "id of the rating", required = true) long id) {
        Rating rating = ratingFacade.getRatingByID(userId, id);

        if(rating == null)
            throw new WebApplicationException(404);

        return rating;
    }

    @Override
    @UnitOfWork
    public Rating createRating(@ApiParam(value = "id of the desired user", required = true) long userId, @ApiParam(value = "Rating to create", required = true) Rating newRating) {
        Rating ret = ratingFacade.createNewRating(userId, newRating);
        updateUserAvgRating(userId);
        return ret;
    }

    @Override
    @UnitOfWork
    public Rating updateRating(@ApiParam(value = "id of the desired user", required = true) long userId, @ApiParam(value = "id of the rating", required = true) long id, @ApiParam(value = "new details of the specified rating", required = true) Rating rating) {
        Rating ret = ratingFacade.updateRating(userId, id, rating);
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
        if(dummyExecuted > 1) {
            return;
        }

        Rating[] ratings = Dummies.getRatings(userId);

        for(Rating r : ratings)
            createRating(userId, r);

        dummyExecuted++;
    }

    private void updateUserAvgRating(long userId) {
        Rating avgRating = ratingFacade.avgRating(userId);
        User user = userFacade.getUserByID(userId);
        user.setRating(avgRating.getStars());
        userFacade.updateUser(userId, user);
    }

}
