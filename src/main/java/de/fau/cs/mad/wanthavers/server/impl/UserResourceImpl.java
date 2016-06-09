package de.fau.cs.mad.wanthavers.server.impl;

import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.Rating;
import de.fau.cs.mad.wanthavers.common.User;
import de.fau.cs.mad.wanthavers.common.UserStatus;
import de.fau.cs.mad.wanthavers.common.rest.api.RatingResource;
import de.fau.cs.mad.wanthavers.common.rest.api.UserResource;
import de.fau.cs.mad.wanthavers.server.auth.HashHelper;
import de.fau.cs.mad.wanthavers.server.dummy.Dummies;
import de.fau.cs.mad.wanthavers.server.facade.RatingFacade;
import de.fau.cs.mad.wanthavers.server.facade.UserFacade;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.ApiParam;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.FormParam;
import javax.ws.rs.PathParam;
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
        for (User u : list) {
            Rating r = ratingFacade.avgRating(u.getID());
            u.setRating(r.getStars());
        }
        return list;
    }

    @Override
    @UnitOfWork
    public User get(@ApiParam(value = "id of the desired user", required = true) long id) {
        User ret = facade.getUserByID(id);
        
        if (ret == null) {
            throw new WebApplicationException(404);
        }

        Rating r = ratingFacade.avgRating(id);
        ret.setRating(r.getStars());

        return ret;
    }

    @Override
    @UnitOfWork
    public User createUser(@ApiParam(value = "User to create", required = true) User newUser, String password) {
        if (facade.getUserByEmail(newUser.getEmail().toLowerCase()) != null) {
            throw new WebApplicationException(409);
        }

        try {
            newUser.setPassword(HashHelper.getSaltedHash(password));
            newUser.setEmail(newUser.getEmail().toLowerCase());
            newUser.setStatus(UserStatus.ACTIVE);
            return facade.createNewUser(newUser, password);
        } catch (Exception e) {
            throw new WebApplicationException(400);
        }
    }

    @Override
    @UnitOfWork
    public User updateUser(@Auth User user, @ApiParam(value = "new details of the specified user", required = true) User newUser) {
        if(!user.getEmail().equals(newUser.getEmail().toLowerCase())){
            User tmp = facade.getUserByEmail(newUser.getEmail().toLowerCase());
            if ( tmp != null && tmp.getID() != user.getID()) {
                throw new WebApplicationException(409);
            }
        }
        return facade.updateUser(user.getID(), newUser);
    }

    @Override
    @UnitOfWork
    public void deleteUser(@Auth User user) {
        user.setStatus(UserStatus.INACTIVE);
        facade.updateUser(user.getID(), user);
    }

    @Override
    @UnitOfWork
    public List<Desire> getDesires(@ApiParam(value = "id of the desired user", required = true) long id) {
        return facade.getDesires(id);
    }

    @Override
    @UnitOfWork
    public List<Desire> getDesiresAsHaver(@ApiParam(value = "id of the user", required = true) long id,  List<Integer> status) {
        return facade.getDesiresAsHaver(id, status);
    }

    @Override
    @UnitOfWork
    public void resetPassword(String token, String password) {
        if(!this.facade.resetPassword(token, password)){
            throw new WebApplicationException(400);
        }
    }

    @Override
    @UnitOfWork
    public void sendToken(String email) {
        this.facade.sendResetToken(email);
    }

    /**
     * TODO: remove after tests
     */
    @Override
    @UnitOfWork
    public void createDummies() {
        if (dummyExecuted) {
            return;
        }

        User[] users = Dummies.getUsers();

        for (User u : users) {
            createUser(u, "test");
        }

        dummyExecuted = true;
    }


}
