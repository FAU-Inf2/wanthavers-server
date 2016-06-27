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

    public CreateCategoriesTask(String taskName, SessionFactory sessionFactory) {
        super(taskName, sessionFactory);
    }

    @Override
    public void run(ImmutableMultimap<String, String> parameters, PrintWriter output) throws Exception {
        final String DE = "de_DE";
        final String EN = "en_EN";

        final String CAR_IMAGE      = "https://s3.eu-central-1.amazonaws.com/whimages/377fcd6d-ed71-466b-b9ad-2681cf82e874.png";
        final String CAR_KEY        = "CATEGORY_CAR";
        registerLangString(DE, CAR_KEY, "Auto & Motorrad");
        registerLangString(EN, CAR_KEY, "Cars & Bicycles");

        final String BEAUTY_IMAGE   = "https://s3.eu-central-1.amazonaws.com/whimages/hospital.png";
        final String BEAUTY_KEY     = "CATEGORY_BEAUTY";
        registerLangString(DE, BEAUTY_KEY, "Beauty & Gesundheit");
        registerLangString(EN, BEAUTY_KEY, "Beauty & Health");

        final String BOOK_IMAGE     = "https://s3.eu-central-1.amazonaws.com/whimages/77351574-6724-44b2-99c2-fe7decaea584.png";
        final String BOOK_KEY       = "CATEGORY_BOOK";
        registerLangString(DE, BOOK_KEY, "Bücher");
        registerLangString(EN, BOOK_KEY, "Books");

        final String OFFICE_IMAGE   = "https://s3.eu-central-1.amazonaws.com/whimages/3e4e49ab-2cc4-49aa-9e9f-ce8dddbcdcbf.png";
        final String OFFICE_KEY     = "CATEGORY_OFFICE";
        registerLangString(DE, OFFICE_KEY, "Büro & Schreibwaren");
        registerLangString(EN, OFFICE_KEY, "Office & Stationery");

        final String TABLET_IMAGE   = "https://s3.eu-central-1.amazonaws.com/whimages/c9c07c9b-0321-4626-90a6-e3fdac35669e.png";
        final String TABLET_KEY     = "CATEGORY_TABLET";
        registerLangString(DE, TABLET_KEY, "Computer, Tablets, Smartphones");
        registerLangString(EN, TABLET_KEY, "Computer, Tablets, Smartphones");

        final String SERVICE_IMAGE  = "https://s3.eu-central-1.amazonaws.com/whimages/1ce29861-0d3a-4f66-b538-fd37232d6b98.png";
        final String SERVICE_KEY     = "CATEGORY_SERVICE";
        registerLangString(DE, SERVICE_KEY, "Dienstleistungen");
        registerLangString(EN, SERVICE_KEY, "Services");

        final String FOOD_IMAGE     = "https://s3.eu-central-1.amazonaws.com/whimages/9a4709eb-7fe5-44ea-b122-050505217db2.png";
        final String FOOD_KEY     = "CATEGORY_FOOD";
        registerLangString(DE, FOOD_KEY, "Essen & Nahrungsmittel");
        registerLangString(EN, FOOD_KEY, "Food");

        final String DRINKS_IMAGE   = "https://s3.eu-central-1.amazonaws.com/whimages/e96985d9-a566-49ce-a883-481a86ae529c.png";
        final String DRINKS_KEY     = "CATEGORY_DRINKS";
        registerLangString(DE, DRINKS_KEY, "Getränke");
        registerLangString(EN, DRINKS_KEY, "Drinks");

        final String DIY_IMAGE      = "https://s3.eu-central-1.amazonaws.com/whimages/2bfd6f20-91e5-47ad-a3a8-0e77aaec0cae.png";
        final String DIY_KEY     = "CATEGORY_DIY";
        registerLangString(DE, DIY_KEY, "Heimwerkerbedarf");
        registerLangString(EN, DIY_KEY, "Do-It-Yourself Products");

        final String CLOTHES_IMAGE  = "https://s3.eu-central-1.amazonaws.com/whimages/211f73cb-0611-4212-b757-abff18a88b73.png";
        final String CLOTHES_KEY     = "CATEGORY_CLOTHES";
        registerLangString(DE, CLOTHES_KEY, "Kleidung & Schumck");
        registerLangString(EN, CLOTHES_KEY, "Clothes & Jewellery");

        final String LIVING_IMAGE   = "https://s3.eu-central-1.amazonaws.com/whimages/desk.png";
        final String LIVING_KEY     = "CATEGORY_LIVING";
        registerLangString(DE, LIVING_KEY, "Möbel & Wohnen");
        registerLangString(EN, LIVING_KEY, "Furniture & Lifestyle");

        final String GAMES_IMAGE    = "https://s3.eu-central-1.amazonaws.com/whimages/f785ceae-8527-47c2-a04b-5db432a3a51f.png";
        final String GAMES_KEY     = "CATEGORY_GAMES";
        registerLangString(DE, GAMES_KEY, "PC- & Videospiele");
        registerLangString(EN, GAMES_KEY, "PC- & Videogames");

        final String TRAVEL_IMAGE   = "https://s3.eu-central-1.amazonaws.com/whimages/48bbd58e-8ed7-48d4-b33f-b488d3126411.png";
        final String TRAVEL_KEY     = "CATEGORY_TRAVEL";
        registerLangString(DE, TRAVEL_KEY, "Reisen");
        registerLangString(EN, TRAVEL_KEY, "Travelling");

        final String TICKETS_IMAGE  = "https://s3.eu-central-1.amazonaws.com/whimages/tickets.png";
        final String TICKETS_KEY     = "CATEGORY_TICKETS";
        registerLangString(DE, TICKETS_KEY, "Tickets");
        registerLangString(EN, TICKETS_KEY, "Tickets");

        final String ESTATE_IMAGE   = "https://s3.eu-central-1.amazonaws.com/whimages/house.png";
        final String ESTATE_KEY     = "CATEGORY_ESTATE";
        registerLangString(DE, ESTATE_KEY, "Immobilien");
        registerLangString(EN, ESTATE_KEY, "Real Estate");

        final String OTHER_IMAGE    = "https://s3.eu-central-1.amazonaws.com/whimages/511d5b34-bdb0-4133-b20c-da80c97d7cbe.png";
        final String OTHER_KEY     = "CATEGORY_OTHER";
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

        List<Category> cs = categoryFacade.getSubCategoriesFlat(0);
        for(Category c : cs){
            System.out.println(c.getLangStringKey());
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
}