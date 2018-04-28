import controllers.MainController;
import controllers.WeatherControllerCurrent;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import network.DarkSkyRequest;
import network.GoogleDriveRequest;
import network.GoogleGeo;
import network.model.GeoLoc;
import network.model.WeatherCurrentModel;

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
    private String propLoc;
    private String picLoc;

    public Main(){
        super();

        Properties props = new Properties();
        try {
            URL propFile = getClass().getResource("config/applicationConfig.properties");
            String fileLoc = propFile.getFile();
            propLoc = fileLoc;
            FileInputStream in = new FileInputStream(fileLoc);
            props.load(in);
            in.close();
        } catch (FileNotFoundException e) {
            System.out.println("File was not found");
        } catch (IOException e) {
            System.out.println("There was an error processing the file");
        }

        this.picLoc = getClass().getResource("pictures/").getPath();

        this.props = props;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        Parent root = loader.load();
        MainController control = loader.getController();

        primaryStage.setTitle("RPISlideShow");
        primaryStage.setScene(new Scene(root));
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
    }


    public static void main(String[] args) {
        launch(args);
    }
}
