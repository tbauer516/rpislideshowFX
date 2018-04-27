package controllers;

import javafx.fxml.FXML;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;

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
    private final String windSymbol = "MPH";
    private final String precipSymbol = "%";

    public void updateDay(String icon, double tempHigh, double tempLow, double wind, double precipitation){
        this.tempHigh.setText(tempHigh + tempSymbol);
        this.tempLow.setText(tempLow + tempSymbol);
        this.wind.setText(wind + windSymbol);
        this.precipitation.setText(precipitation + precipSymbol);

        this.icon.setContent(icon);
    }
}
