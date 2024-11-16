package Controller;

import com.example.sgroupdrive.HelloApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

public class HomePageController {
    @FXML
    public ImageView searchIMG;
    public ImageView shareIMG;
    public ImageView downloadIMG;
    public ImageView ourIMG;
    public ImageView fileIMG;
    public ImageView nearIMG;
    public ImageView trashIMG;
    public ImageView sharedIMG;
    public ImageView bodyShareIMG;
    public TextField searchField;
    public HBox shareButton;
    public void initialize()
    {
        initImages();
        textFiled();
        buttonevent();
    }
    void initImages()
    {
        Image imageSearch = new Image(getClass().getResourceAsStream("/images/search.png"));
        Image imageDownload = new Image(getClass().getResourceAsStream("/images/download.png"));
        Image imageShare = new Image(getClass().getResourceAsStream("/images/share.png"));
        Image imageOur = new Image(getClass().getResourceAsStream("/images/chung.png"));
        Image imageFile = new Image(getClass().getResourceAsStream("/images/file.png"));
        Image imageNear = new Image(getClass().getResourceAsStream("/images/near.png"));
        Image imageTrash = new Image(getClass().getResourceAsStream("/images/trash.png"));
        Image imageShared = new Image(getClass().getResourceAsStream("/images/shared.png"));
        searchIMG.setImage(imageSearch);
        downloadIMG.setImage(imageDownload);
        shareIMG.setImage(imageShare);
        ourIMG.setImage(imageOur);
        fileIMG.setImage(imageFile);
        nearIMG.setImage(imageNear);
        trashIMG.setImage(imageTrash);
        sharedIMG.setImage(imageShared);
        bodyShareIMG.setImage(imageShare);

    }
    void textFiled()
    {
        searchField =new TextField();
        searchField.setPromptText("Search");
    }
    void buttonevent ()
    {
        shareButton.setOnMouseClicked(event ->{
            try {
                Stage newStage = new Stage();

                // Nội dung của màn hình mới
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("ShareScreen.fxml"));
                Scene newScene = new Scene(fxmlLoader.load(), 600, 450);
                newStage.setScene(newScene);
                newStage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
