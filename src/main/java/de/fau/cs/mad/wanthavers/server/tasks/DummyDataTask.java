package de.fau.cs.mad.wanthavers.server.tasks;

import com.google.common.collect.ImmutableMultimap;
import de.fau.cs.mad.wanthavers.server.misc.SessionContextTask;
import org.hibernate.SessionFactory;

import java.io.PrintWriter;


public class DummyDataTask extends SessionContextTask {
    public DummyDataTask(String taskName, SessionFactory sessionFactory) {
        super(taskName, sessionFactory);
    }

    @Override
    public void run(ImmutableMultimap<String, String> parameters, PrintWriter output) throws Exception {

    }
}
