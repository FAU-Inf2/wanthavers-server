package de.fau.cs.mad.wanthavers.server.impl;

import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.User;
import de.fau.cs.mad.wanthavers.common.rest.api.DesireResource;
import de.fau.cs.mad.wanthavers.server.dummy.Dummies;
import de.fau.cs.mad.wanthavers.server.facade.CategoryFacade;
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

    public DesireResourceImpl(DesireFacade desireFacade, CategoryFacade categoryFacade, RatingFacade ratingFacade) {
        this.desireFacade = desireFacade;
        this.categoryFacade = categoryFacade;
        this.ratingFacade = ratingFacade;
    }

    @Override
    @UnitOfWork
    public List<Desire> get() {
        return this.desireFacade.getAllDesires();
    }

    @Override
    @UnitOfWork
    public List<Desire> getByFilters(Long category, Double price_min, Double price_max, Double reward_min, Float rating_min, Double lat, Double lon, Double radius) {
        List<Desire> desiresByFilter = desireFacade.getAllDesiresByFilter(price_min, price_max, reward_min, lat, lon, radius);
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

                if (avgUserRating.get(userId) > rating_min) {
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
    public Desire updateDesire(@ApiParam(value = "id of the Desire", required = true) long id, @ApiParam(value = "new details of the specified Desire", required = true) Desire desire) {
        return desireFacade.updateDesire(id, desire);
    }

    @Override
    @UnitOfWork
    public Desire updateDesireStatus(@ApiParam(value = "id of the Desire", required = true) long id, @ApiParam(value = "new status of the specified Desire", required = true) int status) {
        Desire ret = desireFacade.updateDesireStatus(id, status);

        if (ret == null) {
            throw new WebApplicationException(404);
        }

        return ret;
    }

    @Override
    @UnitOfWork
    public void deleteDesire(@ApiParam(value = "id of the to be deleted Desire", required = true) long id) {
        desireFacade.deleteDesire(id);
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


}
