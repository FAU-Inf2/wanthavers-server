package de.fau.cs.mad.wanthavers.server.impl;


import de.fau.cs.mad.wanthavers.common.CloudMessageToken;
import de.fau.cs.mad.wanthavers.common.User;
import de.fau.cs.mad.wanthavers.common.rest.api.CloudMessageTokenResource;
import de.fau.cs.mad.wanthavers.server.facade.CloudMessageTokenFacade;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.ApiParam;

import javax.ws.rs.WebApplicationException;
import java.util.List;

public class CloudMessageTokenResourceImpl implements CloudMessageTokenResource {

    private final CloudMessageTokenFacade facade;

    public CloudMessageTokenResourceImpl(CloudMessageTokenFacade facade) {
        this.facade = facade;
    }

    @Override
    @UnitOfWork
    public List<CloudMessageToken> getAllTokens(@Auth User user) {
        return facade.getAllTokens(user.getId());
    }

    @Override
    @UnitOfWork
    public CloudMessageToken createToken(@Auth User user, @ApiParam(value = "Token to create", required = true) CloudMessageToken newToken) {
        return facade.createNewToken(user.getId(), newToken);
    }

    @Override
    @UnitOfWork
    public CloudMessageToken get(@Auth User user, @ApiParam(value = "id of the token", required = true) long id) {
        CloudMessageToken token = facade.getTokenById(user.getId(), id);

        if(token == null)
            throw new WebApplicationException(404);

        return token;
    }

    @Override
    @UnitOfWork
    public CloudMessageToken updateToken(@Auth User user, @ApiParam(value = "id of the token", required = true) long id, @ApiParam(value = "new details of the specified token", required = true) CloudMessageToken token) {
        return facade.updateToken(user.getId(), id, token);
    }

    @Override
    @UnitOfWork
    public void deleteToken(@Auth User user, @ApiParam(value = "id of the to be deleted token", required = true) String token) {
        facade.deleteToken(user.getId(), token);
    }
}
