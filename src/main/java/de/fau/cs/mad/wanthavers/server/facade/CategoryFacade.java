package de.fau.cs.mad.wanthavers.server.facade;

import de.fau.cs.mad.wanthavers.common.Category;
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

    public List<Category> getSubCategories(long id){
        return dao.getSubCategories(id);
    }

    public Category create(Category newCategory){
        return dao.create(newCategory);
    }

    public void delete(long id){
        dao.delete(id);
    }

    public Category update(long id, Category newCategory) {
        return this.dao.update(id, newCategory);
    }
}
