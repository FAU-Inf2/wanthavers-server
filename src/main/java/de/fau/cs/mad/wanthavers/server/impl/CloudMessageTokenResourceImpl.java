package de.fau.cs.mad.wanthavers.server.impl;


import de.fau.cs.mad.wanthavers.common.CloudMessageToken;
import de.fau.cs.mad.wanthavers.common.rest.api.CloudMessageTokenResource;
import de.fau.cs.mad.wanthavers.server.facade.CloudMessageTokenFacade;
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
    public List<CloudMessageToken> getAllTokens(@ApiParam(value = "id of the desired user", required = true) long userId) {
        return facade.getAllTokens(userId);
    }

    @Override
    @UnitOfWork
    public CloudMessageToken createToken(@ApiParam(value = "id of the desired user", required = true) long userId, @ApiParam(value = "Token to create", required = true) CloudMessageToken newToken) {
        return facade.createNewToken(userId, newToken);
    }

    @Override
    @UnitOfWork
    public CloudMessageToken get(@ApiParam(value = "id of the desired user", required = true) long userId, @ApiParam(value = "id of the token", required = true) long id) {
        CloudMessageToken token = facade.getTokenById(userId, id);

        if(token == null)
            throw new WebApplicationException(404);

        return token;
    }

    @Override
    @UnitOfWork
    public CloudMessageToken updateToken(@ApiParam(value = "id of the desired user", required = true) long userId, @ApiParam(value = "id of the token", required = true) long id, @ApiParam(value = "new details of the specified token", required = true) CloudMessageToken token) {
        return facade.updateToken(userId, id, token);
    }

    @Override
    @UnitOfWork
    public void deleteToken(@ApiParam(value = "id of the desired user", required = true) long userId, @ApiParam(value = "id of the to be deleted token", required = true) long id) {
        facade.deleteToken(userId, id);
    }
}
