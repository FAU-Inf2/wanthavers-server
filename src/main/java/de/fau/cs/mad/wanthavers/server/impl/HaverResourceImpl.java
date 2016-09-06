package de.fau.cs.mad.wanthavers.server.impl;

import de.fau.cs.mad.wanthavers.common.*;
import de.fau.cs.mad.wanthavers.common.rest.api.HaverResource;
import de.fau.cs.mad.wanthavers.server.SingletonManager;
import de.fau.cs.mad.wanthavers.server.cloudmessaging.CloudMessage;
import de.fau.cs.mad.wanthavers.server.cloudmessaging.CloudMessageSender;
import de.fau.cs.mad.wanthavers.server.facade.DesireFacade;
import de.fau.cs.mad.wanthavers.server.facade.HaverFacade;
import de.fau.cs.mad.wanthavers.server.misc.DynamicStringParser;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.ApiParam;

import javax.ws.rs.WebApplicationException;
import java.util.Date;
import java.util.List;

import static de.fau.cs.mad.wanthavers.server.misc.TranslationHelper.getTranslatedString;


public class HaverResourceImpl implements HaverResource {
    private final HaverFacade facade;
    private final DesireFacade desireFacade;

    public HaverResourceImpl(HaverFacade facade, DesireFacade desireFacade) {
        this.facade = facade;
        this.desireFacade = desireFacade;
    }

    @Override
    @UnitOfWork
    public List<Haver> getAllHavers(@ApiParam(value = "id of the desired desire", required = true) long desireId) {
        return this.facade.getAllHavers(desireId);
    }

    @Override
    @UnitOfWork
    public Haver get(@ApiParam(value = "id of the desired desire", required = true) long desireId, @ApiParam(value = "id of the user", required = true) long userId) {
        Haver haver = facade.getHaverByUserId(desireId, userId);

        if (haver == null)
            throw new WebApplicationException(404);

        return haver;
    }

    @Override
    @UnitOfWork
    public Haver createHaver(@Auth User user, @ApiParam(value = "id of the desired desire", required = true) long desireId, @ApiParam(value = "Haver to create", required = true) Haver newHaver) {
        if (facade.getHaverByUserId(desireId, user.getId()) != null) {
            throw new WebApplicationException(409);
        }

        newHaver.setUser(user);
        newHaver.setCreationDate(new Date(System.currentTimeMillis()));
        newHaver.setStatus(HaverStatus.ADDED);

        check(user, newHaver, desireId);

        return facade.createNewHaver(desireId, newHaver);
    }

    @Override
    @UnitOfWork
    public Haver getAccepted(@ApiParam(value = "id of the desired desire", required = true) long desireId) {
        return this.facade.getAccepted(desireId);
    }

    @Override
    @UnitOfWork
    public Haver updateHaver(@Auth User user, @ApiParam(value = "id of the desired desire", required = true) long desireId, @ApiParam(value = "id of the haver", required = true) long id, @ApiParam(value = "new details of the specified haver", required = true) Haver haver) {
        check(user, haver, desireId);

        return setHaverStatus(user, desireId, id, haver, haver.getStatus());
    }

    @Override
    @UnitOfWork
    public Haver updateHaverStatus(@Auth User user, @ApiParam(value = "id of the desire", required = true) long desireId, @ApiParam(value = "id of the haver relation", required = true) long userId, int status) {
        Haver stored = facade.getHaverByUserId(desireId, userId);

        if (stored == null)
            throw new WebApplicationException(404);

        return setHaverStatus(user, desireId, stored.getId(), stored, status);
    }

    @Override
    @UnitOfWork
    public Haver updateRequestedPrice(@Auth User user, long desireId, long userId, double requestedPrice) {
        if (user.getId() != userId)
            throw new WebApplicationException(403);

        Haver stored = facade.getHaverByUserId(desireId, userId);

        if (stored == null)
            throw new WebApplicationException(404);

        stored.setRequestedPrice(requestedPrice);

        return facade.updateHaver(desireId, stored.getId(), stored);
    }

