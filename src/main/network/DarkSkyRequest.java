package network;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class DarkSkyRequest {
    private String baseUrl = "https://api.darksky.net/forecast/";
    private String lat = "150";
    private String lng = "150";
    private String exclude = "?exclude=minutely,hourly,alerts,flags";

    private final String FORECAST = "forecast";
    private final String CURRENT = "current";
//    current: 'daily',
//    forecast: 'currently'

    private String createPath(String type){
        return "";
//        return baseUrl + key + '/' + lat + ',' + lng + exclude + ',' + type;
    }

    public String getWeather(String type){
        Form form = new Form();
        form.param("x", "foo");
        form.param("y", "bar");

        Client client = ClientBuilder.newClient();

        WebTarget resource = client.target("http://localhost:8080/someresource");

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
