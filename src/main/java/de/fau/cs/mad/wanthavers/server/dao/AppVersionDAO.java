package de.fau.cs.mad.wanthavers.server.dao;


import de.fau.cs.mad.wanthavers.common.AppVersion;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

import java.util.List;

public class AppVersionDAO extends AbstractSuperDAO<AppVersion> {

    public AppVersionDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public AppVersion getVersion(int versionCode, int os, String id){
        Query query = super.currentSession().createQuery("SELECT a FROM AppVersion a WHERE os=:os AND versionCode > :versionCode AND appId = :appId ORDER BY versionCode ASC ");
        query.setParameter("os", os);
        query.setParameter("versionCode", versionCode);
        query.setParameter("appId", id);
        List<AppVersion> result = super.list(query);

        if(result == null || result.isEmpty()){
            AppVersion appVersion = new AppVersion();
            appVersion.setVersionCode(versionCode);
            appVersion.setAppId(id);
            return appVersion;
        }

        int type = AppVersion.TYPE.FEATURE;
        boolean force = false;
        String link = null;
        int code = 0;
        String appId = null;
        for(AppVersion av : result){
            link = av.getStoreLink();
            code = av.getVersionCode();
            appId = av.getAppId();

            if(av.getType() == AppVersion.TYPE.BREAKING_CHANGE){
                type = AppVersion.TYPE.BREAKING_CHANGE;
                force = true;
            }

            if(av.getType() == AppVersion.TYPE.NEW_APP){
                type = AppVersion.TYPE.NEW_APP;
                force = true;
                break;
            }
        }

        AppVersion appVersion = new AppVersion();
        appVersion.setForceUpdate(force);
        appVersion.setType(type);
        appVersion.setStoreLink(link);
        appVersion.setVersionCode(code);
        appVersion.setAppId(appId);
        return appVersion;
    }

}
