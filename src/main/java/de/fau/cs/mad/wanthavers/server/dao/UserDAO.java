package de.fau.cs.mad.wanthavers.server.dao;

import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.User;
import de.fau.cs.mad.wanthavers.server.auth.HashHelper;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class UserDAO extends AbstractDAO<User> {

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
        Session session = sessionFactory.openSession();

        Query query = session.createQuery("SELECT u FROM User u WHERE id=" + id);
        User result = super.uniqueResult(query);

        session.close();
        return result;

/*        Query query = this.sessionFactory.openSession().createQuery("SELECT u FROM User u WHERE id="+id);
        User result = super.uniqueResult(query);
        return result;*/
    }

    public User create(User user, String password) throws Exception {
        return persist(user);
    }

    public User update(long id, User modified) {
        User stored = findById(id);
        stored.setName(modified.getName());
        stored.setEmail(modified.getEmail());
        stored.setImage(modified.getImage());
        stored.setBirthday(modified.getBirthday());

        persist(stored);
        return stored;
    }

    public boolean delete(User user) {
        if (findById(user.getID()) == null) {
            return false;
        }

        currentSession().delete(user);

        return true;
    }

    public List<User> findAll() {
        Query query = super.currentSession().createQuery("SELECT u FROM User u");
        List<User> result = super.list(query);
        return result;
    }


    public List<Desire> getDesires(long id) {
        assert findById(id) != null;

        // Maybe avoid hard coded query, see http://stackoverflow.com/a/2671395
        Query query = currentSession().createQuery("FROM Desire WHERE creator.id = :id");
        query.setParameter("id", id);

        List<Desire> desires = query.list();

        return desires;
    }

    public List<Desire> getDesiresAsHaver(long id) {
        assert findById(id) != null;

        // Maybe avoid hard coded query, see http://stackoverflow.com/a/2671395
        Query query = currentSession().createQuery("SELECT desireId FROM Haver WHERE user.id = :id");
        query.setParameter("id", id);

        List<Long> ids = query.list();

        Query query2 = currentSession().createQuery("FROM Desire WHERE id IN :id");
        query2.setParameterList("id", ids);

        List<Desire> desires = query2.list();

        return desires;
    }

    public User getUserByEmail(String email) {
        Query query = currentSession().createQuery("FROM User WHERE email = :email");
        query.setParameter("email", email);

        User user = super.uniqueResult(query);
        return user;
    }
}
