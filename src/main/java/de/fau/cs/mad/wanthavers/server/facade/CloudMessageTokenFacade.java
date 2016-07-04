package de.fau.cs.mad.wanthavers.server.facade;


import de.fau.cs.mad.wanthavers.common.CloudMessageToken;
import de.fau.cs.mad.wanthavers.server.dao.CloudMessageTokenDAO;

import java.util.List;

public class CloudMessageTokenFacade {
    private final CloudMessageTokenDAO dao;

    public CloudMessageTokenFacade(CloudMessageTokenDAO dao) {
        this.dao = dao;
    }

    public List<CloudMessageToken> getAllTokens(long userId) {
        return dao.findAll(userId);
    }

    public CloudMessageToken createNewToken(long userId, CloudMessageToken newToken) {
        return dao.create(userId, newToken);
    }

    public CloudMessageToken getTokenById(long userId, long id) {
        return dao.findById(userId, id);
    }

    public CloudMessageToken updateToken(long userId, long id, CloudMessageToken token) {
        return dao.update(userId, id, token);
    }

    public boolean deleteToken(long userId, String token) {
        return dao.deleteToken(token);
    }
}
