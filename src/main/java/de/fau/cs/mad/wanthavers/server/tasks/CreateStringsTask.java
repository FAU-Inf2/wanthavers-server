package de.fau.cs.mad.wanthavers.server.tasks;

import com.google.common.collect.ImmutableMultimap;
import de.fau.cs.mad.wanthavers.server.SingletonManager;
import de.fau.cs.mad.wanthavers.server.facade.LangStringFacade;
import de.fau.cs.mad.wanthavers.server.misc.SessionContextTask;
import org.hibernate.SessionFactory;

import java.io.PrintWriter;

public class CreateStringsTask extends SessionContextTask {
    private static String DE = "de_DE";
    private static String EN = "en_EN";

    public CreateStringsTask(String taskName, SessionFactory sessionFactory) {
        super(taskName, sessionFactory);
    }

    @Override
    public void run(ImmutableMultimap<String, String> parameters, PrintWriter output) throws Exception {
        setTranslation("HAVER_NEW_NOTIFICATION_BODY", "A new Haver would like to help you out with {{desire}}", EN);
        setTranslation("HAVER_NEW_NOTIFICATION_TITLE", "New Haver!", EN);
        setTranslation("HAVER_NEW_NOTIFICATION_BODY", "Es möchte jemand deinen Job {{desire}} ausführen", DE);
        setTranslation("HAVER_NEW_NOTIFICATION_TITLE", "Anfrage", DE);




    }

    private void setTranslation(String key, String value, String langCode){
        LangStringFacade lsf = (LangStringFacade) SingletonManager.get(LangStringFacade.class);
        lsf.createLangString(key, value, langCode);
    }



}