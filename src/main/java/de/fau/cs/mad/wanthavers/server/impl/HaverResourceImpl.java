package de.fau.cs.mad.wanthavers.server.impl;

import de.fau.cs.mad.wanthavers.common.*;
import de.fau.cs.mad.wanthavers.common.rest.api.HaverResource;
import de.fau.cs.mad.wanthavers.server.facade.DesireFacade;
import de.fau.cs.mad.wanthavers.server.facade.HaverFacade;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.ApiParam;

import javax.ws.rs.WebApplicationException;
import java.util.List;


public class HaverResourceImpl implements HaverResource {
    private final HaverFacade facade;
    private final DesireFacade desireFacade;

    public HaverResourceImpl(HaverFacade facade, DesireFacade desireFacade) {
        this.facade = facade;
        this.desireFacade = desireFacade;
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
        Haver h = facade.updateHaver(desireId, id, haver);

        switch (h.getStatus()) {
            case HaverStatus.ACCEPTED:
                //TODO: force that there can only be one accepted user
                Desire d = desireFacade.getDesireByID(desireId);
                d.setStatus(DesireStatus.STATUS_IN_PROGRESS);
                desireFacade.updateDesire(desireId, d);
                break;
            case HaverStatus.ADDED:
                break;
            case HaverStatus.DELETED:
                break;
            case HaverStatus.REJECTED:
                break;
        }
        return h;
    }

    @Override
    @UnitOfWork
    public void deleteHaver(@ApiParam(value = "id of the desired desire", required = true) long desireId, @ApiParam(value = "id of the to be deleted haver", required = true) long id) {
        facade.deleteHaver(desireId, id);
    }
}
