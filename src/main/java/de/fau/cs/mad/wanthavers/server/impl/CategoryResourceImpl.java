package de.fau.cs.mad.wanthavers.server.impl;


import de.fau.cs.mad.wanthavers.common.Category;
import de.fau.cs.mad.wanthavers.common.Media;
import de.fau.cs.mad.wanthavers.common.User;
import de.fau.cs.mad.wanthavers.common.rest.api.CategoryResource;
import de.fau.cs.mad.wanthavers.common.rest.api.MediaResource;
import de.fau.cs.mad.wanthavers.server.facade.CategoryFacade;
import de.fau.cs.mad.wanthavers.server.facade.MediaFacade;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.ApiParam;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import javax.ws.rs.WebApplicationException;
import java.io.InputStream;
import java.util.List;

public class CategoryResourceImpl implements CategoryResource {

    private final CategoryFacade facade;

    public CategoryResourceImpl(CategoryFacade facade) {
        this.facade = facade;
    }


    @Override
    @UnitOfWork
    public Category get(@ApiParam(value = "id of the desired Category", required = true) long id) {
        return this.facade.getById(id);
    }

    @Override
    @UnitOfWork
    public List<Category> getSub(@ApiParam(value = "id of the desired Category", required = true) long id) {
        return this.facade.getSubCategories(id);
    }

    @Override
    @UnitOfWork
    public Category create(@Auth User user, @ApiParam(value = "Category to create", required = true) Category newCategory) {
        return this.facade.create(newCategory);
    }

    @Override
    @UnitOfWork
    public void delete(@Auth User user, @ApiParam(value = "id of the desired Category", required = true) long id) {
        this.facade.delete(id);
    }


    @UnitOfWork
    public Category update(@Auth User user, @ApiParam(value = "id of the desired Category", required = true) long id, Category newCategory) {
        return this.facade.update(id, newCategory);
    }
}
