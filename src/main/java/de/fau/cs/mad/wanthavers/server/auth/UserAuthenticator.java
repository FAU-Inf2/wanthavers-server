package de.fau.cs.mad.wanthavers.server.auth;

import com.google.common.base.Optional;
import de.fau.cs.mad.wanthavers.common.User;
import de.fau.cs.mad.wanthavers.server.facade.UserFacade;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

public class UserAuthenticator implements Authenticator<BasicCredentials, User> {

    private UserFacade userFacade;

    public UserAuthenticator(UserFacade userFacade){
        this.userFacade = userFacade;
    }

    @Override
    public Optional<User> authenticate(BasicCredentials credentials)
            throws AuthenticationException {
        User user = userFacade.getUserByEmail(credentials.getUsername().toLowerCase());
        if(user != null){
            try{
                if(HashHelper.check(credentials.getPassword(), user.getPassword())){
                    return Optional.of(user);
                }
            }catch(Exception e){
                System.err.println(e.getMessage());
            }
        }

        return Optional.absent();
    }
}