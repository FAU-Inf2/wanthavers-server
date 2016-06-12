package de.fau.cs.mad.wanthavers.server.dao;


import de.fau.cs.mad.wanthavers.common.Category;
import de.fau.cs.mad.wanthavers.common.Desire;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
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

    public List<Category> getSubCategoriesRecursive(long id) {
        List<Category> sub = getSubCategories(id);
        List<Category> result = new ArrayList<>();
        result.addAll(sub);
        for (Category c : sub){
            result.addAll(getSubCategoriesRecursive(c.getId()));
        }
        return result;
    }

    public Category create(Category newCategory) {
        return persist(newCategory);
    }

    public void delete(long id) {
        Query query = super.currentSession().createQuery("DELETE FROM Category WHERE id="+id);
        query.executeUpdate();
    }

    public Category update(long id, Category modified) {
        Category stored = findById(id);
        modified.setId(stored.getId());
        currentSession().merge(modified);

        return modified;
    }

    public List<Desire> getDesiresByCategoryFlat(long id){
        Query query = super.currentSession().createQuery("FROM Desire WHERE categoryId = :id");
        query.setParameter("id", id);

        List<Desire> desires = query.list();
        return desires;
    }

    public List<Desire> getDesiresByCategoryDeep(long id){
        List<Category> list = getSubCategoriesRecursive(id);
        System.out.println(list.size());
        List<Desire> result = new ArrayList<>();
        result.addAll(getDesiresByCategoryFlat(id));
        for(Category c : list){
            result.addAll(getDesiresByCategoryFlat(c.getId()));
        }
        return result;
    }

}
