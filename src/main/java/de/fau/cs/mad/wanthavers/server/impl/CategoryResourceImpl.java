package de.fau.cs.mad.wanthavers.server.impl;


import de.fau.cs.mad.wanthavers.common.Category;
import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.User;
import de.fau.cs.mad.wanthavers.common.rest.api.CategoryResource;
import de.fau.cs.mad.wanthavers.server.facade.CategoryFacade;
import de.fau.cs.mad.wanthavers.server.misc.TranslationHelper;
import de.fau.cs.mad.wanthavers.server.sort.CategoryNameComperator;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.ApiParam;

import java.util.Collections;
import java.util.List;

public class CategoryResourceImpl implements CategoryResource {

    private final CategoryFacade facade;

    public CategoryResourceImpl(CategoryFacade facade) {
        this.facade = facade;
    }


    @Override
    @UnitOfWork
    public Category get(@Auth User user, @ApiParam(value = "id of the desired Category", required = true) long id) {
        return TranslationHelper.translate(this.facade.getById(id), user.getLangCode());
    }

    @Override
    @UnitOfWork
    public List<Category> getSub(@Auth User user, @ApiParam(value = "id of the desired Category", required = true) long id, boolean recursive) {

        List<Category> ret;

        if(recursive){
            ret = TranslationHelper.translate(this.facade.getSubCategoriesDeep(id), user.getLangCode());
        }else{
            ret = TranslationHelper.translate(this.facade.getSubCategoriesFlat(id), user.getLangCode());
        }

        Collections.sort(ret, new CategoryNameComperator());
        return ret;
    }

    @Override
    @UnitOfWork
    public List<Desire> getDesires(@ApiParam(value = "id of the desired Category", required = true) long id, boolean recursive) {
        if(recursive){
            return this.facade.getDesiresByCategoryDeep(id);
        }else{
            return this.facade.getDesiresByCategoryFlat(id);
        }
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
