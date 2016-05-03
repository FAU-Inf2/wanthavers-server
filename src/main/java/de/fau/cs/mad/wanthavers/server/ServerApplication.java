package de.fau.cs.mad.wanthavers.server;

import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.User;
import de.fau.cs.mad.wanthavers.server.auth.UserAuthenticator;
import de.fau.cs.mad.wanthavers.server.dao.DesireDAO;
import de.fau.cs.mad.wanthavers.server.dao.UserDAO;
import de.fau.cs.mad.wanthavers.server.facade.DesireFacade;
import de.fau.cs.mad.wanthavers.server.facade.UserFacade;
import de.fau.cs.mad.wanthavers.server.impl.DesireResourceImpl;
import de.fau.cs.mad.wanthavers.server.impl.UserResourceImpl;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthFactory;
import io.dropwizard.auth.basic.BasicAuthFactory;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class ServerApplication extends Application<ServerConfiguration> {
    private final HibernateBundle<ServerConfiguration> hibernate =
            new HibernateBundle<ServerConfiguration>(User.class, Desire.class) {
                @Override
                public DataSourceFactory getDataSourceFactory(ServerConfiguration configuration) {
                    DataSourceFactory fac = configuration.getDataSourceFactory();
                    return fac;
                }
            };

    @Override
    public void initialize(Bootstrap<ServerConfiguration> bootstrap) {
        bootstrap.addBundle(hibernate);
    }

    @Override
    public void run(ServerConfiguration configuration, Environment environment) throws Exception {
        /** create DAOs and facades **/

        final UserDAO userDAO = new UserDAO(hibernate.getSessionFactory());
        final UserFacade userFacade = new UserFacade(userDAO);

        final DesireDAO desireDAO = new DesireDAO(hibernate.getSessionFactory());
        final DesireFacade desireFacade = new DesireFacade(desireDAO);

        /** create resources and register **/

        final UserResourceImpl userResource = new UserResourceImpl(userFacade);
        environment.jersey().register(userResource);

        UserAuthenticator authenticator = new UserAuthenticator(userFacade);
        environment.jersey().register(AuthFactory.binder(new BasicAuthFactory<>(authenticator, "SECRET", User.class)));

        final DesireResourceImpl desireResource = new DesireResourceImpl(desireFacade);
        environment.jersey().register(desireResource);
    }

    public static void main(String[] args) {
        try {
            new ServerApplication().run(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
