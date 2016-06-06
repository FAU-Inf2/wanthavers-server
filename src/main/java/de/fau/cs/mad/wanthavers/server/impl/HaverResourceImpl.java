package de.fau.cs.mad.wanthavers.server.impl;

import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.Haver;
import de.fau.cs.mad.wanthavers.common.User;
import de.fau.cs.mad.wanthavers.common.rest.api.HaverResource;
import de.fau.cs.mad.wanthavers.server.dummy.Dummies;
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
    public Haver createHaver(@Auth User user, @ApiParam(value = "id of the desired desire", required = true) long desireId, @ApiParam(value = "Haver to create", required = true) Haver newHaver) {
        newHaver.setUser(user);
        return facade.createNewHaver(desireId, newHaver);
    }

    @Override
    @UnitOfWork
    public Haver getAccepted(@ApiParam(value = "id of the desired desire", required = true) long desireId) {
        return this.facade.getAccepted(desireId);
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

        Haver[] havers = Dummies.getHavers();

        for(Haver h : havers)
            createHaver(h.getUser(), h.getDesireId(), h);

        dummyExecuted = true;
    }

}
