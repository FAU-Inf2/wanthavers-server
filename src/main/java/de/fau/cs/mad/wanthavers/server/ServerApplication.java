package de.fau.cs.mad.wanthavers.server;


import de.fau.cs.mad.wanthavers.common.*;
import de.fau.cs.mad.wanthavers.common.rest.api.LoginResource;
import de.fau.cs.mad.wanthavers.common.rest.api.UserResource;
import de.fau.cs.mad.wanthavers.server.auth.UserAuthenticator;
import de.fau.cs.mad.wanthavers.server.auth.UserAuthorizer;
import de.fau.cs.mad.wanthavers.server.dao.*;
import de.fau.cs.mad.wanthavers.server.facade.*;
import de.fau.cs.mad.wanthavers.server.impl.*;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import org.eclipse.jetty.server.Dispatcher;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.eclipse.jetty.util.resource.FileResource;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;

public class ServerApplication extends Application<ServerConfiguration> {
    private final HibernateBundle<ServerConfiguration> hibernate =
            new HibernateBundle<ServerConfiguration>(User.class, Desire.class, Rating.class, Haver.class, Media.class, Category.class, Location.class, CloudMessageToken.class) {
                @Override
                public DataSourceFactory getDataSourceFactory(ServerConfiguration configuration) {
                    DataSourceFactory fac = configuration.getDataSourceFactory();
                    return fac;
                }
            };

    @Override
    public void initialize(Bootstrap<ServerConfiguration> bootstrap) {
        bootstrap.addBundle(hibernate);

        bootstrap.addBundle(new SwaggerBundle<ServerConfiguration>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(ServerConfiguration configuration) {
                return configuration.swaggerBundleConfiguration;
            }
        });

        bootstrap.addBundle(new AssetsBundle("/static", "/static", "index.html"));

    }

    @Override
    public String getName() {
        return "wanthavers-rest-server";
    }

    @Override
    public void run(ServerConfiguration configuration, Environment environment) throws Exception {
        /** create DAOs and facades **/

        final RatingDAO ratingDAO = new RatingDAO(hibernate.getSessionFactory());
        final RatingFacade ratingFacade = new RatingFacade(ratingDAO);


        final UserDAO userDAO = new UserDAO(hibernate.getSessionFactory());
        final UserFacade userFacade = new UserFacade(userDAO);

        final DesireDAO desireDAO = new DesireDAO(hibernate.getSessionFactory());
        final DesireFacade desireFacade = new DesireFacade(desireDAO);

        final HaverDAO haverDAO = new HaverDAO(hibernate.getSessionFactory());
        final HaverFacade haverFacade = new HaverFacade(haverDAO);

        final ChatDAO chatDAO = new ChatDAO();
        final ChatFacade chatFacade = new ChatFacade(chatDAO);

        final MediaDAO mediaDAO = new MediaDAO(hibernate.getSessionFactory());
        final MediaFacade mediaFacade = new MediaFacade(mediaDAO);

        final CategoryDAO categoryDAO = new CategoryDAO(hibernate.getSessionFactory());
        final CategoryFacade categoryFacade = new CategoryFacade(categoryDAO);

        final LocationDAO locationDAO = new LocationDAO(hibernate.getSessionFactory());
        final LocationFacade locationFacade = new LocationFacade(locationDAO);

        final CloudMessageTokenDAO tokenDAO = new CloudMessageTokenDAO(hibernate.getSessionFactory());
        final CloudMessageTokenFacade tokenFacade = new CloudMessageTokenFacade(tokenDAO);

        /** create resources and register **/

        environment.jersey().register(MultiPartFeature.class);
        environment.jersey().register(CORSResponseFilter.class);

        final UserResourceImpl userResource = new UserResourceImpl(userFacade, ratingFacade);
        environment.jersey().register(userResource);

        environment.jersey().register(new AuthDynamicFeature(
                new BasicCredentialAuthFilter.Builder<User>()
                        .setAuthenticator(new UserAuthenticator(userFacade))
                        .setAuthorizer(new UserAuthorizer())
                        .setRealm("SUPER SECRET STUFF")
                        .buildAuthFilter()));

        environment.jersey().register(RolesAllowedDynamicFeature.class);
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));



        //UserAuthenticator authenticator = new UserAuthenticator(userFacade);
        //environment.jersey().register(AuthFactory.binder(new BasicAuthFactory<>(authenticator, "SECRET", User.class)));

        final DesireResourceImpl desireResource = new DesireResourceImpl(desireFacade);
        environment.jersey().register(desireResource);

        final RatingResourceImpl ratingResource = new RatingResourceImpl(ratingFacade);
        environment.jersey().register(ratingResource);

        final HaverResourceImpl haverResource = new HaverResourceImpl(haverFacade);
        environment.jersey().register(haverResource);

        final ChatResourceImpl chatResource = new ChatResourceImpl(chatFacade, userFacade, desireFacade);
        environment.jersey().register(chatResource);

        final MediaResourceImpl mediaResource = new MediaResourceImpl(mediaFacade);
        environment.jersey().register(mediaResource);

        final CategoryResourceImpl categoryResource = new CategoryResourceImpl(categoryFacade);
        environment.jersey().register(categoryResource);

        final LocationResourceImpl locationResource = new LocationResourceImpl(locationFacade);
        environment.jersey().register(locationResource);

        final CloudMessageTokenResourceImpl tokenResource = new CloudMessageTokenResourceImpl(tokenFacade);
        environment.jersey().register(tokenResource);
        
	final LoginResource loginResource = new LoginResourceImpl();
        environment.jersey().register(loginResource);

        final ApiListingResource api = new ApiListingResource();
        environment.jersey().register(api);
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
        BeanConfig config = new BeanConfig();
        config.setTitle(getName());
        config.setVersion("0.0.1");
        config.setBasePath("/v1");
        String pkg = UserResource.class.getPackage().toString().split(" ")[1];
        config.setResourcePackage(pkg);
        config.setScan(true);
    }

}
