package de.fau.cs.mad.wanthavers.server;

import de.fau.cs.mad.wanthavers.common.User;
import de.fau.cs.mad.wanthavers.common.rest.api.UserResource;
import io.swagger.annotations.ApiParam;

import javax.ws.rs.PathParam;

/**
 * Created by Hoschi on 02.05.2016.
 */
public class UserResourceImpl implements UserResource {
    @Override
    public User get() {
        return new User("John Doe", "jojo@ojoj.com");
    }
}
