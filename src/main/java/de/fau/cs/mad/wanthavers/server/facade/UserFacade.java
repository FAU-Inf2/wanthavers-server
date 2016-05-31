package de.fau.cs.mad.wanthavers.server.facade;

import de.fau.cs.mad.wanthavers.common.Desire;
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

    public User getUserByEmail(String email) { return this.dao.getUserByEmail(email); }
}
