package info.tylerbauer.rpislideshow.network.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DarkSkyResponseCurrent {
    @JsonProperty
    private WeatherCurrentModel currently;

    public DarkSkyResponseCurrent() {}

    public WeatherCurrentModel getData() {
        return currently;
    }

    public String toString(){
        return currently.toString();
    }
}
