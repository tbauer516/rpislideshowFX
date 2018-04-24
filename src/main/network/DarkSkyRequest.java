package network;

import org.glassfish.jersey.client.ClientResponse;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class DarkSkyRequest {
    private String secret;
    private String lat;
    private String lng;
    private String address;

    private String baseUrl = "https://api.darksky.net/forecast/";
    private String exclude = "?exclude=minutely,hourly,alerts,flags";

    public static final String FORECAST = "forecast";
    public static final String CURRENT = "current";

    public DarkSkyRequest(String secret, String lat, String lng, String address) {
        this.secret = secret;

        String realLat = lat;
        String realLng = lng;
        if (realLat == null || realLng == null) {
            Object result = GoogleGeo.getLatLng(address);
        }

        this.lat = realLat;
        this.lng = realLng;
    }

    private String createPath(String type) {
        String toExclude = "";
        switch (type) {
            case FORECAST:
                toExclude = "currently";
                break;
            case CURRENT:
                toExclude = "daily";
                break;
        }

        return baseUrl + secret + "/" + lat + "," + lng + exclude + "," + toExclude;
    }

    public String getWeather(String type) {
        String requestURL = createPath(type);

        Client client = ClientBuilder.newClient();

        WebTarget resource = client.target(requestURL);

        Invocation.Builder request = resource.request();
        request.accept(MediaType.APPLICATION_JSON);

        Response response = request.get();

        if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
            System.out.println("Success! " + response.getStatus());
            System.out.println(response.getEntity());
        } else {
            System.out.println("ERROR! " + response.getStatus());
            System.out.println(response.getEntity());
        }

        return "";
    }
}