    private Haver setHaverStatus(User user, long desireId, long haverId, Haver haver, int status) {
        if (status == HaverStatus.ACCEPTED && acceptedHaverAlreadyExists(desireId)) {
            throw new WebApplicationException(409);
        }

        switch (status) {
            case HaverStatus.ACCEPTED:
                Desire d = desireFacade.getDesireByID(desireId);
                d.setStatus(DesireStatus.STATUS_IN_PROGRESS);
                desireFacade.updateDesire(desireId, d);
                break;
            case HaverStatus.ADDED:
            case HaverStatus.DELETED:
                /* Unaccept haver */
                // See if haver was already accepted and revert status of desire
                if (getAccepted(desireId) == null)
                    break;
                if (haver.getId() == getAccepted(desireId).getId()) {
                    d = desireFacade.getDesireByID(desireId);
                    d.setStatus(DesireStatus.STATUS_OPEN);
                    desireFacade.updateDesire(desireId, d);

                    /** Send push notifications to Wanter if Haver unaccepted**/
                    if(user.getId() == haver.getId()) {
                        DynamicStringParser cloudMessageStr = DynamicStringParser.parse(
                                getTranslatedString("HAVER_UNACCEPTED_NOTIFICATION_BODY", d.getCreator().getLangCode()));
                        cloudMessageStr = cloudMessageStr.set("desire", d.getTitle());
                        cloudMessageStr = cloudMessageStr.set("haver", haver.getUser().getName());

                        CloudMessage message = new CloudMessage(d.getCreator().getId(),
                                CloudMessageSubject.HAVERUNACCEPTED,
                                cloudMessageStr.getValue(),
                                getTranslatedString("DESIRE_COMPLETE_NOTIFICATION_TITLE", d.getCreator().getLangCode()));
                        message.addKeyValue(CloudMessageSubject.HAVERUNACCEPTED_DESIREID, desireId);
                        message.addKeyValue(CloudMessageSubject.HAVERUNACCEPTED_DESIRETITLE, d.getTitle());
                        CloudMessageSender.sendMessage(message);
                    } else {
                        /** Send push notifications to Haver if Wanter unaccepted **/
                        DynamicStringParser cloudMessageStr = DynamicStringParser.parse(
                                getTranslatedString("HAVER_UNACCEPTED_NOTIFICATION_BODY", haver.getUser().getLangCode()));
                        cloudMessageStr = cloudMessageStr.set("desire", d.getTitle());
                        cloudMessageStr = cloudMessageStr.set("wanter", d.getCreator().getName());

                        CloudMessage message = new CloudMessage(haver.getUser().getId(),
                                CloudMessageSubject.HAVERUNACCEPTED,
                                cloudMessageStr.getValue(),
                                getTranslatedString("DESIRE_COMPLETE_NOTIFICATION_TITLE", haver.getUser().getLangCode()));
                        message.addKeyValue(CloudMessageSubject.HAVERUNACCEPTED_DESIREID, desireId);
                        message.addKeyValue(CloudMessageSubject.HAVERUNACCEPTED_DESIRETITLE, d.getTitle());
                        CloudMessageSender.sendMessage(message);
                    }
                }
                break;
            case HaverStatus.REJECTED:
                break;
        }

        haver.setStatus(status);

        return facade.updateHaver(desireId, haver.getId(), haver);
    }

    private void check(User user, Haver haver, long desireId) throws WebApplicationException {
        DesireFacade desireFacade = (DesireFacade) SingletonManager.get(DesireFacade.class);
        Desire desire = desireFacade.getDesireByID(desireId);

        if (desire == null) {
            throw new WebApplicationException("desire not found", 404);
        }

        if (user.getId() != haver.getUser().getId() && user.getId() != desire.getCreator().getId()) {
            throw new WebApplicationException(401);
        }

        if (haver.getDesireId() != desireId) {
            throw new WebApplicationException(400);
        }
    }

    private boolean acceptedHaverAlreadyExists(long desireId) {
        return facade.getAccepted(desireId) != null;
    }
}
