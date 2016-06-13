package de.fau.cs.mad.wanthavers.server.dummy;

import de.fau.cs.mad.wanthavers.common.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Nico on 26.05.2016.
 */
public class Dummies {

    public static Media[] getMedia() {
        Media media1 = new Media();
        media1.setFullRes("https://s3.eu-central-1.amazonaws.com/whimages/e52e06c4-8173-4420-ab10-39e86db162bb.jpg");
        media1.setMediumRes("https://s3.eu-central-1.amazonaws.com/whimages/506eb96e-d5bc-455d-a1f2-cb9ee9b61dfb.jpg");
        media1.setLowRes("https://s3.eu-central-1.amazonaws.com/whimages/b627fa0c-230e-4821-abf7-12bb88494620.jpg");

        Media media2 = new Media();
        media2.setFullRes("https://s3.eu-central-1.amazonaws.com/whimages/bfa1d88a-4abf-408a-bf10-14d9ddba0c4b.jpg");
        media2.setMediumRes("https://s3.eu-central-1.amazonaws.com/whimages/bacf1866-aec8-430a-9038-66ecdde14577.jpg");
        media2.setLowRes("https://s3.eu-central-1.amazonaws.com/whimages/73b8bdd0-c878-4a4f-a796-c773a158165c.jpg");


        Media media3 = new Media();
        media3.setFullRes("https://s3.eu-central-1.amazonaws.com/whimages/8f62e699-f0a2-4e7f-99bb-24dcb36464a4.JPG");
        media3.setMediumRes("https://s3.eu-central-1.amazonaws.com/whimages/74a7e134-f979-4a4b-b399-122c437ab11b.JPG");
        media3.setLowRes("https://s3.eu-central-1.amazonaws.com/whimages/da9a0c4f-b282-4b8f-8175-08e511a433a8.JPG");

        Media media4 = new Media();
        media4.setFullRes("https://s3.eu-central-1.amazonaws.com/whimages/cc4472a0-f9d0-4b4c-b31f-06a776de5f11.jpg");
        media4.setMediumRes("https://s3.eu-central-1.amazonaws.com/whimages/a1fd047a-7058-486d-9ecb-a0d246e0925b.jpg");
        media4.setLowRes("https://s3.eu-central-1.amazonaws.com/whimages/90021f3e-2289-466e-84be-07ee375504ab.jpg");

        Media media5 = new Media();
        media5.setFullRes("https://s3.eu-central-1.amazonaws.com/whimages/f0035b5c-90de-4808-8c52-881f210e4f0d.jpg");
        media5.setMediumRes("https://s3.eu-central-1.amazonaws.com/whimages/ada58e0f-3ea0-48bf-9452-15a14f8110e3.jpg");
        media5.setLowRes("https://s3.eu-central-1.amazonaws.com/whimages/f2180670-8156-48e8-96c9-df66c9774758.jpg");

        return new Media[]{
                media1,
                media2,
                media3,
                media4,
                media5
        };
    }

    public static void setMediaIds(Media[] media) {
        for (int i = 0; i < media.length; i++) {
            media[i].setId((long) i + 1);
        }
    }

    public static User[] getUsers() {
        Media[] media = getMedia();

        setMediaIds(media);

        User yoda = new User("Yoda", "com.mail@yoda");
        yoda.setImage(media[0]);
        yoda.setPassword("test");

        yoda.setRole(UserRoles.USER_ROLE_ADMIN);

/*        Set<String> roles = new HashSet<>();
        roles.add(UserRoles.USER_ROLE_USER);
        roles.add(UserRoles.USER_ROLE_ADMIN);
        yoda.setRoles(roles);*/

        User jon = new User("Jon Doe", "jon@doe.com");
        jon.setImage(media[2]);
        jon.setPassword("test");

        User max = new User("Max Muster", "m.muster@xyz.de");
        max.setImage(media[1]);
        max.setPassword("test");

        User tick = new User("Tick", "tick@bla.de");
        tick.setPassword("test");

        User trick = new User("Trick", "trick@blubb.de");
        trick.setPassword("test");

        User track = new User("Track", "track@foo.com");
        track.setPassword("test");

        User donald = new User("Donald", "donald@duck.net");
        donald.setPassword("test");

        User dagobert = new User("Dagobert", "money@save.com");
        dagobert.setPassword("test");

        return new User[]{
                yoda,
                jon,
                max,
                tick,
                trick,
                track,
                donald,
                dagobert
        };
    }

    public static void setUserIds(User[] users, int lengthMedia) {
        for (int i = 0; i < users.length; i++) {
            users[i].setId((long) (i + lengthMedia + 1));
        }
    }

