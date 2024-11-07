package BLL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneSwitch {
    public void switchToHomePage(Stage stage) throws Exception {
        Parent homePage = FXMLLoader.load(getClass().getResource("\\resources\\com\\example\\sgroupdrive\\HomePage.fxml"));
        Scene homeScene = new Scene(homePage);
        stage.setScene(homeScene);
        stage.show();
    }

    public void switchToSettingsPage(Stage stage) throws Exception {
        Parent settingsPage = FXMLLoader.load(getClass().getResource("SettingsPage.fxml"));
        Scene settingsScene = new Scene(settingsPage);
        stage.setScene(settingsScene);
        stage.show();
    }
}
