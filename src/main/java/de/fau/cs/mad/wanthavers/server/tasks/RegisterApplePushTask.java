package de.fau.cs.mad.wanthavers.server.tasks;


import com.google.common.collect.ImmutableMultimap;
import com.relayrides.pushy.apns.ApnsClient;
import com.relayrides.pushy.apns.ApnsClientBuilder;
import de.fau.cs.mad.wanthavers.server.SingletonManager;
import de.fau.cs.mad.wanthavers.server.misc.SessionContextTask;
import io.netty.util.concurrent.Future;
import org.hibernate.SessionFactory;

import java.io.File;
import java.io.PrintWriter;

public class RegisterApplePushTask extends SessionContextTask {

    public RegisterApplePushTask(String taskName, SessionFactory sessionFactory) {
        super(taskName, sessionFactory);
    }

    @Override
    public void run(ImmutableMultimap<String, String> parameters, PrintWriter output) throws Exception {
        /** configure ApnsClient for sending push to Apple **/
        String pw = System.getenv("APPLE_PW");
        final ApnsClient apnsClient = new ApnsClientBuilder()
                .setClientCredentials(new File("wanthavers.p12"), pw)
                .build();

        String isProduction = System.getenv("IS_PRODUCTION");
        final Future<Void> connectFuture;

        if (isProduction != null && isProduction.equals("true")) {
            connectFuture = apnsClient.connect(ApnsClient.PRODUCTION_APNS_HOST);
        } else {
            connectFuture = apnsClient.connect(ApnsClient.DEVELOPMENT_APNS_HOST);
        }

        connectFuture.await();

        if(connectFuture.isSuccess()) {
            System.out.println("Connecting to APNS successful");
            SingletonManager.add(apnsClient);
        } else {
            System.out.println("Connecting to APNS failed");
        }
    }
}
