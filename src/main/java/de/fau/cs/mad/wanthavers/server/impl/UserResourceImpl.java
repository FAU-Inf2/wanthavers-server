package de.fau.cs.mad.wanthavers.server.impl;

import de.fau.cs.mad.wanthavers.common.User;
import de.fau.cs.mad.wanthavers.common.rest.api.UserResource;
import de.fau.cs.mad.wanthavers.server.facade.UserFacade;
import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.ApiParam;

import javax.ws.rs.WebApplicationException;
import java.util.List;

public class UserResourceImpl implements UserResource {
    private final UserFacade facade;

    public UserResourceImpl(UserFacade facade) {
        this.facade = facade;
    }

    @Override
    @UnitOfWork
    public List<User> get() {
        return this.facade.getAllUsers();
    }

    @Override
    @UnitOfWork
    public User get(@ApiParam(value = "id of the desired user", required = true) long id) {
        User ret =  facade.getUserByID(id);

        if(ret == null){
            throw new WebApplicationException(404);
        }

        return ret;
    }

    @Override
    @UnitOfWork
    public User createUser(@ApiParam(value = "User to create", required = true) User newUser) {
        return facade.createNewUser(newUser);
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

    /**
     * TODO: remove after tests
     */
    @Override
    @UnitOfWork
    public void createDummies() {
        User[] users = new User[]{
                new User("Yoda", "com.mail@yoda"),
                new User("Jon Doe", "jon@doe.com"),
                new User("Max Muster", "m.muster@xyz.de")
        };

        for(User u : users){
            createUser(u);
        }
    }


}