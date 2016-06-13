package de.fau.cs.mad.wanthavers.server.dao;


import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.DesireFlag;
import de.fau.cs.mad.wanthavers.server.SingletonManager;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class DesireFlagDAO extends AbstractSuperDAO<DesireFlag> {

    public DesireFlagDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }


    public List<DesireFlag> getDesireFlagsByDesireId(long id) {
        final Session session = currentSession();

        Criteria criteria = session.createCriteria(DesireFlag.class)
                .add(Restrictions.eq(Desire.DESIRE_ID, id));

        List<DesireFlag> flags = criteria.list();

        return flags;
    }

}
