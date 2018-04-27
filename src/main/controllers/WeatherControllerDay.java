package controllers;

import assets.WeatherIcon;
import javafx.fxml.FXML;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;
import network.model.WeatherDayModel;

public class WeatherControllerDay {

    @FXML
    private SVGPath icon;
    @FXML
    private Text tempHigh;
    @FXML
    private Text tempLow;
    @FXML
    private Text wind;
    @FXML
    private Text precipitation;

    private final String tempSymbol = " \u00b0F";
    private final String windSymbol = " MPH";
    private final String precipSymbol = "%";

    public void update(WeatherDayModel model){
        this.tempHigh.setText(Math.round(model.tempHigh) + this.tempSymbol);
        this.tempLow.setText(Math.round(model.tempLow) + this.tempSymbol);
        this.wind.setText(Math.round(model.wind) + this.windSymbol);
        this.precipitation.setText(Math.round(model.precip * 100) + this.precipSymbol);

        this.icon.setContent(WeatherIcon.icon(model.icon));
    }
}
