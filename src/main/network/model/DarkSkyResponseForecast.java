package network.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DarkSkyResponseForecast {
    @JsonProperty
    private DailyWrapper daily;

    public DarkSkyResponseForecast() {}

    public WeatherDayModel[] getData() {
        WeatherDayModel[] days = daily.data;
        WeatherDayModel[] trimmed = new WeatherDayModel[6];

        for (int i = 1; i <= 6; i++) {
            trimmed[i - 1] = days[i];
        }

        return trimmed;
    }

    public String toString(){
        return daily.toString();
    }
}
