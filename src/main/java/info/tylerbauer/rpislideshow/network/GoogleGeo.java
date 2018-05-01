package info.tylerbauer.rpislideshow.network;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import info.tylerbauer.rpislideshow.network.model.GeoLoc;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;

public class GoogleGeo {
    private static String resourceURL = "http://maps.google.com/maps/api/geocode/json";
    private static String addressParam = "address";

    public static GeoLoc getLatLng(String address) {
        Client client = ClientBuilder.newClient();

        WebTarget resource = client.target(resourceURL).queryParam(addressParam, address);

        Invocation.Builder request = resource.request();
        request.accept(MediaType.APPLICATION_JSON);

        ObjectMapper mapper = new ObjectMapper();

        Response response = request.get();

        if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
            System.out.println("ERROR! " + response.getStatus());
            System.out.println(response.getEntity());
            return null;
        }

        JsonNode jsonResponse = null;
        try {
            jsonResponse = mapper.readTree(response.readEntity(InputStream.class));
        } catch (IOException e) {
            System.out.println("there was an error reading the response");
        }

        JsonNode loc = jsonResponse.path("results").get(0).path("geometry").path("location");
        String lat = loc.path("lat").asText();
        String lng = loc.path("lng").asText();
        return new GeoLoc(lat, lng);
    }
}
