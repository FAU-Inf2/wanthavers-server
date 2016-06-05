package de.fau.cs.mad.wanthavers.server.dao;


import de.fau.cs.mad.wanthavers.common.Category;
import de.fau.cs.mad.wanthavers.common.Location;
import io.dropwizard.hibernate.AbstractDAO;
import org.glassfish.jersey.client.ClientResponse;
import org.hibernate.SessionFactory;
import org.json.JSONObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class LocationDAO extends AbstractDAO<Category>{

    private final SessionFactory sessionFactory;

    public LocationDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    public Location getReverseGeo(double lat, double lon){
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://nominatim.openstreetmap.org").path("reverse").queryParam("lat", lat).queryParam("lon", lon).queryParam("format", "json");
        Response response = target.request(MediaType.APPLICATION_JSON_TYPE).get(Response.class);
        String str = response.readEntity(String.class);
        JSONObject json = new JSONObject(str);
        JSONObject address = json.getJSONObject("address");
        Location l = new Location();
        l.setLat(lat);
        l.setLon(lon);
        String tmp = address.getString("road")+" "+address.getString("house_number")+", "+address.getString("postcode")+" "+address.getString("city")+", "+address.getString("country");
        l.setFullAddress(tmp);
        return l;
    }


}