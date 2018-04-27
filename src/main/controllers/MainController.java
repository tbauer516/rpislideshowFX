package controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import network.model.WeatherCurrentModel;

public class MainController {

    @FXML
    private GridPane current;
//    @FXML
//    private WeatherControllerDay day1;
//    @FXML
//    private WeatherControllerDay day2;
//    @FXML
//    private WeatherControllerDay day3;
//    @FXML
//    private WeatherControllerDay day4;
//    @FXML
//    private WeatherControllerDay day5;
//    @FXML
//    private WeatherControllerDay day6;

    public void updateCurrent(WeatherCurrentModel model) {
        ((WeatherControllerCurrent) current.getProperties().get("control")).update(model);
    }
}
