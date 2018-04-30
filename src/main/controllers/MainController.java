package controllers;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import manager.GoogleDriveManager;
import network.DarkSkyRequest;
import network.model.WeatherCurrentModel;
import network.model.WeatherDayModel;

import java.awt.*;
import java.io.File;
import java.net.MalformedURLException;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainController {

    @FXML
    private Region pictureFrame;
    @FXML
    private GridPane current;
    @FXML
    private GridPane day1;
    @FXML
    private GridPane day2;
    @FXML
    private GridPane day3;
    @FXML
    private GridPane day4;
    @FXML
    private GridPane day5;
    @FXML
    private GridPane day6;

    private WeatherControllerCurrent currentController;
    private WeatherControllerDay[] dayControllers;
    private ScheduledExecutorService executor;

    public void setGoogleDrive(GoogleDriveManager gdrive) {
        // sync the pictures from drive
        executor.scheduleAtFixedRate(() -> {

            gdrive.sync();

        }, 0, 12, TimeUnit.HOURS);

        // change picture in frame
        executor.scheduleAtFixedRate(() -> {

            File pic = gdrive.getPic();

            if (pic == null)
                return;

            try {
                pictureFrame.setStyle("-fx-background-image: url(" + pic.toURI().toURL() + ");");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

        }, 0, 5, TimeUnit.MINUTES);
    }

    public void setWeatherRequest(DarkSkyRequest darkSky) {
        // update of current time
        executor.scheduleAtFixedRate(() -> {

            currentController.updateTime(ZonedDateTime.now());

        }, 0, 30, TimeUnit.SECONDS);

        // update of current weather
        executor.scheduleAtFixedRate(() -> {

            WeatherCurrentModel info = darkSky.getWeatherCurrent();
            currentController.update(info);

        }, 0, 5, TimeUnit.MINUTES);

        // update of weekly forecast
        executor.scheduleAtFixedRate(() -> {

            WeatherDayModel[] days = darkSky.getWeatherForecast();
            for (int i = 0; i < days.length; i++) {
                dayControllers[i].update(days[i]);
            }

        }, 0, 6, TimeUnit.HOURS);
    }

    public void initialize() {
        executor = Executors.newScheduledThreadPool(4);
        currentController = (WeatherControllerCurrent) current.getProperties().get("control");

        dayControllers = new WeatherControllerDay[6];
        GridPane[] dayVars = new GridPane[] { day1, day2, day3, day4, day5, day6 };
        for (int i = 0; i < dayVars.length; i++) {
            dayControllers[i] = (WeatherControllerDay) dayVars[i].getProperties().get("control");
        }
    }
}
