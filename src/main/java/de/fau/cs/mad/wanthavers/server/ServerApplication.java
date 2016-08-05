package de.fau.cs.mad.wanthavers.server;


import com.amazonaws.regions.Regions;
import de.fau.cs.mad.wanthavers.common.*;
import de.fau.cs.mad.wanthavers.common.rest.api.AppVersionResource;
import de.fau.cs.mad.wanthavers.common.rest.api.LoginResource;
import de.fau.cs.mad.wanthavers.server.auth.UserAuthenticator;
import de.fau.cs.mad.wanthavers.server.auth.UserAuthorizer;
import de.fau.cs.mad.wanthavers.server.dao.*;
import de.fau.cs.mad.wanthavers.server.facade.*;
import de.fau.cs.mad.wanthavers.server.impl.*;
import de.fau.cs.mad.wanthavers.server.misc.Mailer;
import de.fau.cs.mad.wanthavers.server.tasks.*;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

public class ServerApplication extends Application<ServerConfiguration> {
    public static final String SERVER_URL = "https://wanthaver.com";
    public static final String DEFAULT_LANGCODE = "en_EN";

    private final HibernateBundle<ServerConfiguration> hibernate =
            new HibernateBundle<ServerConfiguration>(User.class, Desire.class, Rating.class, Haver.class, Media.class, Category.class, Location.class, CloudMessageToken.class, DesireFlag.class, LangString.class, AppVersion.class) {
                @Override
                public DataSourceFactory getDataSourceFactory(ServerConfiguration configuration) {
                    DataSourceFactory fac = configuration.getDataSourceFactory();
                    return fac;
                }
            };

