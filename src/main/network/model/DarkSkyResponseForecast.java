package network.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DarkSkyResponseForecast {
    @JsonProperty
    public DailyWrapper daily;

    public DarkSkyResponseForecast() {}

    public String toString(){
        return daily.toString();
    }
}
