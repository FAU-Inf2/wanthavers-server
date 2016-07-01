package de.fau.cs.mad.wanthavers.server.tasks;

import com.google.common.collect.ImmutableMultimap;
import de.fau.cs.mad.wanthavers.common.Category;
import de.fau.cs.mad.wanthavers.common.LangString;
import de.fau.cs.mad.wanthavers.common.Media;
import de.fau.cs.mad.wanthavers.server.SingletonManager;
import de.fau.cs.mad.wanthavers.server.dao.LangStringDAO;
import de.fau.cs.mad.wanthavers.server.dao.MediaDAO;
import de.fau.cs.mad.wanthavers.server.facade.CategoryFacade;
import de.fau.cs.mad.wanthavers.server.misc.SessionContextTask;
import org.hibernate.SessionFactory;

import java.io.PrintWriter;
import java.util.List;

public class CreateCategoriesTask extends SessionContextTask {
    public static final String CAR_KEY        = "CATEGORY_CAR";
    public static final String BEAUTY_KEY     = "CATEGORY_BEAUTY";
    public static final String BOOK_KEY       = "CATEGORY_BOOK";
    public static final String OFFICE_KEY     = "CATEGORY_OFFICE";
    public static final String TABLET_KEY     = "CATEGORY_TABLET";
    public static final String FOOD_KEY       = "CATEGORY_FOOD";
    public static final String DRINKS_KEY     = "CATEGORY_DRINKS";
    public static final String DIY_KEY        = "CATEGORY_DIY";
    public static final String CLOTHES_KEY    = "CATEGORY_CLOTHES";
    public static final String LIVING_KEY     = "CATEGORY_LIVING";
    public static final String GAMES_KEY      = "CATEGORY_GAMES";
    public static final String TRAVEL_KEY     = "CATEGORY_TRAVEL";
    public static final String TICKETS_KEY    = "CATEGORY_TICKETS";
    public static final String ESTATE_KEY     = "CATEGORY_ESTATE";
    public static final String OTHER_KEY      = "CATEGORY_OTHER";
    public static final String SERVICE_KEY    = "CATEGORY_SERVICE";


    public CreateCategoriesTask(String taskName, SessionFactory sessionFactory) {
        super(taskName, sessionFactory);
    }

