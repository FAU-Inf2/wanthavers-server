package de.fau.cs.mad.wanthavers.server.impl;

import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.Rating;
import de.fau.cs.mad.wanthavers.common.User;
import de.fau.cs.mad.wanthavers.common.rest.api.RatingResource;
import de.fau.cs.mad.wanthavers.common.rest.api.UserResource;
import de.fau.cs.mad.wanthavers.server.dummy.Dummies;
import de.fau.cs.mad.wanthavers.server.facade.RatingFacade;
import de.fau.cs.mad.wanthavers.server.facade.UserFacade;
import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.ApiParam;

import javax.ws.rs.WebApplicationException;
import java.util.List;

public class UserResourceImpl implements UserResource {
    private static boolean dummyExecuted = false;

    private final UserFacade facade;
    private final RatingFacade ratingFacade;

    public UserResourceImpl(UserFacade facade, RatingFacade ratingFacade) {
        this.facade = facade;
        this.ratingFacade = ratingFacade;
    }

    @Override
    @UnitOfWork
    public List<User> get() {
        List<User> list = this.facade.getAllUsers();
        for(User u : list){
            Rating r = ratingFacade.avgRating(u.getID());
            u.setRating(r.getStars());
        }
        return list;
    }

    @Override
    @UnitOfWork
    public User get(@ApiParam(value = "id of the desired user", required = true) long id) {
        User ret =  facade.getUserByID(id);
        Rating r = ratingFacade.avgRating(id);
        ret.setRating(r.getStars());

        if(ret == null){
            throw new WebApplicationException(404);
        }

        return ret;
    }

    @Override
    @UnitOfWork
    public User createUser(@ApiParam(value = "User to create", required = true) User newUser, String password) {
        try {
            return facade.createNewUser(newUser, password);
        } catch (Exception e) {
            throw new WebApplicationException(400);
        }
    }

    @Override
    @UnitOfWork
    public User updateUser(@ApiParam(value = "id of the user", required = true) long id, @ApiParam(value = "new details of the specified user", required = true) User user) {
        return facade.updateUser(id, user);
    }

    @Override
    @UnitOfWork
    public void deleteUser(@ApiParam(value = "id of the to be deleted user", required = true) long id) {
        facade.deleteUser(id);
    }

    @Override
    @UnitOfWork
    public List<Desire> getDesires(@ApiParam(value = "id of the desired user", required = true) long id) {
        return facade.getDesires(id);
    }

    /**
     * TODO: remove after tests
     */
    @Override
    @UnitOfWork
    public void createDummies() {
        if(dummyExecuted) {
            return;
        }

        User[] users = Dummies.getUsers();

        for(User u : users){
            createUser(u, u.getPassword());
        }

        dummyExecuted = true;
    }


}
