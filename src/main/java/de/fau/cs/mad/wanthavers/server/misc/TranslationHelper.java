package de.fau.cs.mad.wanthavers.server.misc;

import de.fau.cs.mad.wanthavers.common.Category;
import de.fau.cs.mad.wanthavers.common.LangString;
import de.fau.cs.mad.wanthavers.server.SingletonManager;
import de.fau.cs.mad.wanthavers.server.dao.LangStringDAO;

import java.util.ArrayList;
import java.util.List;

public class TranslationHelper {

    public static String getTranslatedString(String key, String langCode){
        LangStringDAO langStringDAO = (LangStringDAO)SingletonManager.get(LangStringDAO.class);
        LangString ls = langStringDAO.getByKeyAndLangCode(key, langCode);
        String value = null;
        if(ls != null){
            value = ls.getValue();
        }


        return value;
    }


    public static Category translate(Category c, String langCode){
        String name = getTranslatedString(c.getLangStringKey(), langCode);
        c.setName(name);
        return c;
    }

    public static List<Category> translate(List<Category> cs, String langCode){
        List<Category> tmp = new ArrayList<>();
        for(Category c : cs){
            tmp.add(translate(c, langCode));
        }
        return tmp;
    }
}
