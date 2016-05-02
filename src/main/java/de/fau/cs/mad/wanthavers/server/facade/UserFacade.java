package de.fau.cs.mad.wanthavers.server.facade;

import de.fau.cs.mad.wanthavers.common.User;
import de.fau.cs.mad.wanthavers.server.dao.UserDAO;

public class UserFacade {
    private final UserDAO dao;

    public UserFacade(UserDAO dao){
        this.dao = dao;
    }

    public User getUserByID(long id){
        return dao.findById(id);
    }

    public User createNewUser(User user){
        return dao.create(user);
    }

    public User updateUser(long id, User user){
        return dao.update(id, user);
    }

    public boolean deleteUser(long id){
        return dao.delete(dao.findById(id));
    }
}
