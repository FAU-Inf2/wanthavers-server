package de.fau.cs.mad.wanthavers.server.dao;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSSessionCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.SystemPropertiesCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.Haver;
import de.fau.cs.mad.wanthavers.common.Media;
import de.fau.cs.mad.wanthavers.common.User;
import io.dropwizard.hibernate.AbstractDAO;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.apache.commons.codec.binary.Base64;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import java.util.UUID;

public class MediaDAO extends AbstractDAO<Media>{

    private final SessionFactory sessionFactory;

    public MediaDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.sessionFactory = sessionFactory;
    }


    public List<Media> findAll() {
        Query query = super.currentSession().createQuery("SELECT m FROM Media m");
        List<Media> result = super.list(query);
        return result;
    }

    public Media findById(long id) {
        return super.get(id);
    }

    public Media create(Media media) {
        return persist(media);
    }

    public Media create(User u, String base64, String filename) {
        Media m = new Media();
        m.setCreator(u.getId());

        String accessKey = System.getenv("S3_ACCESS_KEY");
        String secretKey = System.getenv("S3_SECRET_KEY");

        AmazonS3 s3client = new AmazonS3Client(new BasicAWSCredentials(accessKey, secretKey));
        String extension = "";
        String[] tmp = filename.split("\\.");
        if(tmp.length > 1){
            extension = "."+tmp[tmp.length - 1];
        }


        File image = null;
        try {
            image = saveTemp(base64);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(int res : Media.RESOLUTIONS) {

            try {

                String tmpName = UUID.randomUUID().toString() + extension;
                File out = File.createTempFile(tmpName, extension);

                BufferedImage imageTmp = ImageIO.read(image);
                int size = Math.min(imageTmp.getHeight(), imageTmp.getWidth());

                Thumbnails.of(image)
                        .sourceRegion(Positions.CENTER, size, size)
                        .size(res, res)
                        .toFile(out);

                s3client.putObject(new PutObjectRequest("whimages", tmpName, out));
                String url = "https://s3.eu-central-1.amazonaws.com/whimages/"+tmpName;
                m.setImage(url, res);
                out.delete();
            } catch (AmazonServiceException ase) {
                ase.printStackTrace();
            } catch (AmazonClientException ace) {
                ace.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return persist(m);
    }


    private static File saveTemp(String base64) throws IOException {
        File file = File.createTempFile(UUID.randomUUID().toString(), ".tmp");

        // write the inputStream to a FileOutputStream
        FileOutputStream outputStream = new FileOutputStream(file);
        byte[] data = Base64.decodeBase64(base64);
        outputStream.write(data);

        return file;
    }


}
