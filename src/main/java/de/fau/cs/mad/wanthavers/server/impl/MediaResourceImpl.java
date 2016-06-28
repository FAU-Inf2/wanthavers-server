package de.fau.cs.mad.wanthavers.server.impl;


import de.fau.cs.mad.wanthavers.common.Media;
import de.fau.cs.mad.wanthavers.common.User;
import de.fau.cs.mad.wanthavers.common.rest.api.MediaResource;
import de.fau.cs.mad.wanthavers.server.facade.MediaFacade;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.WebApplicationException;
import java.util.List;

public class MediaResourceImpl implements MediaResource {
    private final MediaFacade facade;

    public MediaResourceImpl(MediaFacade facade) {
        this.facade = facade;
    }

    @Override
    @UnitOfWork
    public List<Media> getAll() {
        return this.facade.getAll();
    }

    @Override
    @UnitOfWork
    public Media get(long id) {
        Media ret = facade.getById(id);

        if (ret == null) {
            throw new WebApplicationException(404);
        }

        return ret;
    }

    @Override
    @UnitOfWork
    public Media createMedia(@Auth User user, String base64, String filename) {
        return this.facade.createNewMedia(user, base64, filename);
    }
}
