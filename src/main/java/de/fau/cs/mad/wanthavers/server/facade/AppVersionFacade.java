package de.fau.cs.mad.wanthavers.server.facade;


import de.fau.cs.mad.wanthavers.common.AppVersion;
import de.fau.cs.mad.wanthavers.server.dao.AppVersionDAO;

public class AppVersionFacade {
    private final AppVersionDAO dao;

    public AppVersionFacade(AppVersionDAO dao) {
        this.dao = dao;
    }

    public AppVersion getVersion(int langCode, int os, String id){
        return dao.getVersion(langCode, os, id);
    }
}
