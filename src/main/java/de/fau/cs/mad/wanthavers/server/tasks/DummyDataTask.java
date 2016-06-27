package de.fau.cs.mad.wanthavers.server.tasks;

import com.google.common.collect.ImmutableMultimap;
import de.fau.cs.mad.wanthavers.common.*;
import de.fau.cs.mad.wanthavers.server.SingletonManager;
import de.fau.cs.mad.wanthavers.server.auth.HashHelper;
import de.fau.cs.mad.wanthavers.server.dao.MediaDAO;
import de.fau.cs.mad.wanthavers.server.facade.*;
import de.fau.cs.mad.wanthavers.server.impl.UserResourceImpl;
import de.fau.cs.mad.wanthavers.server.misc.SessionContextTask;
import org.hibernate.SessionFactory;

import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class DummyDataTask extends SessionContextTask {
    private static User users[];
    private static Desire desires[];

    private static HashMap<String, Category> categories;

    public DummyDataTask(String taskName, SessionFactory sessionFactory) {
        super(taskName, sessionFactory);
    }

    @Override
    public void run(ImmutableMultimap<String, String> parameters, PrintWriter output) throws Exception {
        users = getUsers();
        categories = getCategories();
        desires = getDesires();

        UserResourceImpl userImpl = (UserResourceImpl) SingletonManager.get(UserResourceImpl.class);

        for(int i = 0; i < users.length; i++) {
            User u = users[i];
            users[i] = userImpl.createUser(u, u.getPassword());
        }

        DesireFacade desireFacade = (DesireFacade) SingletonManager.get(DesireFacade.class);

        for (int i = 0; i < desires.length; i++) {
            Desire d = desires[i];
            desires[i] = desireFacade.createNewDesire(d);
        }

        Rating ratings[] = getRatings();

        RatingFacade ratingFacade = (RatingFacade) SingletonManager.get(RatingFacade.class);

        for (Rating r : ratings) {
            ratingFacade.createNewRating(r.getUserId(), r);
        }

        Haver havers[] = getHavers();

        HaverFacade haverFacade = (HaverFacade) SingletonManager.get(HaverFacade.class);

        for (Haver h : havers) {
            haverFacade.createNewHaver(h.getDesireId(), h);
        }

        DesireFlag desireFlags[] = getFlags();

        DesireFlagFacade desireFlagFacade = (DesireFlagFacade) SingletonManager.get(DesireFlagFacade.class);

        for (DesireFlag df : desireFlags) {
            desireFlagFacade.flagDesire(df.getDesireId(), df);
        }
    }

    public static User[] getUsers() throws Exception {
        String adminPw = System.getenv("ADMIN_PW") == null ? "test" : System.getenv("ADMIN_PW");
        String adminEmail = System.getenv("ADMIN_EMAIL") == null ? "wanthavers" : System.getenv("ADMIN_EMAIL");
        User admin = new User("Admin", adminEmail);
        admin.setPassword(adminPw);
        admin.setLangCode("en_EN");

        User yoda = new User("Yoda", "com.mail@yoda");
        yoda.setPassword(HashHelper.getSaltedHash("test"));
        yoda.setStatus(UserStatus.ACTIVE);
        yoda.setLangCode("de_DE");
        yoda.setImage(getMediaForURL("https://s3.eu-central-1.amazonaws.com/whimages/64a842da-46b3-4e85-9dd8-a5e15ad3e9e7.jpg"));

        User jon = new User("Jon Doe", "jon@doe.com");
        jon.setPassword("test");
        jon.setImage(getMediaForURL("https://s3.eu-central-1.amazonaws.com/whimages/0fe5a41a-1a49-4b26-b383-39b01643f948.jpg"));

        User max = new User("Max Muster", "m.muster@xyz.de");
        max.setPassword("test");
        max.setImage(getMediaForURL("https://s3.eu-central-1.amazonaws.com/whimages/0fe5a41a-1a49-4b26-b383-39b01643f948.jpg"));

        User tick = new User("Tick", "tick@bla.de");
        tick.setPassword("test");
        tick.setImage(getMediaForURL("https://s3.eu-central-1.amazonaws.com/whimages/0fe5a41a-1a49-4b26-b383-39b01643f948.jpg"));

        User trick = new User("Trick", "trick@blubb.de");
        trick.setPassword("test");
        trick.setImage(getMediaForURL("https://s3.eu-central-1.amazonaws.com/whimages/0fe5a41a-1a49-4b26-b383-39b01643f948.jpg"));

        User track = new User("Track", "track@foo.com");
        track.setPassword("test");
        track.setImage(getMediaForURL("https://s3.eu-central-1.amazonaws.com/whimages/0fe5a41a-1a49-4b26-b383-39b01643f948.jpg"));

        User donald = new User("Donald", "donald@duck.net");
        donald.setPassword("test");
        donald.setImage(getMediaForURL("https://s3.eu-central-1.amazonaws.com/whimages/0fe5a41a-1a49-4b26-b383-39b01643f948.jpg"));

        User dagobert = new User("Dagobert", "money@save.com");
        dagobert.setPassword("test");
        dagobert.setImage(getMediaForURL("https://s3.eu-central-1.amazonaws.com/whimages/0fe5a41a-1a49-4b26-b383-39b01643f948.jpg"));

        return new User[]{
                yoda,
                jon,
                max,
                tick,
                trick,
                track,
                donald,
                dagobert,
                admin
        };
    }

    public static Desire[] getDesires() {
        Desire kastenBier = new Desire("Kasten Bier", "Uns ist das Bier ausgegangen...", users[0], 25., 0., "EUR", new Date(System.currentTimeMillis() - 0 * 5 * 60 * 1000), "Cauerstraße 27", 49.589674, 11.011961, 0);
        kastenBier.setImage(getMediaForURL("https://s3.eu-central-1.amazonaws.com/whimages/805162ba-709c-482c-a44a-fffd3be4b963.png"));
        kastenBier.setCategoryId(categories.get(CreateCategoriesTask.DRINKS_KEY).getId());

        Desire doener = new Desire("Döner", "Döner vom Ali.", users[0], 6., 0., "EUR", new Date(System.currentTimeMillis() - 1 * 5 * 60 * 1000), "Martenstraße 37", 49.589674, 11.011961, 0);
        doener.setImage(getMediaForURL("https://s3.eu-central-1.amazonaws.com/whimages/9a4709eb-7fe5-44ea-b122-050505217db2.png"));
        doener.setCategoryId(categories.get(CreateCategoriesTask.FOOD_KEY).getId());

        Desire aldi = new Desire("Einkauf", "Brauche eine Packung Eier, zwei Packungen Milch und eine Packung Mehl.", users[0], 10., 0., "EUR", new Date(System.currentTimeMillis() - 2 * 5 * 60 * 1000), "Unter den Linden 127", 49.589674, 11.011961, 0);
        aldi.setImage(getMediaForURL("https://s3.eu-central-1.amazonaws.com/whimages/9a4709eb-7fe5-44ea-b122-050505217db2.png"));
        aldi.setCategoryId(categories.get(CreateCategoriesTask.FOOD_KEY).getId());

        Desire autowaschen = new Desire("Autowäsche", "Suche jemanden, der mir mein wunderschönes Auto wäscht.", users[0], 65., 0., "EUR", new Date(System.currentTimeMillis() - 3 * 5 * 60 * 1000), "Gebbertstraße 123", 49.589674, 11.011961, 0);
        autowaschen.setImage(getMediaForURL("https://s3.eu-central-1.amazonaws.com/whimages/66bb8934-a953-48da-8d90-15e40a3d1255.jpg"));
        autowaschen.setCategoryId(categories.get(CreateCategoriesTask.SERVICE_KEY).getId());

        Desire konzertticket = new Desire("Konzertticket", "Suche ein Ticket für das AC/DC-Konzert heute Abend in Nürnberg.", users[0], 90., 0., "EUR", new Date(System.currentTimeMillis() - 4 * 5 * 60 * 1000), "Bierweg 133", 49.589674, 11.011961, 0);
        konzertticket.setImage(getMediaForURL("https://s3.eu-central-1.amazonaws.com/whimages/tickets.png"));
        konzertticket.setCategoryId(categories.get(CreateCategoriesTask.TICKETS_KEY).getId());

        Desire abgeschlossen = new Desire("Kuchen", "Mamorkuchen vom Bäcker", users[0], 5., 0., "EUR", new Date(System.currentTimeMillis() - 20 * 5 * 60 * 1000), "Abgeschlossenstraße 321", 49.589674, 11.011961, 0);
        abgeschlossen.setImage(getMediaForURL("https://s3.eu-central-1.amazonaws.com/whimages/9a4709eb-7fe5-44ea-b122-050505217db2.png"));
        abgeschlossen.setCategoryId(categories.get(CreateCategoriesTask.FOOD_KEY).getId());

        return new Desire[]{
                abgeschlossen,
                konzertticket,
                autowaschen,
                aldi,
                doener,
                kastenBier
        };
    }

    public static Rating[] getRatings() {
        return new Rating[]{
                new Rating(1, new Date(System.currentTimeMillis()), 5, "Awesome", users[2], desires[5]),
        };
    }

    public static Haver[] getHavers() {
        return new Haver[]{
                new Haver(users[1], new Date(System.currentTimeMillis()), desires[1].getId()),
                new Haver(users[2], new Date(System.currentTimeMillis()), desires[1].getId()),
                new Haver(users[3], new Date(System.currentTimeMillis()), desires[1].getId()),
                new Haver(users[4], new Date(System.currentTimeMillis()), desires[1].getId()),
                new Haver(users[5], new Date(System.currentTimeMillis()), desires[1].getId()),
                new Haver(users[6], new Date(System.currentTimeMillis()), desires[1].getId()),
                new Haver(users[7], new Date(System.currentTimeMillis()), desires[1].getId()),
        };
    }

    public static DesireFlag[] getFlags() {
        return new DesireFlag[]{
                new DesireFlag(desires[0].getId(), FlagReason.IMPOSSIBLE, "Geht nicht"),
                new DesireFlag(desires[0].getId(), FlagReason.INAPPROPRIATE, "Ih"),
                new DesireFlag(desires[0].getId(), FlagReason.SPAM, "..."),
                new DesireFlag(desires[0].getId(), FlagReason.SPAM, "....")
        };
    }

    private static Media getMediaForURL(String url) {
        MediaDAO mediaDAO = (MediaDAO) SingletonManager.get(MediaDAO.class);
        Media m = new Media();
        m.setLowRes(url);
        m.setMediumRes(url);
        m.setFullRes(url);
        return mediaDAO.create(m);
    }

    private static HashMap<String, Category> getCategories() {
        HashMap<String, Category> ret = new HashMap<>();
        CategoryFacade categoryFacade = (CategoryFacade) SingletonManager.get(CategoryFacade.class);

        List<Category> categories = categoryFacade.getSubCategoriesDeep(0);
        for (Category c : categories) {
            ret.put(c.getLangStringKey(), c);
        }

        return ret;
    }
}
