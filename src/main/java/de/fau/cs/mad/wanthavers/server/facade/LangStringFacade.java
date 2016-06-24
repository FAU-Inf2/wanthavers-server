package de.fau.cs.mad.wanthavers.server.facade;

import de.fau.cs.mad.wanthavers.common.LangString;
import de.fau.cs.mad.wanthavers.common.Location;
import de.fau.cs.mad.wanthavers.server.dao.LangStringDAO;
import de.fau.cs.mad.wanthavers.server.dao.LocationDAO;

import java.util.List;

public class LangStringFacade {
    private final LangStringDAO dao;

    public LangStringFacade(LangStringDAO dao){
        this.dao = dao;
    }

    public void createLangString(String key, String value, String langCode){
        LangString ls = new LangString();
        ls.setKey(key);
        ls.setLangCode(langCode);
        ls.setValue(value);
        this.dao.saveOrReplace(ls);
    }

    public LangString getLangString(String key, String langCode){
        return this.dao.getByKeyAndLangCode(key, langCode);
    }

    public List<LangString> getAllLangStrings(){
        return this.dao.getAll();
    }
}
