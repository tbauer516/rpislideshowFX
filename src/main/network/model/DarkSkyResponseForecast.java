package network.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DarkSkyResponseForecast {
    @JsonProperty
    private DailyWrapper daily;

    public DarkSkyResponseForecast() {}

    public WeatherDayModel[] getData() {
        return daily.data;
    }

    public String toString(){
        return daily.toString();
    }
}
