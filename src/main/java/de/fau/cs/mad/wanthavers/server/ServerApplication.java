package de.fau.cs.mad.wanthavers.server;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class ServerApplication extends Application<ServerConfiguration> {
    @Override
    public void run(ServerConfiguration configuration, Environment environment) throws Exception {
        environment.jersey().register(new UserResourceImpl());
    }

    public static void main(String[] args) {
        try {
            new ServerApplication().run(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
