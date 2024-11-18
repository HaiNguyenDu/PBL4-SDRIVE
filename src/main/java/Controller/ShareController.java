package Controller;

import DAL.File_Folder;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ShareController {
    @FXML
    public ImageView iconFolder;
    public ImageView iconAdmin;
    public ImageView iconUser;
    public ImageView iconShare;
    public ComboBox comboBox0;
    public ComboBox comboBox1;
    public Button add;
    public Text cancel;
    public Text share;

    public Stage stage;
    public String path;
    public File_Folder itemSelect;

    public void setItemSelect(File_Folder itemSelect)
    {
        this.itemSelect = itemSelect;
    }

    public void setStage(Stage stage){
        this.stage=stage;
    }
    public void setPath(String newPath) {
        path = newPath;
        System.out.println(this.path);
    }

    public void initialize() {

        initImage();
        initComboBox();
    }
    //sukien cac button
    public void eventButton(){
        //su kien button add
        add.setOnAction(e->{

        });
        //buttoncancel
        cancel.setOnMouseClicked(e->{
            stage.close();
        });
        //button share
        share.setOnMouseClicked(e->{

        });
    }


    void initImage() {
        Image imageFolder = new Image(getClass().getResourceAsStream("/images/folderyellow.png"));
        Image imageAdmin = new Image(getClass().getResourceAsStream("/images/admin.png"));
        Image imageUser = new Image(getClass().getResourceAsStream("/images/user.png"));
        Image imageShare = new Image(getClass().getResourceAsStream("/images/share.png"));
        iconFolder.setImage(imageFolder);
        iconAdmin.setImage(imageAdmin);
        iconUser.setImage(imageUser);
        iconShare.setImage(imageShare);
    }

    void initComboBox() {
        comboBox0.setStyle("-fx-font-size: 12px;-fx-background-color: transparent;");
        comboBox1.setStyle("-fx-font-size: 12px;-fx-background-color: transparent;");
        comboBox0.getItems().addAll("Read/Write", "Read Only", "No Access");
        comboBox1.getItems().addAll("Read/Write", "Read Only", "No Access");
    }
}
