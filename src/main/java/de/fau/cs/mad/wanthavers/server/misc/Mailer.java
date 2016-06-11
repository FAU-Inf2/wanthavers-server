package de.fau.cs.mad.wanthavers.server.misc;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.*;

import java.util.List;

public class Mailer {
    private static String FROM_MAIL;
    private static Regions AWS_REGION = Regions.US_WEST_2;
    private static boolean FAKE = false;
    private static String FAKE_TO;

    public static void configure(String fromMail, Regions region){
        FROM_MAIL = fromMail;
        AWS_REGION = region;
    }

    public static void enableFake(String to){
        FAKE = true;
        FAKE_TO = to;
    }

    public static void send(String subject, String body, List<String> to){
        if(FAKE){
            send(subject, body, "");
        }
        Destination destination = new Destination().withToAddresses((String[])to.toArray());
        sendHelper(subject, body, destination);
    }

    public static void send(String subject, String body, String to){
        if(FAKE){
            to = FAKE_TO;
        }
        Destination destination = new Destination().withToAddresses(new String[]{to});
        sendHelper(subject, body, destination);
    }

    private static void sendHelper(String sSubject, String sBody, Destination destination){

        Content subject = new Content().withData(sSubject);
        Content textBody = new Content().withData(sBody);
        Body body = new Body().withHtml(textBody);

        Message message = new Message().withSubject(subject).withBody(body);
        SendEmailRequest request = new SendEmailRequest().withSource(FROM_MAIL).withDestination(destination).withMessage(message);

        try {

            String accessKey = System.getenv("S3_ACCESS_KEY");
            String secretKey = System.getenv("S3_SECRET_KEY");
            AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
            AmazonSimpleEmailServiceClient client = new AmazonSimpleEmailServiceClient(credentials);
            Region region = Region.getRegion(AWS_REGION);
            client.setRegion(region);
            client.sendEmail(request);
        } catch (Exception ex) {
            System.out.println("Error message: " + ex.getMessage());
        }
    }
}
