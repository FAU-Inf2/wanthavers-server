package de.fau.cs.mad.wanthavers.server.dao;

import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.Haver;
import de.fau.cs.mad.wanthavers.common.Rating;
import de.fau.cs.mad.wanthavers.common.User;
import de.fau.cs.mad.wanthavers.server.auth.HashHelper;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import javax.persistence.criteria.Selection;
import java.util.List;

import static de.fau.cs.mad.wanthavers.common.User.USER_ID;

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
        modified.setId(stored.getID());
        currentSession().merge(modified);

        //persist(modified);
        return modified;
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

    public List<Desire> getDesiresAsHaver(long userId, List<Integer> status) {
        assert findById(userId) != null;

        Criteria criteria = currentSession().createCriteria(Haver.class)
                .setProjection(Projections.property(Desire.DESIRE_ID))
                .add(Restrictions.eq("user.id", userId));

        if(status != null) {
            criteria.add(Restrictions.in("status", status));
        }

        List<Long> ids = criteria.list();

        Query query2 = currentSession().createQuery("FROM Desire WHERE id IN :id");
        query2.setParameterList("id", ids);

        List<Desire> desires = query2.list();

        return desires;
    }

    public User getUserByEmail(String email) {
        Session session = sessionFactory.openSession();

        Query query = session.createQuery("FROM User WHERE email = :email");
        query.setParameter("email", email);

        User user = super.uniqueResult(query);
        session.close();
        return user;
    }

    public void sendResetToken(String email){
        User user = getUserByEmail(email);
        String token = user.createPasswordToken();
        persist(user);
        System.out.println(token);
    }

    public boolean resetPassword(String token, String newPassword){
        Query query = currentSession().createQuery("FROM User WHERE passwordToken = :token");
        query.setParameter("token", token);

        User user = (User)query.uniqueResult();
        if(user == null || !user.checkPasswordToken(token)){
            return false;
        }

        user.createPasswordToken();
        try {
            user.setPassword(HashHelper.getSaltedHash(newPassword));
        } catch (Exception e) {
            return false;
        }

        persist(user);
        return true;
    }
}
