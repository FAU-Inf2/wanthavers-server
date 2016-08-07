package de.fau.cs.mad.wanthavers.server.dao;


import de.fau.cs.mad.wanthavers.common.LangString;
import de.fau.cs.mad.wanthavers.server.ServerApplication;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class LangStringDAO extends AbstractSuperDAO<LangString>{

    public LangStringDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    private LangString getFallbackValue(String key){
        Criteria criteria = currentSession().createCriteria(LangString.class)
                .add(Restrictions.eq("key", key))
                .add(Restrictions.eq("langCode", ServerApplication.DEFAULT_LANGCODE));

        LangString ls = (LangString) criteria.uniqueResult();
        return ls;
    }

    public LangString getByKeyAndLangCode(String key, String code){
        if(code == null){
            return getFallbackValue(key);
        }

        Criteria criteria = currentSession().createCriteria(LangString.class)
                .add(Restrictions.eq("key", key))
                .add(Restrictions.eq("langCode", code));

        LangString ls = (LangString)criteria.uniqueResult();

        /** use default language as fallback **/
        if(ls == null){
            ls = getFallbackValue(key);
        }
        return ls;
    }

    public LangString saveOrReplace(LangString ls){
        LangString tmp = getByKeyAndLangCode(ls.getKey(), ls.getLangCode());
        if( tmp != null && tmp.getLangCode().equals(ls.getLangCode())){
            delete(tmp);
        }

        return persist(ls);
    }

    public List<LangString> getAll(){
        Query query = super.currentSession().createQuery("SELECT u FROM LangString u");
        List<LangString> result = super.list(query);
        return result;
    }

}
