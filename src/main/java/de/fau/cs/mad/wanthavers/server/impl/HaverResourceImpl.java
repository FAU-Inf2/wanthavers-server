package de.fau.cs.mad.wanthavers.server.impl;

import de.fau.cs.mad.wanthavers.common.*;
import de.fau.cs.mad.wanthavers.common.rest.api.HaverResource;
import de.fau.cs.mad.wanthavers.server.facade.DesireFacade;
import de.fau.cs.mad.wanthavers.server.facade.HaverFacade;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.ApiParam;

import javax.ws.rs.WebApplicationException;
import java.util.Date;
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
    public Haver get(@ApiParam(value = "id of the desired desire", required = true) long desireId, @ApiParam(value = "id of the user", required = true) long userId) {
        Haver haver = facade.getHaverByUserId(desireId, userId);

        if (haver == null)
            throw new WebApplicationException(404);

        return haver;
    }

    @Override
    @UnitOfWork
    public Haver createHaver(@Auth User user, @ApiParam(value = "id of the desired desire", required = true) long desireId, @ApiParam(value = "Haver to create", required = true) Haver newHaver) {
        if (facade.getHaverByUserId(desireId, user.getId()) != null) {
            throw new WebApplicationException(409);
        }

        newHaver.setUser(user);
        newHaver.setCreationDate(new Date(System.currentTimeMillis()));
        newHaver.setStatus(HaverStatus.ADDED);

        check(user, newHaver, desireId);

        return facade.createNewHaver(desireId, newHaver);
    }

    @Override
    @UnitOfWork
    public Haver getAccepted(@ApiParam(value = "id of the desired desire", required = true) long desireId) {
        return this.facade.getAccepted(desireId);
    }

    @Override
    @UnitOfWork
    public Haver updateHaver(@Auth User user, @ApiParam(value = "id of the desired desire", required = true) long desireId, @ApiParam(value = "id of the haver", required = true) long id, @ApiParam(value = "new details of the specified haver", required = true) Haver haver) {
        check(user, haver, desireId);

        if (haver.getStatus() == HaverStatus.ACCEPTED && acceptedHaverAlreadyExists(desireId)) {
            throw new WebApplicationException(409);
        }

        Haver h = facade.updateHaver(desireId, id, haver);

        switch (h.getStatus()) {
            case HaverStatus.ACCEPTED:
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
    public Haver updateHaverStatus(@Auth User user, @ApiParam(value = "id of the desire", required = true) long desireId, @ApiParam(value = "id of the haver relation", required = true) long userId, int status) {
        if (status == HaverStatus.ACCEPTED && acceptedHaverAlreadyExists(desireId)) {
            throw new WebApplicationException(409);
        }

        Haver stored = facade.getHaverByUserId(desireId, userId);

        if (stored == null)
            throw new WebApplicationException(404);

        check(user, stored, desireId);

        stored.setStatus(status);
        return facade.updateHaver(desireId, userId, stored);
    }

    private void check(User user, Haver haver, long desireId) throws WebApplicationException {
        if (user.getId() != haver.getUser().getId()) {
            throw new WebApplicationException(401);
        }

        if (haver.getDesireId() != desireId) {
            throw new WebApplicationException(400);
        }
    }

    private boolean acceptedHaverAlreadyExists(long desireId) {
        return facade.getAccepted(desireId) != null;
    }
}
