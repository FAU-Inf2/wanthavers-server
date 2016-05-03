package de.fau.cs.mad.wanthavers.server.dao;

import de.fau.cs.mad.wanthavers.common.User;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

import java.util.List;

public class UserDAO extends AbstractDAO<User>{

    private final SessionFactory sessionFactory;

    /**
     * Creates a new DAO with a given session provider.
     *
     * @param sessionFactory a session provider
     */
    public UserDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    public User findById(long id) {
        return super.get(id);
    }

    public User create(User user){
        return persist(user);
    }

    public User update(long id, User modified){
        User stored = findById(id);
        stored.setName(modified.getName());
        stored.setEmail(modified.getEmail());

        persist(stored);
        return stored;
    }

    public boolean delete(User user){
        if(findById(user.getID()) == null){
            return false;
        }

        currentSession().delete(user);

        return true;
    }

    public List<User> findAll(){
        Query query = super.currentSession().createQuery("SELECT u FROM User u");
        List<User> result = super.list(query);
        return result;
    }
}
