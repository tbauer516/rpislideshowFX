package network;

import controllers.MainController;
import network.model.DarkSkyResponseCurrent;
import network.model.DarkSkyResponseForecast;
import network.model.WeatherCurrentModel;
import network.model.WeatherDayModel;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class DarkSkyRequest {
    private String secret;
    private String lat;
    private String lng;

    private Client darkClient;

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

        this.darkClient = ClientBuilder.newClient();
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

    private Response makeRequest(String requestURL) {
        WebTarget resource = darkClient.target(requestURL);
        Invocation.Builder request = resource.request();
        request.accept(MediaType.APPLICATION_JSON);
        Response response = request.get();
        return response;
    }

    public WeatherCurrentModel getWeatherCurrent() {
        String requestURL = createPath(CURRENT);
        Response response = makeRequest(requestURL);
        if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
            DarkSkyResponseCurrent resp = response.readEntity(DarkSkyResponseCurrent.class);
            return resp.getData();
        }
        System.out.println("ERROR! " + response.getStatus());
        System.out.println(response.getEntity());
        return null;
    }

    public WeatherDayModel[] getWeatherForecast() {
        String requestURL = createPath(FORECAST);
        Response response = makeRequest(requestURL);
        if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
            DarkSkyResponseForecast resp = response.readEntity(DarkSkyResponseForecast.class);
            return resp.getData();
        }
        System.out.println("ERROR! " + response.getStatus());
        System.out.println(response.getEntity());
        return null;
    }
}
