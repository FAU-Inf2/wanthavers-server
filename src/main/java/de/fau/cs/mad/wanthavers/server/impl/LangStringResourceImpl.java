package de.fau.cs.mad.wanthavers.server.impl;


import de.fau.cs.mad.wanthavers.common.LangString;
import de.fau.cs.mad.wanthavers.common.User;
import de.fau.cs.mad.wanthavers.common.rest.api.LangStringResource;
import de.fau.cs.mad.wanthavers.server.facade.LangStringFacade;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;

public class LangStringResourceImpl implements LangStringResource {

    private final LangStringFacade facade;

    public LangStringResourceImpl(LangStringFacade facade) {
        this.facade = facade;
    }

    @Override
    @UnitOfWork
    public LangString get(String key, String code) {
        return this.facade.getLangString(key, code);
    }

    @Override
    @UnitOfWork
    public void create(@Auth User user, String key, String code, String value) {
        this.facade.createLangString(key, value, code);
    }
}
