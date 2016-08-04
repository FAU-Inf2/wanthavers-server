package de.fau.cs.mad.wanthavers.server.tasks;

import com.google.common.collect.ImmutableMultimap;
import de.fau.cs.mad.wanthavers.server.misc.SessionContextTask;
import org.hibernate.SessionFactory;

import java.io.PrintWriter;

public class DesireExpirationCheckTask extends SessionContextTask {
    //21 days until desires on server expire if user didn't give a date
    public static final long EXPIRE_DURATION = 1000 * 60 * 60 * 24 * 21;

    public DesireExpirationCheckTask(String taskName, SessionFactory sessionFactory) {
        super(taskName, sessionFactory);
    }

    @Override
    public void run(ImmutableMultimap<String, String> parameters, PrintWriter output) throws Exception {
        new Thread(new DesireExpirationCheck()).start();
    }

    class DesireExpirationCheck implements Runnable {
        @Override
        public void run() {


            try {
                Thread.sleep(0L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
