package de.fau.cs.mad.wanthavers.server.tasks;

import com.amazonaws.services.opsworks.model.App;
import com.google.common.collect.ImmutableMultimap;
import de.fau.cs.mad.wanthavers.common.*;
import de.fau.cs.mad.wanthavers.server.SingletonManager;
import de.fau.cs.mad.wanthavers.server.dao.AppVersionDAO;
import de.fau.cs.mad.wanthavers.server.dao.MediaDAO;
import de.fau.cs.mad.wanthavers.server.facade.*;
import de.fau.cs.mad.wanthavers.server.impl.UserResourceImpl;
import de.fau.cs.mad.wanthavers.server.misc.SessionContextTask;
import org.hibernate.SessionFactory;

import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class AppVersionsTask extends SessionContextTask {

    public AppVersionsTask(String taskName, SessionFactory sessionFactory) {
        super(taskName, sessionFactory);
    }

    @Override
    public void run(ImmutableMultimap<String, String> parameters, PrintWriter output) throws Exception {
        /*
            ANDROID
         */
        registerAppVersion("wanthavers.mad.cs.fau.de.wanthavers_android", 1, AppVersion.TYPE.FEATURE, AppVersion.OS.ANDROID, "http://mad.cs.fau.de/apps/wanthavers-android");
        registerAppVersion("wanthavers.mad.cs.fau.de.wanthavers_android", 2, AppVersion.TYPE.BREAKING_CHANGE, AppVersion.OS.ANDROID, "https://play.google.com/store/apps/details?id=wanthavers.mad.cs.fau.de.wanthavers_android");

        /*
            IOS
         */
        registerAppVersion("de.fau.cs.mad.wanthaver.ios", 1, AppVersion.TYPE.FEATURE, AppVersion.OS.IOS, "");
    }

    private void registerAppVersion(String appId, int versionCode, int type, int os, String link){
        AppVersion appVersion = new AppVersion();
        appVersion.setStoreLink(link);
        appVersion.setVersionCode(versionCode);
        appVersion.setType(type);
        appVersion.setOS(os);
        appVersion.setAppId(appId);
        AppVersionDAO dao = (AppVersionDAO)SingletonManager.get(AppVersionDAO.class);
        dao.create(appVersion);
    }

}
