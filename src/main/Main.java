import controllers.MainController;
import controllers.WeatherControllerCurrent;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import network.DarkSkyRequest;
import network.GoogleDriveRequest;
import network.model.WeatherCurrentModel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class Main extends Application {

    private DarkSkyRequest weatherRequest;
    private GoogleDriveRequest driveRequest;

    public Main(){
        super();

        Properties props = new Properties();
        try {
            URL propFile = getClass().getResource("config/applicationConfig.properties");
            FileInputStream in = new FileInputStream(propFile.getFile());
            props.load(in);
            in.close();
        } catch (FileNotFoundException e) {
            System.out.println("File was not found");
        } catch (IOException e) {
            System.out.println("There was an error processing the file");
        }

        String secret = props.getProperty("darksky.secret");
        String lat = props.getProperty("user.lat", null);
        String lng = props.getProperty("user.lng", null);
        String address = props.getProperty("user.address");

        weatherRequest = new DarkSkyRequest(secret, lat, lng, address);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        Parent root = loader.load();
        MainController control = loader.getController();

        primaryStage.setTitle("RPISlideShow");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        WeatherCurrentModel model = weatherRequest.getWeatherCurrent();
        System.out.println(model);
        System.out.println(control == null);
        control.updateCurrent(model);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
