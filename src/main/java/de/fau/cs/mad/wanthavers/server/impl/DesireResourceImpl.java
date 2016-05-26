package de.fau.cs.mad.wanthavers.server.impl;

import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.User;
import de.fau.cs.mad.wanthavers.common.rest.api.DesireResource;
import de.fau.cs.mad.wanthavers.server.dummy.Dummies;
import de.fau.cs.mad.wanthavers.server.facade.DesireFacade;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.ApiParam;

import javax.ws.rs.WebApplicationException;
import java.util.Date;
import java.util.List;

public class DesireResourceImpl implements DesireResource {
    private static boolean dummyExecuted = false;

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
    public Desire createDesire(@Auth User user, @ApiParam(value = "Desire to create", required = true) Desire newDesire) {
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
        if(dummyExecuted) {
            return;
        }

        Desire[] desires = Dummies.getDesires();

        for(Desire d : desires){
            facade.createNewDesire(d);
        }

        dummyExecuted = true;
    }



}