    public static Desire[] getDesires() {

        Media[] media = getMedia();

        setMediaIds(media);

        User[] users = getUsers();

        setUserIds(users, media.length);

        Desire kisteBier = new Desire("Bier", "Kiste Bier", users[0], 15., 5., "EUR", new Date(System.currentTimeMillis()), "Todesstern", 49.589674, 11.011961, 0);
        kisteBier.setImage(media[3]);

        return new Desire[]{
                kisteBier,
                new Desire("Bagger", "Loch baggern", users[2], 155., 35., "EUR", new Date(System.currentTimeMillis()-1*5*60*1000), "TechFak", 49.589674, 11.011961, 1),
                new Desire("Pizza", "Pizza Fungi", users[2], 8.5, 3., "EUR", new Date(System.currentTimeMillis()-2*5*60*1000), "Wohnheim", 49.452030, 11.076750, 2),
                new Desire("Jeans", "Graue Jeans", users[1], 79., 8., "EUR", new Date(System.currentTimeMillis()-3*5*60*1000), "Schlosspark", 49.452030, 11.076750, 3),
                new Desire("Doener", "Doener vegetarisch", users[0], 4., 2.5, "USD", new Date(System.currentTimeMillis()-4*5*60*1000), "Arcaden", 52.520007, 13.404954, 1),
                new Desire("Mineralwasser", "Kasten stilles Mineralwasser", users[1], 6.99, 3., "USD", new Date(System.currentTimeMillis()-5*5*60*1000), "Daheim", 52.520007, 13.404954, 0),
                new Desire("Fass Bier", "5l-Fass Bier", users[0], 9.95, 5., "USD", new Date(System.currentTimeMillis()-6*5*60*1000), "Star Wars", 52.520007, 13.404954, 2),
                new Desire("Router", "WLAN-Router mit Setup", users[1], 49., 6., "USD", new Date(System.currentTimeMillis()-7*5*60*1000), "Schule", 49.452030, 11.076750, 1)
        };
    }

    public static void setDesireIds(Desire[] desires, int mediaLength, int usersLength) {
        for (int i = 0; i < desires.length; i++)
            desires[i].setId((long) i + usersLength + mediaLength + 1);
    }

    public static Rating[] getRatings(long userId) {

        Media[] media = getMedia();

        setMediaIds(media);

        User[] users = getUsers();

        setUserIds(users, media.length);

        Desire[] desires = getDesires();

        setDesireIds(desires, media.length, users.length);

        return new Rating[]{
                new Rating(userId, 5),
                new Rating(userId, new Date(System.currentTimeMillis()), 3, "Awesome", users[0], desires[0])
        };
    }

    public static Haver[] getHavers() {

        Media[] media = getMedia();

        setMediaIds(media);

        User[] users = getUsers();

        setUserIds(users, media.length);

        Desire[] desires = getDesires();

        setDesireIds(desires, media.length, users.length);

        Haver accepted = new Haver(users[0], new Date(System.currentTimeMillis()), desires[1].getId());
        accepted.setStatus(HaverStatus.ACCEPTED);

        return new Haver[]{
                new Haver(users[1], new Date(System.currentTimeMillis()), desires[0].getId()),
                new Haver(users[2], new Date(System.currentTimeMillis()), desires[0].getId()),
                new Haver(users[3], new Date(System.currentTimeMillis()), desires[0].getId()),
                new Haver(users[4], new Date(System.currentTimeMillis()), desires[0].getId()),
                new Haver(users[5], new Date(System.currentTimeMillis()), desires[0].getId()),
                new Haver(users[6], new Date(System.currentTimeMillis()), desires[0].getId()),
                new Haver(users[7], new Date(System.currentTimeMillis()), desires[0].getId()),
                accepted,
                new Haver(users[1], new Date(System.currentTimeMillis()), desires[1].getId()),
                new Haver(users[0], new Date(System.currentTimeMillis()), desires[2].getId()),
                new Haver(users[1], new Date(System.currentTimeMillis()), desires[2].getId()),
                new Haver(users[0], new Date(System.currentTimeMillis()), desires[3].getId()),
                new Haver(users[2], new Date(System.currentTimeMillis()), desires[3].getId()),
                new Haver(users[1], new Date(System.currentTimeMillis()), desires[4].getId()),
                new Haver(users[2], new Date(System.currentTimeMillis()), desires[4].getId()),
                new Haver(users[0], new Date(System.currentTimeMillis()), desires[5].getId()),
                new Haver(users[2], new Date(System.currentTimeMillis()), desires[5].getId()),
                new Haver(users[1], new Date(System.currentTimeMillis()), desires[6].getId()),
                new Haver(users[2], new Date(System.currentTimeMillis()), desires[6].getId()),
                new Haver(users[0], new Date(System.currentTimeMillis()), desires[7].getId()),
                new Haver(users[2], new Date(System.currentTimeMillis()), desires[7].getId())
        };
    }

    public static DesireFlag[] getFlags() {

        Media[] media = getMedia();

        setMediaIds(media);

        User[] users = getUsers();

        setUserIds(users, media.length);

        Desire[] desires = getDesires();

        setDesireIds(desires, media.length, users.length);

        return new DesireFlag[]{
                new DesireFlag(desires[0].getId(), FlagReason.IMPOSSIBLE, "Geht nicht"),
                new DesireFlag(desires[0].getId(), FlagReason.INAPPROPRIATE, "Ih"),
                new DesireFlag(desires[0].getId(), FlagReason.SPAM, "..."),
                new DesireFlag(desires[0].getId(), FlagReason.SPAM, "....")
        };
    }
}
