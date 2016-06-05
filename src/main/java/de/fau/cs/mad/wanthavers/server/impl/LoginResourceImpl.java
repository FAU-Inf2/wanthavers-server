package de.fau.cs.mad.wanthavers.server.impl;

import de.fau.cs.mad.wanthavers.common.User;
import de.fau.cs.mad.wanthavers.common.rest.api.LoginResource;
import io.dropwizard.auth.Auth;

public class LoginResourceImpl implements LoginResource {

    @Override
    public User login(@Auth User user) {
        return user;
    }
}
