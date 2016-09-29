package de.fau.cs.mad.wanthavers.server.dao;

import de.fau.cs.mad.wanthavers.common.*;
import de.fau.cs.mad.wanthavers.server.ServerApplication;
import de.fau.cs.mad.wanthavers.server.SingletonManager;
import de.fau.cs.mad.wanthavers.server.auth.HashHelper;
import de.fau.cs.mad.wanthavers.server.misc.Mailer;
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

public class UserDAO extends AbstractSuperDAO<User> {

    public UserDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }


    @Override
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
        return super.create(user);
    }

    public User update(long id, User modified) {
        User stored = findById(id);
        modified.setId(stored.getId());
        modified.setPassword(stored.getPassword());
        currentSession().merge(modified);

        //persist(modified);
        return modified;
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

        if(status != null && !status.isEmpty()) {
            criteria.add(Restrictions.in("status", status));
        }

        List<Long> ids = criteria.list();

        Query query2 = currentSession().createQuery("FROM Desire WHERE id IN :id");
        query2.setParameterList("id", ids);

        List<Desire> desires = query2.list();

        return desires;
    }

    public List<Location> getSavedLocations(long id) {
        final Session session = currentSession();

        Criteria criteria = session.createCriteria(Location.class)
                .add(Restrictions.eq(USER_ID, id));

        return criteria.list();
    }

    public User getUserByEmail(String email) {
        Session session = sessionFactory.openSession();

        Query query = session.createQuery("FROM User WHERE email = :email");
        query.setParameter("email", email);

        User user = super.uniqueResult(query);
        session.close();
        return user;
    }

    public boolean sendResetToken(String email){
        User user = getUserByEmail(email);
        if(user == null){
            return false;
        }
        String token = user.createPasswordToken();
        persist(user);
        //System.out.println(token);
        String url = ServerApplication.SERVER_URL+"/password.html#"+token;
        String body = "Hi "+ user.getName()+"!<br><br>";
        body += "We got a request to reset your password. Click on the following link to set a new password or ignore this mail if you did not do this intentionally. <br>";
        body += ("<a href='"+ url + "'>Reset your password here</a>");
        body += "<br><br>";
        body += "Cheers, Wanthavers-Team";
        Mailer.send("Password Reset", body, email);
        return true;
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

    public UserFlag flagUser(long issuerId, long userId) {

        UserFlag uf = new UserFlag();
        uf.setIssuerId(issuerId);
        uf.setUserId(userId);

        UserFlagDAO userFlagDao = (UserFlagDAO)SingletonManager.get(UserFlagDAO.class);
        userFlagDao.create(uf);

        return uf;
    }

    public boolean isUserBlocked(long issuerId, long userId){
        Criteria criteria = currentSession().createCriteria(UserFlag.class)
                .add(Restrictions.eq("issuerId", issuerId))
                .add(Restrictions.eq("userId", userId));

        if(criteria.list().size() > 0){
            return true;
        }

        return false;
    }
}
