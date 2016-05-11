package de.fau.cs.mad.wanthavers.server.impl;

import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.User;
import de.fau.cs.mad.wanthavers.common.rest.api.DesireResource;
import de.fau.cs.mad.wanthavers.server.facade.DesireFacade;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.ApiParam;

import javax.ws.rs.WebApplicationException;
import java.util.Date;
import java.util.List;

public class DesireResourceImpl implements DesireResource {
    private final DesireFacade facade;

    public DesireResourceImpl(DesireFacade facade) {
        this.facade = facade;
    }

    @Override
    @UnitOfWork
    public List<Desire> get() {
        return this.facade.getAllDesires();
    }

    @Override
    @UnitOfWork
    public List<Desire> getByLocation(double lat, double lon, double radius) {
        return this.facade.getAllDesiresByLocation(lat, lon, radius);
    }

    @Override
    @UnitOfWork
    public Desire get(@ApiParam(value = "id of the desired Desire", required = true) long id) {
        Desire ret =  facade.getDesireByID(id);

        if(ret == null){
            throw new WebApplicationException(404);
        }

        return ret;
    }

    @Override
    @UnitOfWork
    public Desire createDesire(@ApiParam(value = "Desire to create", required = true) Desire newDesire, @Auth User user) {
        //set desire creator
        newDesire.setCreator(user);

        return facade.createNewDesire(newDesire);
    }

    @Override
    @UnitOfWork
    public Desire updateDesire(@ApiParam(value = "id of the Desire", required = true) long id, @ApiParam(value = "new details of the specified Desire", required = true) Desire desire) {
        return facade.updateDesire(id, desire);
    }

    @Override
    @UnitOfWork
    public void deleteDesire(@ApiParam(value = "id of the to be deleted Desire", required = true) long id) {
        facade.deleteDesire(id);
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

        for(int i = 0; i < users.length; i++){
            users[i].setId((long) (i+1));
        }

        Desire[] desires = new Desire[]{
                new Desire("Bier", "Kiste Bier", users[0], 15., 5., new Date(System.currentTimeMillis()), "Todesstern", 49.589674, 11.011961),
                new Desire("Bagger", "Loch baggern", users[2], 155., 35., new Date(System.currentTimeMillis()), "TechFak", 49.589674, 11.011961),
                new Desire("Pizza", "Pizza Fungi", users[2], 8.5, 3., new Date(System.currentTimeMillis()), "Wohnheim", 49.452030, 11.076750),
                new Desire("Jeans", "Graue Jeans", users[1], 79., 8., new Date(System.currentTimeMillis()), "Schlosspark", 49.452030, 11.076750),
                new Desire("Doener", "Doener vegetarisch", users[0], 4., 2.5, new Date(System.currentTimeMillis()), "Arcaden", 52.520007, 13.404954),
                new Desire("Mineralwasser", "Kasten stilles Mineralwasser", users[1], 6.99, 3., new Date(System.currentTimeMillis()), "Daheim", 52.520007, 13.404954),
                new Desire("Fass Bier", "5l-Fass Bier", users[0], 9.95, 5., new Date(System.currentTimeMillis()), "Star Wars", 52.520007, 13.404954),
                new Desire("Router", "WLAN-Router mit Setup", users[1], 49., 6., new Date(System.currentTimeMillis()), "Schule", 49.452030, 11.076750)
        };

        for(Desire d : desires){
            facade.createNewDesire(d);
        }
    }



}
