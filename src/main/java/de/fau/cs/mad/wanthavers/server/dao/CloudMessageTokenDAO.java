package de.fau.cs.mad.wanthavers.server.dao;


import de.fau.cs.mad.wanthavers.common.CloudMessageToken;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;

import static de.fau.cs.mad.wanthavers.common.User.USER_ID;


public class CloudMessageTokenDAO extends AbstractDAO<CloudMessageToken> {

    private final SessionFactory sessionFactory;

    public CloudMessageTokenDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    private List<CloudMessageToken> findTokensByUserId(long userId) {
        final Session session = currentSession();

        Criteria criteria = session.createCriteria(CloudMessageToken.class)
                .add(Restrictions.eq(USER_ID, userId));

        List<CloudMessageToken> tokens = criteria.list();

        return tokens;
    }

    /**
     *
     * @param userId
     * @param token
     * @return null if userId of rating does not match param userId
     */
    private CloudMessageToken checkForCorrectUserId(long userId, CloudMessageToken token) {
        return token.getUserId() == userId ? token : null;
    }

    public List<CloudMessageToken> findAll(long userId) {
        return findTokensByUserId(userId);
    }

    public CloudMessageToken create(long userId, CloudMessageToken newToken) {
        // Prevent app from persisting the same token multiple times
        List<CloudMessageToken> registered = findTokensByUserId(userId);
        for(CloudMessageToken token : registered)
            if(token.getToken().equals(newToken.getToken()))
                return token;

        return persist(newToken);
    }

    public CloudMessageToken findById(long userId, long id) {
        CloudMessageToken token = super.get(id);
        return checkForCorrectUserId(userId, token);
    }

    public CloudMessageToken update(long userId, long id, CloudMessageToken token) {
        CloudMessageToken stored = findById(userId, id);
        if(stored == null || checkForCorrectUserId(userId, stored) == null) return null;

        stored.setToken(token.getToken());
        persist(stored);
        return stored;
    }

    public boolean delete(CloudMessageToken token) {
        if(token == null)
            return false;

        currentSession().delete(token);

        return true;
    }
}
