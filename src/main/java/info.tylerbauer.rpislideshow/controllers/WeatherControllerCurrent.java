package info.tylerbauer.rpislideshow.controllers;

import javafx.fxml.FXML;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;
import info.tylerbauer.rpislideshow.assets.WeatherIcon;
import info.tylerbauer.rpislideshow.network.model.WeatherCurrentModel;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class WeatherControllerCurrent {

    @FXML
    private Text time;
    @FXML
    private SVGPath icon;
    @FXML
    private Text temp;
    @FXML
    private Text wind;
    @FXML
    private Text precipitation;

    private final String tempSymbol = " \u00b0F";
    private final String windSymbol = " MPH";
    private final String precipSymbol = "%";

    public void update(WeatherCurrentModel model) {
        if (model == null) {
            this.temp.setText("?" + this.tempSymbol);
            this.wind.setText("?" + this.windSymbol);
            this.precipitation.setText("?" + this.precipSymbol);

            this.icon.setContent(WeatherIcon.icon("default"));
        } else {
            this.temp.setText(Math.round(model.temp) + this.tempSymbol);
            this.wind.setText(Math.round(model.wind) + this.windSymbol);
            this.precipitation.setText(Math.round(model.precip * 100) + this.precipSymbol);

            this.icon.setContent(WeatherIcon.icon(model.icon));
        }
    }

    public void updateTime(ZonedDateTime now) {
        String timeFormatted = DateTimeFormatter.ofPattern("h:mm a").format(now);
        this.time.setText(timeFormatted);
    }
}
