package de.fau.cs.mad.wanthavers.server.impl;

import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.User;
import de.fau.cs.mad.wanthavers.common.rest.api.DesireResource;
import de.fau.cs.mad.wanthavers.server.facade.DesireFacade;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.ApiParam;

import javax.ws.rs.WebApplicationException;

public class DesireResourceImpl implements DesireResource {
    private final DesireFacade facade;

    public DesireResourceImpl(DesireFacade facade) {
        this.facade = facade;
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
        System.out.println("USER: "+user);

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
     * DEBUG REMOVE
     */

    @Override
    @UnitOfWork
    public void createDummies() {


        User[] list = new User[]{
                new User("Yoda", "com.mail@yoda"),
                new User("Jon Doe", "jon@doe.com"),
                new User("Max Muster", "m.muster@xyz.de")
        };

        for(User u : list){
           // createUser(u);
        }

    }



}
