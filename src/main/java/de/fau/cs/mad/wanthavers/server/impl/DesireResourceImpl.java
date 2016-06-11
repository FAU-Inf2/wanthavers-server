package de.fau.cs.mad.wanthavers.server.impl;

import de.fau.cs.mad.wanthavers.common.Chat;
import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.User;
import de.fau.cs.mad.wanthavers.common.rest.api.DesireResource;
import de.fau.cs.mad.wanthavers.server.dummy.Dummies;
import de.fau.cs.mad.wanthavers.server.facade.CategoryFacade;
import de.fau.cs.mad.wanthavers.server.facade.ChatFacade;
import de.fau.cs.mad.wanthavers.server.facade.DesireFacade;
import de.fau.cs.mad.wanthavers.server.facade.RatingFacade;
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

    public DesireResourceImpl(DesireFacade desireFacade, CategoryFacade categoryFacade, RatingFacade ratingFacade, ChatFacade chatFacade) {
        this.desireFacade = desireFacade;
        this.categoryFacade = categoryFacade;
        this.ratingFacade = ratingFacade;
        this.chatFacade = chatFacade;
    }

    @Override
    @UnitOfWork
    public List<Desire> get() {
        return this.desireFacade.getAllDesires();
    }

    @Override
    @UnitOfWork
    public List<Desire> getByFilters(Long category, Double price_min, Double price_max, Double reward_min, Float rating_min, Double lat, Double lon, Double radius, List<Integer> status, Long lastCreationTime, Integer limit) {
        List<Desire> desiresByFilter = desireFacade.getAllDesiresByFilter(price_min, price_max, reward_min, lat, lon, radius, status, lastCreationTime, limit);
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

        List<Desire> ret = new ArrayList<>();

        if (rating_min != null && rating_min >= 0) {
            HashMap<Long, Float> avgUserRating = new HashMap<>();

            for (Desire d : desires) {
                long userId = d.getCreator().getID();
                if (!avgUserRating.containsKey(userId)) {
                    avgUserRating.put(userId, ratingFacade.avgRating(userId).getStars());
                }

                if (avgUserRating.get(userId) >= rating_min) {
                    ret.add(d);
                }
            }
        } else {
            ret = desires;
        }

        return ret;
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
        return this.chatFacade.getChat(user.getID(), user2, desireId);
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

    private void checkPermission(User u, long id) throws WebApplicationException{
        Desire d = this.desireFacade.getDesireByID(id);
        if(d == null){
            throw new WebApplicationException(404);
        }
        if(u.getID() != d.getCreator().getID()){
            throw new WebApplicationException(401);
        }
    }


}
