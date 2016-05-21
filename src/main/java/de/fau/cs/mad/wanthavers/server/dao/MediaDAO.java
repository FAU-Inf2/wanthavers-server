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
import io.dropwizard.hibernate.AbstractDAO;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.io.File;
import java.io.InputStream;
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

    public Media create(InputStream fileInputStream,
                       FormDataContentDisposition contentDispositionHeader) {

        Media m = new Media();

        String accessKey = System.getenv("S3_ACCESS_KEY");
        String secretKey = System.getenv("S3_SECRET_KEY");

        AmazonS3 s3client = new AmazonS3Client(new BasicAWSCredentials(accessKey, secretKey));

        try {

            String extension = "";
            String[] tmp = contentDispositionHeader.getFileName().split("\\.");
            if(tmp.length > 1){
                extension = "."+tmp[tmp.length - 1];
            }

            String filename = UUID.randomUUID().toString()+extension;
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentDisposition(contentDispositionHeader.toString());

            AWSSessionCredentials s3SourceFactory;
            PutObjectResult putObjectResult = s3client.putObject(new PutObjectRequest("whimages", filename, fileInputStream, metadata));

            m.setFullRes("https://s3.eu-central-1.amazonaws.com/whimages/"+filename);
            m.setMediumRes("https://s3.eu-central-1.amazonaws.com/whimages/"+filename);
            m.setLowRes("https://s3.eu-central-1.amazonaws.com/whimages/"+filename);

        } catch (AmazonServiceException ase) {
            ase.printStackTrace();
        } catch (AmazonClientException ace) {
            ace.printStackTrace();
        }



        return persist(m);
    }



}
