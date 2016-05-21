package de.fau.cs.mad.wanthavers.server.impl;



import de.fau.cs.mad.wanthavers.common.Media;
import de.fau.cs.mad.wanthavers.common.rest.api.MediaResource;
import de.fau.cs.mad.wanthavers.server.facade.MediaFacade;
import io.dropwizard.hibernate.UnitOfWork;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;


import javax.ws.rs.WebApplicationException;
import java.io.InputStream;

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
        Media ret =  facade.getById(id);

        if(ret == null){
            throw new WebApplicationException(404);
        }

        return ret;
    }

    @Override
    @UnitOfWork
    public Media createMedia(InputStream fileInputStream, FormDataContentDisposition contentDispositionHeader) {
        return this.facade.createNewDesire(fileInputStream, contentDispositionHeader);
    }




}
