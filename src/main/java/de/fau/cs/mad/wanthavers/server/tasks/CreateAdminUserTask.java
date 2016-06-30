package de.fau.cs.mad.wanthavers.server.tasks;

import com.google.common.collect.ImmutableMultimap;
import de.fau.cs.mad.wanthavers.common.User;
import de.fau.cs.mad.wanthavers.common.UserRoles;
import de.fau.cs.mad.wanthavers.server.SingletonManager;
import de.fau.cs.mad.wanthavers.server.facade.UserFacade;
import de.fau.cs.mad.wanthavers.server.impl.UserResourceImpl;
import de.fau.cs.mad.wanthavers.server.misc.SessionContextTask;
import org.hibernate.SessionFactory;

import java.io.PrintWriter;


public class CreateAdminUserTask extends SessionContextTask {

    public CreateAdminUserTask(String taskName, SessionFactory sessionFactory) {
        super(taskName, sessionFactory);
    }

    @Override
    public void run(ImmutableMultimap<String, String> parameters, PrintWriter output) throws Exception {
        String adminPw = System.getenv("ADMIN_PW") == null ? "test" : System.getenv("ADMIN_PW");
        String adminEmail = System.getenv("ADMIN_EMAIL") == null ? "wh" : System.getenv("ADMIN_EMAIL");
        User admin = new User("Admin", adminEmail);
        admin.setPassword(adminPw);
        admin.setLangCode("en_EN");
        admin.setRole(UserRoles.USER_ROLE_ADMIN);

        UserResourceImpl userResourceImpl = (UserResourceImpl) SingletonManager.get(UserResourceImpl.class);
        UserFacade userFacade = (UserFacade) SingletonManager.get(UserFacade.class);

        if (userFacade.getUserByEmail(adminEmail) == null) {
            userResourceImpl.createUser(admin, adminPw);
        }
    }
}
