package info.tylerbauer.rpislideshow.network;

import info.tylerbauer.rpislideshow.network.model.DarkSkyResponseCurrent;
import info.tylerbauer.rpislideshow.network.model.DarkSkyResponseForecast;
import info.tylerbauer.rpislideshow.network.model.WeatherCurrentModel;
import info.tylerbauer.rpislideshow.network.model.WeatherDayModel;

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

    public DarkSkyRequest(String secret, String lat, String lng) {
        this.secret = secret;
        this.lat = lat;
        this.lng = lng;

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

        return null;
    }

    public WeatherDayModel[] getWeatherForecast() {
        String requestURL = createPath(FORECAST);
        Response response = makeRequest(requestURL);
        if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
            DarkSkyResponseForecast resp = response.readEntity(DarkSkyResponseForecast.class);
            return resp.getData();
        }

        return null;
    }
}
