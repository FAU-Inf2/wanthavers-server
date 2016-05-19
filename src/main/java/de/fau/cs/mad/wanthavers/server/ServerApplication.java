package de.fau.cs.mad.wanthavers.server;

import com.wordnik.swagger.jaxrs.listing.ApiListingResource;
import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.Haver;
import de.fau.cs.mad.wanthavers.common.Rating;
import de.fau.cs.mad.wanthavers.common.User;
import de.fau.cs.mad.wanthavers.server.auth.UserAuthenticator;
import de.fau.cs.mad.wanthavers.server.dao.*;
import de.fau.cs.mad.wanthavers.server.facade.*;
import de.fau.cs.mad.wanthavers.server.impl.*;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

public class ServerApplication extends Application<ServerConfiguration> {
    private final HibernateBundle<ServerConfiguration> hibernate =
            new HibernateBundle<ServerConfiguration>(User.class, Desire.class, Rating.class, Haver.class) {
                @Override
                public DataSourceFactory getDataSourceFactory(ServerConfiguration configuration) {
                    DataSourceFactory fac = configuration.getDataSourceFactory();
                    return fac;
                }
            };

    @Override
    public void initialize(Bootstrap<ServerConfiguration> bootstrap) {
        bootstrap.addBundle(hibernate);

        /*
        bootstrap.addBundle(new SwaggerBundle<ServerConfiguration>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(ServerConfiguration configuration) {
                return configuration.swaggerBundleConfiguration;
            }
        });*/
    }

    @Override
    public String getName() {
        return "wanthavers-rest-server";
    }

    @Override
    public void run(ServerConfiguration configuration, Environment environment) throws Exception {
        /** create DAOs and facades **/


        final UserDAO userDAO = new UserDAO(hibernate.getSessionFactory());
        final UserFacade userFacade = new UserFacade(userDAO);

        final DesireDAO desireDAO = new DesireDAO(hibernate.getSessionFactory());
        final DesireFacade desireFacade = new DesireFacade(desireDAO);

        final RatingDAO ratingDAO = new RatingDAO(hibernate.getSessionFactory());
        final RatingFacade ratingFacade = new RatingFacade(ratingDAO);

        final HaverDAO haverDAO = new HaverDAO(hibernate.getSessionFactory());
        final HaverFacade haverFacade = new HaverFacade(haverDAO);

        final ChatDAO chatDAO = new ChatDAO();
        final ChatFacade chatFacade = new ChatFacade(chatDAO);

        /** create resources and register **/

        final UserResourceImpl userResource = new UserResourceImpl(userFacade);
        environment.jersey().register(userResource);

        environment.jersey().register(new AuthDynamicFeature(
                new BasicCredentialAuthFilter.Builder<User>()
                        .setAuthenticator(new UserAuthenticator(userFacade))
                        .setRealm("SUPER SECRET STUFF")
                        .buildAuthFilter()));
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));

        //UserAuthenticator authenticator = new UserAuthenticator(userFacade);
        //environment.jersey().register(AuthFactory.binder(new BasicAuthFactory<>(authenticator, "SECRET", User.class)));

        final DesireResourceImpl desireResource = new DesireResourceImpl(desireFacade);
        environment.jersey().register(desireResource);

        final RatingResourceImpl ratingResource = new RatingResourceImpl(ratingFacade);
        environment.jersey().register(ratingResource);

        final HaverResourceImpl haverResource = new HaverResourceImpl(haverFacade);
        environment.jersey().register(haverResource);

        final ChatResourceImpl chatResource = new ChatResourceImpl(chatFacade);
        environment.jersey().register(chatResource);

       //final ApiListingResource api = new ApiListingResource();
        //environment.jersey().register(api);
        //configureSwagger(environment);
    }

    public static void main(String[] args) {
        try {
            new ServerApplication().run(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void configureSwagger(Environment environment) {
/*        BeanConfig config = new BeanConfig();
        config.setTitle(getName());
        config.setVersion("0.0.1");
        config.setBasePath("/v1");
        String pkg = UserResource.class.getPackage().toString().split(" ")[1];
        config.setResourcePackage(pkg);
        config.setScan(true);
        config.setHost("localhost:8080");*/
    }

}
