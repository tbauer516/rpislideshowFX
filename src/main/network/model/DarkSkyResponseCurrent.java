package network.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DarkSkyResponseCurrent {
    @JsonProperty
    public WeatherCurrentModel currently;

    public DarkSkyResponseCurrent() {}

    public String toString(){
        return currently.toString();
    }
}
