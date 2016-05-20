package de.fau.cs.mad.wanthavers.server.impl;

import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.Haver;
import de.fau.cs.mad.wanthavers.common.User;
import de.fau.cs.mad.wanthavers.common.rest.api.HaverResource;
import de.fau.cs.mad.wanthavers.server.facade.HaverFacade;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.ApiParam;

import javax.ws.rs.WebApplicationException;
import java.util.Date;
import java.util.List;


public class HaverResourceImpl implements HaverResource {
    private static boolean dummyExecuted = false;

    private final HaverFacade facade;

    public HaverResourceImpl(HaverFacade facade) {
        this.facade = facade;
    }

    @Override
    @UnitOfWork
    public List<Haver> getAllHavers(@ApiParam(value = "id of the desired desire", required = true) long desireId) {
        return this.facade.getAllHavers(desireId);
    }

    @Override
    @UnitOfWork
    public Haver get(@ApiParam(value = "id of the desired desire", required = true) long desireId, @ApiParam(value = "id of the haver", required = true) long id) {
        Haver haver = facade.getHaverByID(desireId, id);

        if (haver == null)
            throw new WebApplicationException(404);

        return haver;
    }

    @Override
    @UnitOfWork
    public Haver createHaver(@ApiParam(value = "id of the desired desire", required = true) long desireId, @ApiParam(value = "Haver to create", required = true) Haver newHaver, @Auth User user) {
        newHaver.setUser(user);
        return facade.createNewHaver(desireId, newHaver);
    }

    @Override
    @UnitOfWork
    public Haver updateHaver(@ApiParam(value = "id of the desired desire", required = true) long desireId, @ApiParam(value = "id of the haver", required = true) long id, @ApiParam(value = "new details of the specified haver", required = true) Haver haver) {
        return facade.updateHaver(desireId, id, haver);
    }

    @Override
    @UnitOfWork
    public void deleteHaver(@ApiParam(value = "id of the desired desire", required = true) long desireId, @ApiParam(value = "id of the to be deleted haver", required = true) long id) {
        facade.deleteHaver(desireId, id);
    }

    @Override
    @UnitOfWork
    public void createDummies() {
        if(dummyExecuted) {
            return;
        }

        User[] users = new User[]{
                new User("Yoda", "com.mail@yoda"),
                new User("Jon Doe", "jon@doe.com"),
                new User("Max Muster", "m.muster@xyz.de")
        };

        for (int i = 0; i < users.length; i++)
            users[i].setId((long) (i + 1));

        Desire[] desires = new Desire[]{
                new Desire("Bier", "Kiste Bier", users[0], 15., 5., new Date(System.currentTimeMillis()), "Todesstern", 49.589674, 11.011961, 0),
                new Desire("Bagger", "Loch baggern", users[2], 155., 35., new Date(System.currentTimeMillis()), "TechFak", 49.589674, 11.011961, 1),
                new Desire("Pizza", "Pizza Fungi", users[2], 8.5, 3., new Date(System.currentTimeMillis()), "Wohnheim", 49.452030, 11.076750, 2),
                new Desire("Jeans", "Graue Jeans", users[1], 79., 8., new Date(System.currentTimeMillis()), "Schlosspark", 49.452030, 11.076750, 3),
                new Desire("Doener", "Doener vegetarisch", users[0], 4., 2.5, new Date(System.currentTimeMillis()), "Arcaden", 52.520007, 13.404954, 1),
                new Desire("Mineralwasser", "Kasten stilles Mineralwasser", users[1], 6.99, 3., new Date(System.currentTimeMillis()), "Daheim", 52.520007, 13.404954, 0),
                new Desire("Fass Bier", "5l-Fass Bier", users[0], 9.95, 5., new Date(System.currentTimeMillis()), "Star Wars", 52.520007, 13.404954, 2),
                new Desire("Router", "WLAN-Router mit Setup", users[1], 49., 6., new Date(System.currentTimeMillis()), "Schule", 49.452030, 11.076750, 1)
        };

        for (int i = 0; i < desires.length; i++)
            desires[i].setId((long) i + users.length + 1);

        Haver[] havers = {
                new Haver(users[1], new Date(System.currentTimeMillis()), desires[0].getID()),
                new Haver(users[2], new Date(System.currentTimeMillis()), desires[0].getID()),
                new Haver(users[0], new Date(System.currentTimeMillis()), desires[1].getID()),
                new Haver(users[1], new Date(System.currentTimeMillis()), desires[1].getID()),
                new Haver(users[0], new Date(System.currentTimeMillis()), desires[2].getID()),
                new Haver(users[1], new Date(System.currentTimeMillis()), desires[2].getID()),
                new Haver(users[0], new Date(System.currentTimeMillis()), desires[3].getID()),
                new Haver(users[2], new Date(System.currentTimeMillis()), desires[3].getID()),
                new Haver(users[1], new Date(System.currentTimeMillis()), desires[4].getID()),
                new Haver(users[2], new Date(System.currentTimeMillis()), desires[4].getID()),
                new Haver(users[0], new Date(System.currentTimeMillis()), desires[5].getID()),
                new Haver(users[2], new Date(System.currentTimeMillis()), desires[5].getID()),
                new Haver(users[1], new Date(System.currentTimeMillis()), desires[6].getID()),
                new Haver(users[2], new Date(System.currentTimeMillis()), desires[6].getID()),
                new Haver(users[0], new Date(System.currentTimeMillis()), desires[7].getID()),
                new Haver(users[2], new Date(System.currentTimeMillis()), desires[7].getID())

        };

        for(Haver h : havers)
            createHaver(h.getDesireId(), h, h.getUser());

        dummyExecuted = true;
    }

}
