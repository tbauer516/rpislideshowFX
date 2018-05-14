package info.tylerbauer.rpislideshow;

import info.tylerbauer.rpislideshow.controllers.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import info.tylerbauer.rpislideshow.manager.GoogleDriveManager;
import info.tylerbauer.rpislideshow.network.DarkSkyRequest;
import info.tylerbauer.rpislideshow.network.GoogleDriveRequest;
import info.tylerbauer.rpislideshow.network.GoogleGeo;
import info.tylerbauer.rpislideshow.network.model.GeoLoc;

import javax.net.ssl.SSLEngineResult;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class Main extends Application {

    private Properties props;
    private String configLoc;
    private String propLoc;
    private String picLoc;

    public Main(){
        super();

        Properties props = new Properties();
        try {
            configLoc = System.getProperty("user.dir") + "/" + System.getProperty("configPath");
            String fileLoc = configLoc + "/applicationConfig.properties";
            System.out.println(fileLoc);
            propLoc = fileLoc;
            FileInputStream in = new FileInputStream(fileLoc);
            props.load(in);
            in.close();
        } catch (FileNotFoundException e) {
            System.out.println("File was not found");
            System.exit(1);
        } catch (IOException e) {
            System.out.println("There was an error processing the file");
            System.exit(1);
        }

        this.picLoc = System.getProperty("user.dir") + "/" + "pictures";
        System.out.println(picLoc);

        this.props = props;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + getClass().getPackageName() + "/main.fxml"));
        Parent root = loader.load();
        MainController control = loader.getController();
        System.out.println(control != null);

        primaryStage.setTitle("RPISlideShow");
        primaryStage.setScene(new Scene(root));
        primaryStage.setWidth(1280);
        primaryStage.setHeight(720);
        primaryStage.show();

        String secret = props.getProperty("darksky.secret");
        String lat = props.getProperty("user.lat", null);
        String lng = props.getProperty("user.lng", null);
        String address = props.getProperty("user.address");

        if (lat == null || lng == null) {
            GeoLoc result = GoogleGeo.getLatLng(address);
            lat = result.lat;
            lng = result.lng;
            props.setProperty("user.lat", lat);
            props.setProperty("user.lng", lng);
            props.store(new FileOutputStream(propLoc), "--- Updated on " + DateTimeFormatter.ISO_DATE_TIME.format(ZonedDateTime.now()) + " ---");
        }

        control.setWeatherRequest(new DarkSkyRequest(secret, lat, lng));
        control.setGoogleDrive(
                new GoogleDriveManager(
                        new GoogleDriveRequest(configLoc, picLoc)
                )
        );
    }


    public static void main(String[] args) {
        launch(args);
    }
}
