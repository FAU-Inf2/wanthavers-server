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
        setTranslation("HAVER_NEW_NOTIFICATION_BODY", "Es möchte jemand deinen Job {{desire}} ausführen", DE);
        setTranslation("HAVER_NEW_NOTIFICATION_TITLE", "New Haver", EN);
        setTranslation("HAVER_NEW_NOTIFICATION_TITLE", "Neuer Haver", DE);

        setTranslation("HAVER_ACCEPTED_NOTIFICATION_BODY", "You were accepted for {{desireTitle}} by {{desireCreator}}.", EN);
        setTranslation("HAVER_ACCEPTED_NOTIFICATION_BODY", "Du wurdest für den Job {{desireTitle}} von {{desireCreator}} angenommen.", DE);
        setTranslation("HAVER_ACCEPTED_NOTIFICATION_TITLE", "New Job", EN);
        setTranslation("HAVER_ACCEPTED_NOTIFICATION_TITLE", "Neuer Job", DE);

        setTranslation("HAVER_REJECTED_NOTIFICATION_BODY", "Sorry! You didn't get the job {{desireTitle}} by {{desireCreator}}. Better luck next time!", EN);
        setTranslation("HAVER_REJECTED_NOTIFICATION_BODY", "Sorry! Du hast den Job {{desireTitle}} von {{desireCreator}} nicht bekommen. Viel Glück beim nächsten mal!", DE);
        setTranslation("HAVER_REJECTED_NOTIFICATION_TITLE", "Rejected", EN);
        setTranslation("HAVER_REJECTED_NOTIFICATION_TITLE", "Abgehlehnt", DE);

        setTranslation("DESIRE_COMPLETE_NOTIFICATION_BODY", "Desire {{desire}} was completed. Rate now.", EN);
        setTranslation("DESIRE_COMPLETE_NOTIFICATION_BODY", "Job {{desire}} wurde abgeschlossen. Bitte bewerte die Transaktion.", DE);
        setTranslation("DESIRE_COMPLETE_NOTIFICATION_TITLE", "Job completed", EN);
        setTranslation("DESIRE_COMPLETE_NOTIFICATION_TITLE", "Job abgeschlossen", DE);
    }

    private void setTranslation(String key, String value, String langCode){
        LangStringFacade lsf = (LangStringFacade) SingletonManager.get(LangStringFacade.class);
        lsf.createLangString(key, value, langCode);
    }



}