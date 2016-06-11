package de.fau.cs.mad.wanthavers.server.facade;

import de.fau.cs.mad.wanthavers.common.Location;
import de.fau.cs.mad.wanthavers.server.dao.LocationDAO;

public class LocationFacade {
    private final LocationDAO dao;

    public LocationFacade(LocationDAO dao){
        this.dao = dao;
    }

    public Location getReverseGeo(double lat, double lon){
        return this.dao.getReverseGeo(lat, lon);
    }

    public Location createLocation(Location location){return this.dao.createLocation(location);}

    public Location updateLocation(long id, Location location){return this.dao.updateLocation(id, location);}
}
