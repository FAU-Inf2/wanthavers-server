package de.fau.cs.mad.wanthavers.server.facade;

import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.Location;
import de.fau.cs.mad.wanthavers.common.User;
import de.fau.cs.mad.wanthavers.server.dao.UserDAO;

import java.util.List;

public class UserFacade {
    private final UserDAO dao;

    public UserFacade(UserDAO dao){
        this.dao = dao;
    }

    public User getUserByID(long id){
        return dao.findById(id);
    }

    public User createNewUser(User user, String password) throws Exception {
        return dao.create(user, password);
    }

    public User updateUser(long id, User user){
        return dao.update(id, user);
    }

    public boolean deleteUser(long id){
        return dao.delete(dao.findById(id));
    }

    public List<User> getAllUsers(){
       return this.dao.findAll();
    }

    public List<Desire> getDesires(long id) { return this.dao.getDesires(id);}

    public List<Desire> getDesiresAsHaver(long id,  List<Integer> status) { return  this.dao.getDesiresAsHaver(id, status); }

    public User getUserByEmail(String email) { return this.dao.getUserByEmail(email); }

    public void sendResetToken(String email){ this.dao.sendResetToken(email);}

    public boolean resetPassword(String token, String newPassword){
        return this.dao.resetPassword(token, newPassword);
    }

    public List<Location> getSavedLocations(long userid){
        return this.dao.getSavedLocations(userid);
    }

}
