package network.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DailyWrapper {
    @JsonProperty
    public WeatherDayModel[] data;

    public DailyWrapper() {}

    public String toString(){
        String toReturn = "";
        for (WeatherDayModel day : data) {
            toReturn += day.toString() + "\n";
        }
        return toReturn;
    }
}
