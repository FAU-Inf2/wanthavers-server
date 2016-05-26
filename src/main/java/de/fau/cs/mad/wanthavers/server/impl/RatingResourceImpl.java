package de.fau.cs.mad.wanthavers.server.impl;

import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.Rating;
import de.fau.cs.mad.wanthavers.common.User;
import de.fau.cs.mad.wanthavers.common.rest.api.RatingResource;
import de.fau.cs.mad.wanthavers.server.dummy.Dummies;
import de.fau.cs.mad.wanthavers.server.facade.RatingFacade;
import de.fau.cs.mad.wanthavers.server.facade.UserFacade;
import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.ApiParam;

import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import java.util.Date;
import java.util.List;


public class RatingResourceImpl implements RatingResource {
    private static int dummyExecuted = 0;

    private final RatingFacade facade;

    public RatingResourceImpl(RatingFacade facade) {
        this.facade = facade;
    }

    @Override
    @UnitOfWork
    public List<Rating> getAllRatings(@ApiParam(value = "id of the desired user", required = true) long userId) {
        return this.facade.getAllRatings(userId);
    }

    @Override
    @UnitOfWork
    public Rating get(@ApiParam(value = "id of the desired user", required = true) long userId, @ApiParam(value = "id of the rating", required = true) long id) {
        Rating rating = facade.getRatingByID(userId, id);

        if(rating == null)
            throw new WebApplicationException(404);

        return rating;
    }

    @Override
    @UnitOfWork
    public Rating createRating(@ApiParam(value = "id of the desired user", required = true) long userId, @ApiParam(value = "Rating to create", required = true) Rating newRating) {
        return facade.createNewRating(userId, newRating);
    }

    @Override
    @UnitOfWork
    public Rating updateRating(@ApiParam(value = "id of the desired user", required = true) long userId, @ApiParam(value = "id of the rating", required = true) long id, @ApiParam(value = "new details of the specified rating", required = true) Rating rating) {
        return facade.updateRating(userId, id, rating);
    }

    @Override
    @UnitOfWork
    public void deleteRating(@ApiParam(value = "id of the desired user", required = true) long userId, @ApiParam(value = "id of the to be deleted rating", required = true) long id) {
        facade.deleteRating(userId, id);
    }

    @Override
    @UnitOfWork
    public Rating avgRating(@ApiParam(value = "id of the desired user", required = true) long userId) {
        return facade.avgRating(userId);
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

}
