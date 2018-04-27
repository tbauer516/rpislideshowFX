package controllers;

import assets.WeatherIcon;
import javafx.fxml.FXML;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;
import network.model.WeatherCurrentModel;

public class WeatherControllerCurrent {

    @FXML
    private SVGPath icon;
    @FXML
    private Text temp;
    @FXML
    private Text wind;
    @FXML
    private Text precipitation;

    private final String tempSymbol = " \u00b0F";
    private final String windSymbol = "MPH";
    private final String precipSymbol = "%";

    public void update(WeatherCurrentModel model) {
        this.temp.setText(model.temp + this.tempSymbol);
        this.wind.setText(model.wind + this.windSymbol);
        this.precipitation.setText(model.precip + this.precipSymbol);

        this.icon.setContent(WeatherIcon.icon(model.icon));
    }
}
