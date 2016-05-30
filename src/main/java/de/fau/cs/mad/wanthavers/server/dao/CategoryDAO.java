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
        Query query = super.currentSession().createQuery("SELECT c FROM Category c WHERE parentId = "+id);
        List<Category> result = super.list(query);
        return result;
    }

    public Category create(Category newCategory) {
        return persist(newCategory);
    }

    public void delete(long id) {
        Query query = super.currentSession().createQuery("DELETE FROM Category WHERE id="+id);
        query.executeUpdate();
    }

    public Category update(long id, Category newCategory) {
        Category c = findById(id);
        c.setImage(newCategory.getImage());
        c.setName(newCategory.getName());
        c.setParent(newCategory.getParent());

        persist(c);
        return c;
    }
}
