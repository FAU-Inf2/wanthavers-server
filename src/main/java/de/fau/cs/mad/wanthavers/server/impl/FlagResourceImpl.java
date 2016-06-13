package de.fau.cs.mad.wanthavers.server.impl;


import de.fau.cs.mad.wanthavers.common.DesireFlag;
import de.fau.cs.mad.wanthavers.common.User;
import de.fau.cs.mad.wanthavers.common.rest.api.FlagResource;
import de.fau.cs.mad.wanthavers.server.dummy.Dummies;
import de.fau.cs.mad.wanthavers.server.facade.DesireFlagFacade;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.ApiParam;

import java.util.List;

public class FlagResourceImpl implements FlagResource {
    private static int dummyExecuted = 0;

    private final DesireFlagFacade desireFlagFacade;

    public FlagResourceImpl(DesireFlagFacade desireFlagFacade) {
        this.desireFlagFacade = desireFlagFacade;
    }

    @Override
    @UnitOfWork
    public List<DesireFlag> getDesireFlags(@Auth User user, @ApiParam(value = "id of the Desire", required = true) long id) {
        return desireFlagFacade.getDesireFlags(id);
    }

    @Override
    @UnitOfWork
    public DesireFlag flagDesire(@Auth User user, @ApiParam(value = "id of the Desire", required = true) long id, @ApiParam(value = "Insert new DesireFlag to Desire", required = true) DesireFlag desireFlag) {
        return desireFlagFacade.flagDesire(id, desireFlag);
    }

    @Override
    @UnitOfWork
    public void unflagDesire(@Auth User user, @ApiParam(value = "id of the Desire", required = true) long id, @ApiParam(value = "id of the Desire", required = true) long flagId) {
        desireFlagFacade.unflagDesire(id, flagId);
    }

    @Override
    @UnitOfWork
    public void createDummies(@ApiParam(value = "id of the desire", required = true) long id) {
        if(dummyExecuted > 1)
            return;

        User[] users = Dummies.getUsers();
        DesireFlag[] flags = Dummies.getFlags();

        for(DesireFlag f : flags)
            flagDesire(users[0], f.getDesireId(), f);

        dummyExecuted++;
    }

}
