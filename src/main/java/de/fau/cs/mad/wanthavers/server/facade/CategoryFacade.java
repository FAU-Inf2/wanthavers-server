package de.fau.cs.mad.wanthavers.server.facade;

import de.fau.cs.mad.wanthavers.common.Category;
import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.server.dao.CategoryDAO;

import java.util.List;

public class CategoryFacade {
    private final CategoryDAO dao;

    public CategoryFacade(CategoryDAO dao){
        this.dao = dao;
    }

    public Category getById(long id){
        return dao.findById(id);
    }

    public List<Category> getSubCategoriesFlat(long id){
        return dao.getSubCategories(id);
    }

    public List<Category> getSubCategoriesDeep(long id){
        return dao.getSubCategoriesRecursive(id);
    }

    public Category create(Category newCategory){
        dao.invalidateCache(); //invalidate cache when CRUD operation
        return dao.create(newCategory);
    }

    public void delete(long id){
        dao.invalidateCache(); //invalidate cache when CRUD operation
        dao.delete(id);
    }

    public Category update(long id, Category newCategory) {
        dao.invalidateCache(); //invalidate cache when CRUD operation
        return this.dao.update(id, newCategory);
    }

    public List<Desire> getDesiresByCategoryFlat(long id){
        return this.dao.getDesiresByCategoryFlat(id);
    }

    public List<Desire> getDesiresByCategoryDeep(long id){
        return this.dao.getDesiresByCategoryDeep(id);
    }
}