    @Override
    public void run(ImmutableMultimap<String, String> parameters, PrintWriter output) throws Exception {
        if(categoriesAlreadyExist()) {
            return;
        }

        final String DE = "de_DE";
        final String EN = "en_EN";

        final String CAR_IMAGE      = "https://s3.eu-central-1.amazonaws.com/whimages/000000c/transport.png";
        registerLangString(DE, CAR_KEY, "Auto & Motorrad");
        registerLangString(EN, CAR_KEY, "Cars & Bicycles");

        final String BEAUTY_IMAGE   = "https://s3.eu-central-1.amazonaws.com/whimages/000000c/hospital.png";
        registerLangString(DE, BEAUTY_KEY, "Beauty & Gesundheit");
        registerLangString(EN, BEAUTY_KEY, "Beauty & Health");

        final String BOOK_IMAGE     = "https://s3.eu-central-1.amazonaws.com/whimages/000000c/open-book.png";
        registerLangString(DE, BOOK_KEY, "Bücher");
        registerLangString(EN, BOOK_KEY, "Books");

        final String OFFICE_IMAGE   = "https://s3.eu-central-1.amazonaws.com/whimages/000000c/folder.png";
        registerLangString(DE, OFFICE_KEY, "Büro & Schreibwaren");
        registerLangString(EN, OFFICE_KEY, "Office & Stationery");

        final String TABLET_IMAGE   = "https://s3.eu-central-1.amazonaws.com/whimages/000000c/smartphone.png";
        registerLangString(DE, TABLET_KEY, "Computer, Tablets, Smartphones");
        registerLangString(EN, TABLET_KEY, "Computer, Tablets, Smartphones");

        final String SERVICE_IMAGE  = "https://s3.eu-central-1.amazonaws.com/whimages/000000c/builder.png";
        registerLangString(DE, SERVICE_KEY, "Dienstleistungen");
        registerLangString(EN, SERVICE_KEY, "Services");

        final String FOOD_IMAGE     = "https://s3.eu-central-1.amazonaws.com/whimages/000000c/groceries.png";
        registerLangString(DE, FOOD_KEY, "Essen & Nahrungsmittel");
        registerLangString(EN, FOOD_KEY, "Food");

        final String DRINKS_IMAGE   = "https://s3.eu-central-1.amazonaws.com/whimages/000000c/pint.png";
        registerLangString(DE, DRINKS_KEY, "Getränke");
        registerLangString(EN, DRINKS_KEY, "Drinks");

        final String DIY_IMAGE      = "https://s3.eu-central-1.amazonaws.com/whimages/000000c/tools.png";
        registerLangString(DE, DIY_KEY, "Heimwerkerbedarf");
        registerLangString(EN, DIY_KEY, "Do-It-Yourself Products");

        final String CLOTHES_IMAGE  = "https://s3.eu-central-1.amazonaws.com/whimages/000000c/shirt.png";
        registerLangString(DE, CLOTHES_KEY, "Kleidung & Schumck");
        registerLangString(EN, CLOTHES_KEY, "Clothes & Jewellery");

        final String LIVING_IMAGE   = "https://s3.eu-central-1.amazonaws.com/whimages/000000c/desk.png";
        registerLangString(DE, LIVING_KEY, "Möbel & Wohnen");
        registerLangString(EN, LIVING_KEY, "Furniture & Lifestyle");

        final String GAMES_IMAGE    = "https://s3.eu-central-1.amazonaws.com/whimages/000000c/game-controller.png";
        registerLangString(DE, GAMES_KEY, "PC- & Videospiele");
        registerLangString(EN, GAMES_KEY, "PC- & Videogames");

        final String TRAVEL_IMAGE   = "https://s3.eu-central-1.amazonaws.com/whimages/000000c/suitcase.png";
        registerLangString(DE, TRAVEL_KEY, "Reisen");
        registerLangString(EN, TRAVEL_KEY, "Travelling");

        final String TICKETS_IMAGE  = "https://s3.eu-central-1.amazonaws.com/whimages/000000c/tickets.png";
        registerLangString(DE, TICKETS_KEY, "Tickets");
        registerLangString(EN, TICKETS_KEY, "Tickets");

        final String ESTATE_IMAGE   = "https://s3.eu-central-1.amazonaws.com/whimages/000000c/house.png";
        registerLangString(DE, ESTATE_KEY, "Immobilien");
        registerLangString(EN, ESTATE_KEY, "Real Estate");

        final String OTHER_IMAGE    = "https://s3.eu-central-1.amazonaws.com/whimages/000000c/info.png";
        registerLangString(DE, OTHER_KEY, "Sonstiges");
        registerLangString(EN, OTHER_KEY, "Other");

        final String[] CATEGORY_KEYS = new String[]{
                CAR_KEY, BEAUTY_KEY, BOOK_KEY, OFFICE_KEY,
                TABLET_KEY, SERVICE_KEY, FOOD_KEY, DRINKS_KEY,
                DIY_KEY, CLOTHES_KEY, LIVING_KEY, GAMES_KEY,
                TRAVEL_KEY, TICKETS_KEY, ESTATE_KEY, OTHER_KEY
        };

        final String[] CATEGORY_IMAGES = new String[]{
                CAR_IMAGE, BEAUTY_IMAGE, BOOK_IMAGE, OFFICE_IMAGE,
                TABLET_IMAGE, SERVICE_IMAGE, FOOD_IMAGE, DRINKS_IMAGE,
                DIY_IMAGE, CLOTHES_IMAGE, LIVING_IMAGE, GAMES_IMAGE,
                TRAVEL_IMAGE, TICKETS_IMAGE, ESTATE_IMAGE, OTHER_IMAGE
        };

        CategoryFacade categoryFacade = (CategoryFacade)SingletonManager.get(CategoryFacade.class);

        for(int i=0;i<CATEGORY_KEYS.length;i++){
            Category c = new Category();
            c.setParent(0);
            c.setLangStringKey(CATEGORY_KEYS[i]);
            c.setImage(getMediaForURL(CATEGORY_IMAGES[i]));
            categoryFacade.create(c);
        }
    }

    private static void registerLangString(String langCode, String key, String value){
        LangStringDAO langStringDAO = (LangStringDAO)SingletonManager.get(LangStringDAO.class);
        LangString ls = new LangString();
        ls.setLangCode(langCode);
        ls.setKey(key);
        ls.setValue(value);
        langStringDAO.create(ls);
    }

    private static Media getMediaForURL(String url){
        MediaDAO mediaDAO = (MediaDAO)SingletonManager.get(MediaDAO.class);
        Media m = new Media();
        m.setLowRes(url);
        m.setMediumRes(url);
        m.setFullRes(url);
        return mediaDAO.create(m);
    }

    private boolean categoriesAlreadyExist() {
        CategoryFacade categoryFacade = (CategoryFacade)SingletonManager.get(CategoryFacade.class);

        List<Category> list = categoryFacade.getSubCategoriesDeep(0);

        return list.size() > 0;
    }
}