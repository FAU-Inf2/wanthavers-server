package de.fau.cs.mad.wanthavers.server.facade;

import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.Media;
import de.fau.cs.mad.wanthavers.common.User;
import de.fau.cs.mad.wanthavers.server.dao.DesireDAO;
import de.fau.cs.mad.wanthavers.server.dao.MediaDAO;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import java.io.InputStream;
import java.util.List;

public class MediaFacade {
    private final MediaDAO dao;

    public MediaFacade(MediaDAO dao){
        this.dao = dao;
    }

    public Media getById(long id){
        return dao.findById(id);
    }

    public List<Media> getAll(){
        return dao.findAll();
    }

    public Media createNewMedia(User user, String base64, String filename){
        return dao.create(user, base64, filename);
    }

    public Media createNewMedia(Media media) {
        return dao.create(media);
    }

}
