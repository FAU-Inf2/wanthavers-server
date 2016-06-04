package de.fau.cs.mad.wanthavers.server.impl;


import de.fau.cs.mad.wanthavers.common.Location;
import de.fau.cs.mad.wanthavers.common.Media;
import de.fau.cs.mad.wanthavers.common.User;
import de.fau.cs.mad.wanthavers.common.rest.api.LocationResource;
import de.fau.cs.mad.wanthavers.common.rest.api.MediaResource;
import de.fau.cs.mad.wanthavers.server.dummy.Dummies;
import de.fau.cs.mad.wanthavers.server.facade.LocationFacade;
import de.fau.cs.mad.wanthavers.server.facade.MediaFacade;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.FormParam;
import javax.ws.rs.WebApplicationException;
import java.util.List;

public class LocationResourceImpl implements LocationResource {

    private final LocationFacade facade;

    public LocationResourceImpl(LocationFacade facade) {
        this.facade = facade;
    }

    @Override
    public Location reverse(User user, double lat, double lon) {
        return this.facade.getReverseGeo(lat, lon);
    }
}