    @Override
    public void initialize(Bootstrap<ServerConfiguration> bootstrap) {
        bootstrap.addBundle(hibernate);

        bootstrap.addBundle(new AssetsBundle("/static", "/static", "index.html"));

        // Enable variable substitution with environment variables
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(false)
                )
        );
    }

    @Override
    public String getName() {
        return "wanthavers-rest-server";
    }

    @Override
    public void run(ServerConfiguration configuration, Environment environment) throws Exception {

        /** configure mail **/
        Mailer.configure("wanthavers@i2.cs.fau.de", Regions.US_WEST_2);
        //Mailer.enableFake("wanthavers@i2.cs.fau.de");

        /** create DAOs and facades **/

        final RatingDAO ratingDAO = new RatingDAO(hibernate.getSessionFactory());
        final RatingFacade ratingFacade = new RatingFacade(ratingDAO);
        SingletonManager.add(ratingDAO);
        SingletonManager.add(ratingFacade);

        final CloudMessageTokenDAO tokenDAO = new CloudMessageTokenDAO(hibernate.getSessionFactory());
        final CloudMessageTokenFacade tokenFacade = new CloudMessageTokenFacade(tokenDAO);
        SingletonManager.add(tokenDAO);
        SingletonManager.add(tokenFacade);

        final UserDAO userDAO = new UserDAO(hibernate.getSessionFactory());
        final UserFacade userFacade = new UserFacade(userDAO);
        SingletonManager.add(userDAO);
        SingletonManager.add(userFacade);

        final DesireDAO desireDAO = new DesireDAO(hibernate.getSessionFactory());
        final DesireFacade desireFacade = new DesireFacade(desireDAO);
        SingletonManager.add(desireDAO);
        SingletonManager.add(desireFacade);

        final HaverDAO haverDAO = new HaverDAO(hibernate.getSessionFactory());
        final HaverFacade haverFacade = new HaverFacade(haverDAO);
        SingletonManager.add(haverDAO);
        SingletonManager.add(haverFacade);

        final ChatDAO chatDAO = new ChatDAO();
        final ChatFacade chatFacade = new ChatFacade(chatDAO);
        SingletonManager.add(chatDAO);
        SingletonManager.add(chatFacade);

        final MediaDAO mediaDAO = new MediaDAO(hibernate.getSessionFactory());
        final MediaFacade mediaFacade = new MediaFacade(mediaDAO);
        SingletonManager.add(mediaDAO);
        SingletonManager.add(mediaFacade);

        final CategoryDAO categoryDAO = new CategoryDAO(hibernate.getSessionFactory());
        final CategoryFacade categoryFacade = new CategoryFacade(categoryDAO);
        SingletonManager.add(categoryDAO);
        SingletonManager.add(categoryFacade);

        final LocationDAO locationDAO = new LocationDAO(hibernate.getSessionFactory());
        final LocationFacade locationFacade = new LocationFacade(locationDAO);
        SingletonManager.add(locationDAO);
        SingletonManager.add(locationFacade);

        final DesireFlagDAO desireFlagDAO = new DesireFlagDAO(hibernate.getSessionFactory());
        final DesireFlagFacade desireFlagFacade = new DesireFlagFacade(desireFlagDAO, desireDAO);
        SingletonManager.add(desireFlagDAO);
        SingletonManager.add(desireFlagFacade);

        final LangStringDAO langStringDAO = new LangStringDAO(hibernate.getSessionFactory());
        final LangStringFacade langStringFacade = new LangStringFacade(langStringDAO);
        SingletonManager.add(langStringDAO);
        SingletonManager.add(langStringFacade);

        final AppVersionDAO appVersionDAO = new AppVersionDAO(hibernate.getSessionFactory());
        final AppVersionFacade appVersionFacade = new AppVersionFacade(appVersionDAO);
        SingletonManager.add(appVersionDAO);
        SingletonManager.add(appVersionFacade);

        /** create resources and register **/

        environment.jersey().register(MultiPartFeature.class);
        environment.jersey().register(CORSResponseFilter.class);

        final UserResourceImpl userResource = new UserResourceImpl(userFacade, ratingFacade);
        SingletonManager.add(userResource);
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

        final DesireResourceImpl desireResource = new DesireResourceImpl(desireFacade, categoryFacade, ratingFacade, chatFacade, userFacade);
        environment.jersey().register(desireResource);

        final RatingResourceImpl ratingResource = new RatingResourceImpl(ratingFacade, userFacade);
        environment.jersey().register(ratingResource);

        final HaverResourceImpl haverResource = new HaverResourceImpl(haverFacade, desireFacade);
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

        final FlagResourceImpl flagResource = new FlagResourceImpl(desireFlagFacade);
        environment.jersey().register(flagResource);

        final LangStringResourceImpl langStringResource = new LangStringResourceImpl(langStringFacade);
        environment.jersey().register(langStringResource);

        final LoginResource loginResource = new LoginResourceImpl();
        environment.jersey().register(loginResource);

        final AppVersionResource appVersionResource = new AppVersionResourceImpl(appVersionFacade);
        environment.jersey().register(appVersionResource);

        /** register or run tasks **/
        String isProduction = System.getenv("IS_PRODUCTION");

        CreateCategoriesTask createCategoriesTask = new CreateCategoriesTask("CreateCategoriesTask", hibernate.getSessionFactory());
        createCategoriesTask.executeNow();

        DummyDataTask dummyDataTask = new DummyDataTask("DummyDataTask", hibernate.getSessionFactory());
        if (isProduction != null && !isProduction.equals("true")) {
            dummyDataTask.executeNow();
        }

        CreateAdminUserTask createAdminUserTask = new CreateAdminUserTask("CreateAdminUserTask", hibernate.getSessionFactory());
        createAdminUserTask.executeNow();

        CreateStringsTask createStringsTask = new CreateStringsTask("CreateStringsTask", hibernate.getSessionFactory());
        createStringsTask.executeNow();

        AppVersionsTask appVersionsTask = new AppVersionsTask("AppVersionsTask", hibernate.getSessionFactory());
        appVersionsTask.executeNow();

        DesireExpirationCheckTask desireExpirationCheckTask = new DesireExpirationCheckTask("DesireExpirationCheckTask", hibernate.getSessionFactory());
        desireExpirationCheckTask.executeNow();
    }

    public static void main(String[] args) {
        try {
            new ServerApplication().run(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
