package de.fau.cs.mad.wanthavers.server.facade;


import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.DesireFlag;
import de.fau.cs.mad.wanthavers.server.dao.DesireDAO;
import de.fau.cs.mad.wanthavers.server.dao.DesireFlagDAO;
import de.fau.cs.mad.wanthavers.server.misc.Mailer;

import java.util.List;

public class DesireFlagFacade {
    private final DesireFlagDAO dao;
    private final DesireDAO desireDAO;

    public DesireFlagFacade(DesireFlagDAO dao, DesireDAO desireDAO) {
        this.dao = dao;
        this.desireDAO = desireDAO;
    }


    public List<DesireFlag> getDesireFlags(long id) {
        return dao.getDesireFlagsByDesireId(id);
    }

    public DesireFlag flagDesire(long id, DesireFlag desireFlag) {
        Desire desire = desireDAO.findById(id);
        if(desire == null)
            return null;

        DesireFlag ret = dao.create(desireFlag);

        desire.setFlagCounter(desire.getFlagCounter() + 1);
        desireDAO.update(id, desire);

        if(desire.getFlagCounter() % 5 == 0) {
            List<DesireFlag> flags = dao.getDesireFlagsByDesireId(id);
            String reasons = "";
            for(DesireFlag f : flags)
                reasons += f.getFlagReason() + ": " + f.getComment() + "\n";

            Mailer.send("Desire with Id "+desire.getId()+":"+desire.getTitle()+" has over 5 flags!", reasons, "");
        }

        return ret;
    }

    public boolean unflagDesire(long id, long flagId) {
        Desire desire = desireDAO.findById(id);
        if(desire == null)
            return false;

        boolean ret = dao.delete(dao.findById(flagId));

        desire.setFlagCounter(desire.getFlagCounter() - 1);
        desireDAO.update(id, desire);

        return ret;
    }
}
