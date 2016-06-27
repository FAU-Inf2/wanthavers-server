package de.fau.cs.mad.wanthavers.server.misc;

import com.google.common.collect.ImmutableMultimap;
import de.fau.cs.mad.wanthavers.server.ServerApplication;
import de.fau.cs.mad.wanthavers.server.SingletonManager;
import io.dropwizard.servlets.tasks.Task;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.context.internal.ManagedSessionContext;

import java.io.PrintWriter;

public abstract class SessionContextTask extends Task{
    private SessionFactory sessionFactory;

    public SessionContextTask(String taskName, SessionFactory sessionFactory) {
        super(taskName);
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void execute(ImmutableMultimap<String, String> parameters, PrintWriter output) throws Exception {
        Session session = sessionFactory.openSession();
        ManagedSessionContext.bind(session);
        Transaction transaction = session.beginTransaction();
        try {
            /** excecute template method **/
            run(parameters, output);
            transaction.commit();
        }catch(Exception e) {
            transaction.rollback();
            throw new RuntimeException(e);
        }finally {
            session.close();
            ManagedSessionContext.unbind(sessionFactory);
        }
    }

    public void executeNow() throws Exception {
        execute(null, new PrintWriter(System.out));
    }

    public abstract void run(ImmutableMultimap<String, String> parameters, PrintWriter output) throws Exception;
}
