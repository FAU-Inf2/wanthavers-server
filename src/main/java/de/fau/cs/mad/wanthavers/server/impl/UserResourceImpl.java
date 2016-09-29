package de.fau.cs.mad.wanthavers.server.impl;

import de.fau.cs.mad.wanthavers.common.*;
import de.fau.cs.mad.wanthavers.common.rest.api.UserResource;
import de.fau.cs.mad.wanthavers.server.SingletonManager;
import de.fau.cs.mad.wanthavers.server.auth.HashHelper;
import de.fau.cs.mad.wanthavers.server.facade.MediaFacade;
import de.fau.cs.mad.wanthavers.server.facade.RatingFacade;
import de.fau.cs.mad.wanthavers.server.facade.UserFacade;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.ApiParam;

import javax.ws.rs.WebApplicationException;
import java.util.List;

public class UserResourceImpl implements UserResource {
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
            Rating r = ratingFacade.avgRating(u.getId());
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

        //Rating r = ratingFacade.avgRating(id);
        //ret.setRating(r.getStars());

        return ret;
    }

    @Override
    @UnitOfWork
    public User createUser(@ApiParam(value = "User to create", required = true) User newUser, String password) {
        if (facade.getUserByEmail(newUser.getEmail().toLowerCase()) != null) {
            throw new WebApplicationException(409);
        }

        if(newUser.getImage() == null) {
            MediaFacade mediaFacade = (MediaFacade) SingletonManager.get(MediaFacade.class);
            Media def = mediaFacade.createNewMedia(User.DefaultUserImage.get());
            newUser.setImage(def);
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
        if (!user.getEmail().equals(newUser.getEmail().toLowerCase())) {
            User tmp = facade.getUserByEmail(newUser.getEmail().toLowerCase());
            if (tmp != null && tmp.getId() != user.getId()) {
                throw new WebApplicationException(409);
            }
        }
        return facade.updateUser(user.getId(), newUser);
    }

    @Override
    @UnitOfWork
    public void deleteUser(@Auth User user) {
        user.setStatus(UserStatus.INACTIVE);
        facade.updateUser(user.getId(), user);
    }

    @Override
    @UnitOfWork
    public List<Location> getSavedLocations(@Auth User user) {
        return this.facade.getSavedLocations(user.getId());
    }

    @Override
    @UnitOfWork
    public void resetPassword(String token, String password) {
        if (!this.facade.resetPassword(token, password)) {
            throw new WebApplicationException(400);
        }
    }

    @Override
    @UnitOfWork
    public void sendToken(String email) {
        if (!this.facade.sendResetToken(email)) {
            throw new WebApplicationException(400);
        }
    }

    @Override
    @UnitOfWork
    public UserFlag flagUser(@Auth User user, long userId) {
        return this.facade.flagUser(user.getId(), userId);
    }

}
