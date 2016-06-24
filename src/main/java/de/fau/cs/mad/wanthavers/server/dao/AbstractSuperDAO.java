package de.fau.cs.mad.wanthavers.server.dao;

import de.fau.cs.mad.wanthavers.common.AbstractModel;
import de.fau.cs.mad.wanthavers.common.Desire;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

import java.sql.Timestamp;
import java.util.List;

public abstract class AbstractSuperDAO<E extends AbstractModel> extends AbstractDAO<E> {
    protected final SessionFactory sessionFactory;

    public AbstractSuperDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    public E findById(long id) {
        return super.get(id);
    }

    public E create(E newE) {
        return persist(newE);
    }

    public boolean delete(E e) {
        if (e == null)
            return false;

        currentSession().delete(e);

        return true;
    }

    public void deleteById(long id){
        delete(findById(id));
    }

    /*
    public List<E> findAllNew(E last) {
        long timestamp = last.getLastModified().getTime();
        String className = last.getClass().getSimpleName();

        String sqlQuery = "SELECT e FROM" + className + "WHERE e.lastModified > :timestamp";
        Query query = super.currentSession().createQuery(sqlQuery);
        query.setParameter("timestamp", timestamp);

        return super.list(query);
    }

    public E update(E update) {
        update.setLastModified(new Timestamp(System.currentTimeMillis()));
        return update;
    }
    */
}
