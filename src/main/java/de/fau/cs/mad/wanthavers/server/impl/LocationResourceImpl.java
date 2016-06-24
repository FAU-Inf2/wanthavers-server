package de.fau.cs.mad.wanthavers.server.impl;


import de.fau.cs.mad.wanthavers.common.Location;
import de.fau.cs.mad.wanthavers.common.User;
import de.fau.cs.mad.wanthavers.common.rest.api.LocationResource;
import de.fau.cs.mad.wanthavers.server.facade.LocationFacade;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.ApiParam;

import javax.ws.rs.WebApplicationException;

public class LocationResourceImpl implements LocationResource {

    private final LocationFacade facade;

    public LocationResourceImpl(LocationFacade facade) {
        this.facade = facade;
    }

    @Override
    public Location reverse(User user, double lat, double lon) {
        return this.facade.getReverseGeo(lat, lon);
    }

    @Override
    @UnitOfWork
    public Location createLocation(@Auth User user, @ApiParam(value = "Location to create", required = true) Location location) {
        location.setUserId(user.getId());
        return this.facade.createLocation(location);
    }

    @Override
    @UnitOfWork
    public Location get(@Auth User user, long id) {
        Location location = facade.getLocation(id);
        if (location == null) {
            throw new WebApplicationException(404);
        }

        if (location.getUserId() != user.getId()) {
            throw new WebApplicationException(401);
        }

        return location;
    }

    @Override
    @UnitOfWork
    public Location updateLocation(@Auth User user, @ApiParam(value = "id of the location", required = true) long id, @ApiParam(value = "Location to create", required = true) Location location) {
        location.setUserId(user.getId());
        return this.facade.updateLocation(id, location);
    }

    @Override
    @UnitOfWork
    public void deleteLocation(@Auth User user, @ApiParam(value = "id of the location", required = true) long id) {
        this.facade.deleteLocation(id);
    }


}
