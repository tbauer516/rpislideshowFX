package info.tylerbauer.rpislideshow.network.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherCurrentModel {
    @JsonProperty
    public String icon;
    @JsonProperty(value = "temperature")
    public double temp;
    @JsonProperty(value = "windSpeed")
    public double wind;
    @JsonProperty(value = "precipProbability")
    public double precip;

    public WeatherCurrentModel() {}

    public String toString() {
        return icon + "\n" + temp + "\u00b0F\n" + wind + " MPH, " + precip + "%";
    }
}
