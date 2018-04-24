package controllers;

import javafx.fxml.FXML;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;

public class CurrentWeatherController {

    @FXML
    private SVGPath icon;
    @FXML
    private Text temp;
    @FXML
    private Text wind;
    @FXML
    private Text precipitation;

    private final String tempSymbol = " °F";
    private final String windSymbol = "MPH";
    private final String precipSymbol = "%";

    public void updateCurrent(String icon, double temp, double wind, double precipitation){
        this.temp.setText(temp + tempSymbol);
        this.wind.setText(wind + windSymbol);
        this.precipitation.setText(precipitation + precipSymbol);

        this.icon.setContent(icon);
    }
}
