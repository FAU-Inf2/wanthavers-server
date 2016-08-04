package de.fau.cs.mad.wanthavers.server.tasks;

import com.google.common.collect.ImmutableMultimap;
import de.fau.cs.mad.wanthavers.common.DesireStatus;
import de.fau.cs.mad.wanthavers.server.misc.SessionContextTask;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.PrintWriter;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DesireExpirationCheckTask extends SessionContextTask {
    //21 days until desires on server expire if user didn't give a date
    public static final long DEFAULT_EXPIRE_DURATION_DAYS = 21;

    //task runs every 5 minutes
    public static final long SLEEP_TIME_MINUTES = 5;

    private SessionFactory sessionFactory;

    public DesireExpirationCheckTask(String taskName, SessionFactory sessionFactory) {
        super(taskName, sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void run(ImmutableMultimap<String, String> parameters, PrintWriter output) throws Exception {
        new Thread(new DesireExpirationCheck()).start();
    }

    class DesireExpirationCheck implements Runnable {
        @Override
        public void run() {
            for (; ; ) {
                String updateStatement = "UPDATE Desire SET status = :status " +
                        "WHERE ( expireDate > :dateNow " +
                        "OR (expireDate IS NULL AND creation_time < :dateDefaultExpired) ) " +
                        "AND status = " + DesireStatus.STATUS_OPEN;
                Session session = sessionFactory.openSession();
                Query query = session.createQuery(updateStatement);
                query.setParameter("status", DesireStatus.STATUS_EXPIRED);
                query.setParameter("dateNow", new Date(System.currentTimeMillis()));
                //if no expire date is given by user then compare to server default
                query.setParameter("dateDefaultExpired", new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(DEFAULT_EXPIRE_DURATION_DAYS)));

                query.executeUpdate();
                session.close();

                try {
                    Thread.sleep(TimeUnit.MINUTES.toMillis(SLEEP_TIME_MINUTES));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
