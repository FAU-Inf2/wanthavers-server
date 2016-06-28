package de.fau.cs.mad.wanthavers.server.impl;


import de.fau.cs.mad.wanthavers.common.AppVersion;
import de.fau.cs.mad.wanthavers.common.rest.api.AppVersionResource;
import de.fau.cs.mad.wanthavers.server.facade.AppVersionFacade;
import io.dropwizard.hibernate.UnitOfWork;

public class AppVersionResourceImpl implements AppVersionResource {
    private final AppVersionFacade facade;

    public AppVersionResourceImpl(AppVersionFacade appVersionFacade) {
        this.facade = appVersionFacade;
    }

    @Override
    @UnitOfWork
    public AppVersion get(int versionCode, int os, String id) {
        return facade.getVersion(versionCode, os, id);
    }
}
