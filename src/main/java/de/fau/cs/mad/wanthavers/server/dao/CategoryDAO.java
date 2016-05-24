package de.fau.cs.mad.wanthavers.server.dao;


import de.fau.cs.mad.wanthavers.common.Category;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

import java.util.List;

public class CategoryDAO extends AbstractDAO<Category>{

    private final SessionFactory sessionFactory;

    public CategoryDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    public Category findById(long id) {
        return super.get(id);
    }

    public List<Category> getSubCategories(long id) {
        Query query = super.currentSession().createQuery("SELECT c FROM Category c WHERE parent = "+id);
        List<Category> result = super.list(query);
        return result;
    }

    public Category create(Category newCategory) {
        return persist(newCategory);
    }



}
