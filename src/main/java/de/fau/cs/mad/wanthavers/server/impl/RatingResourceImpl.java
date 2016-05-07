package de.fau.cs.mad.wanthavers.server.impl;

import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.Rating;
import de.fau.cs.mad.wanthavers.common.User;
import de.fau.cs.mad.wanthavers.common.rest.api.RatingResource;
import de.fau.cs.mad.wanthavers.server.facade.RatingFacade;
import de.fau.cs.mad.wanthavers.server.facade.UserFacade;
import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.ApiParam;

import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import java.util.Date;
import java.util.List;


public class RatingResourceImpl implements RatingResource {
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
        User[] users = new User[]{
                new User("Yoda", "com.mail@yoda"),
                new User("Jon Doe", "jon@doe.com"),
                new User("Max Muster", "m.muster@xyz.de")
        };

        for(int i = 0; i < users.length; i++)
            users[i].setId((long) (i+1));

        Desire[] desires = new Desire[]{
                new Desire("Bier", "Kiste Bier", users[0], 15., 5., new Date(System.currentTimeMillis()), "Todesstern", 0., 0.),
                new Desire("Bagger", "Loch baggern", users[2], 155., 35., new Date(System.currentTimeMillis()), "TechFak", 0., 0.),
                new Desire("Pizza", "Pizza Fungi", users[2], 8.5, 3., new Date(System.currentTimeMillis()), "Wohnheim", 0., 0.),
                new Desire("Jeans", "Graue Jeans", users[1], 79., 8., new Date(System.currentTimeMillis()), "Schlosspark", 0., 0.),
                new Desire("Doener", "Doener vegetarisch", users[0], 4., 2.5, new Date(System.currentTimeMillis()), "Arcaden", 0., 0.),
                new Desire("Mineralwasser", "Kasten stilles Mineralwasser", users[1], 6.99, 3., new Date(System.currentTimeMillis()), "Daheim", 0., 0.),
                new Desire("Fass Bier", "5l-Fass Bier", users[0], 9.95, 5., new Date(System.currentTimeMillis()), "Star Wars", 0., 0.),
                new Desire("Router", "WLAN-Router mit Setup", users[1], 49., 6., new Date(System.currentTimeMillis()), "Schule", 0., 0.)
        };

        for(int i = 0; i < desires.length; i++)
            desires[i].setId((long) i + users.length + 1);


        Rating[] ratings = new Rating[]{
                new Rating(userId, 5),
                new Rating(userId, new Date(System.currentTimeMillis()), 3, "Awesome", users[0], desires[0])
        };

        for(Rating r : ratings)
            createRating(userId, r);
    }

}
