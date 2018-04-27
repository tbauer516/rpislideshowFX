package network.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherDayModel {
    @JsonProperty
    public String icon;
    @JsonProperty(value = "temperatureHigh")
    public double tempHigh;
    @JsonProperty(value = "temperatureLow")
    public double tempLow;
    @JsonProperty(value = "windSpeed")
    public double wind;
    @JsonProperty(value = "precipProbability")
    public double precip;

    public WeatherDayModel() {}

    public String toString() {
        return icon + "\n" + tempHigh + "\u00b0F > " + tempLow + "\u00b0F\n" + wind + " MPH, " + precip + "%";
    }
}
