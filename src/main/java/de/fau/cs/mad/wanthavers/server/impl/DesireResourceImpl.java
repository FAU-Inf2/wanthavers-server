package de.fau.cs.mad.wanthavers.server.impl;

import de.fau.cs.mad.wanthavers.common.Chat;
import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.User;
import de.fau.cs.mad.wanthavers.common.rest.api.DesireResource;
import de.fau.cs.mad.wanthavers.server.dummy.Dummies;
import de.fau.cs.mad.wanthavers.server.facade.*;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.ApiParam;

import javax.ws.rs.WebApplicationException;
import java.util.*;

public class DesireResourceImpl implements DesireResource {
    private static boolean dummyExecuted = false;

    private final DesireFacade desireFacade;
    private final CategoryFacade categoryFacade;
    private final RatingFacade ratingFacade;
    private final ChatFacade chatFacade;
    private final UserFacade userFacade;

    public DesireResourceImpl(DesireFacade desireFacade, CategoryFacade categoryFacade, RatingFacade ratingFacade, ChatFacade chatFacade, UserFacade userFacade) {
        this.desireFacade = desireFacade;
        this.categoryFacade = categoryFacade;
        this.ratingFacade = ratingFacade;
        this.chatFacade = chatFacade;
        this.userFacade = userFacade;
    }

    @Override
    @UnitOfWork
    public List<Desire> get() {
        return this.desireFacade.getAllDesires();
    }

    @Override
    @UnitOfWork
    public List<Desire> getByFilters(Long category, Double price_min, Double price_max, Double reward_min, Float rating_min, Double lat, Double lon, Double radius, List<Integer> status, Long lastCreationTime, Integer limit, Long creatorId) {
        //TODO: Filter by category
        return desireFacade.getAllDesiresByFilter(price_min, price_max, reward_min, rating_min, lat, lon, radius, status, lastCreationTime, limit, creatorId);

/*        List<Desire> desiresByFilter = desireFacade.getAllDesiresByFilter(price_min, price_max, reward_min, rating_min, lat, lon, radius, status, lastCreationTime, limit, creatorId);
        List<Desire> desires;

        if (category != null) {
            List<Desire> desiresByCategory = categoryFacade.getDesiresByCategoryDeep(category);

            Set<Desire> desiresByFilterSet = new HashSet<>(desiresByFilter);
            Set<Desire> desiresByCategorySet = new HashSet<>(desiresByCategory);

            desiresByFilterSet.retainAll(desiresByCategorySet);
            desires = new ArrayList<>(desiresByFilterSet);
        } else {
            desires = desiresByFilter;
        }

        return desires;*/
    }

    @Override
    @UnitOfWork
    public Desire get(@ApiParam(value = "id of the desired Desire", required = true) long id) {
        Desire ret = desireFacade.getDesireByID(id);

        if (ret == null) {
            throw new WebApplicationException(404);
        }

        return ret;
    }

    @Override
    @UnitOfWork
    public Desire createDesire(@Auth User user, @ApiParam(value = "Desire to create", required = true) Desire newDesire) {
        //set desire creator
        newDesire.setCreator(user);
        newDesire.setCreation_time(new Date(System.currentTimeMillis()));

        return desireFacade.createNewDesire(newDesire);
    }

    @Override
    @UnitOfWork
    public Desire updateDesire(@Auth User user, @ApiParam(value = "id of the Desire", required = true) long id, @ApiParam(value = "new details of the specified Desire", required = true) Desire desire) {
        checkPermission(user, id);
        desire.setCreator(user);
        return desireFacade.updateDesire(id, desire);
    }

    @Override
    @UnitOfWork
    public Desire updateDesireStatus(@Auth User user, @ApiParam(value = "id of the Desire", required = true) long id, @ApiParam(value = "new status of the specified Desire", required = true) int status) {
        checkPermission(user, id);
        Desire ret = desireFacade.updateDesireStatus(id, status);

        if (ret == null) {
            throw new WebApplicationException(404);
        }

        return ret;
    }

    @Override
    @UnitOfWork
    public Chat getChat(@Auth User user, long user2, long desireId) {
        return this.chatFacade.getChat(user.getId(), user2, desireId);
    }


    /**
     * TODO: remove after tests
     */
    @Override
    @UnitOfWork
    public void createDummies() {
        if (dummyExecuted) {
            return;
        }

        Desire[] desires = Dummies.getDesires();

        for (Desire d : desires) {
            desireFacade.createNewDesire(d);
        }

        dummyExecuted = true;
    }

    private void checkPermission(User u, long id) throws WebApplicationException {
        Desire d = this.desireFacade.getDesireByID(id);
        if (d == null) {
            throw new WebApplicationException(404);
        }
        if (u.getId() != d.getCreator().getId()) {
            throw new WebApplicationException(401);
        }
    }


}
