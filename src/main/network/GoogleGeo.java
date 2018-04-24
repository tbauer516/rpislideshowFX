package network;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class GoogleGeo {
    private static String resourceURL = "http://maps.google.com/maps/api/geocode/json";
    private static String addressParam = "address";

    public static Object getLatLng(String address) {
        Client client = ClientBuilder.newClient();

        WebTarget resource = client.target(resourceURL).queryParam(addressParam, address);

        Invocation.Builder request = resource.request();
        request.accept(MediaType.APPLICATION_JSON);

        Response response = request.get();

        System.out.println(response.getEntity());
        if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL)
            return response.getEntity();

        System.out.println("ERROR! " + response.getStatus());
        System.out.println(response.getEntity());

        return null;
    }
}
