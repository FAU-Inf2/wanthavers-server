package de.fau.cs.mad.wanthavers.server.auth;

import de.fau.cs.mad.wanthavers.common.User;
import io.dropwizard.auth.Authorizer;

import java.util.Set;

/**
 * Created by Nico on 26.05.2016.
 */
public class UserAuthorizer implements Authorizer<User> {
    @Override
    public boolean authorize(User principal, String role) {
        if(principal.getRole() == null)
            return false;

        return principal.getRole().equals(role);
    }
}
