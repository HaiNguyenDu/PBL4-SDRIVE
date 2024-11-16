package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ShareController
{
    @FXML
    public ImageView iconFolder;
    public ImageView iconAdmin;
    public ImageView iconUser;
    public ImageView iconShare;
    public ComboBox comboBox0;
    public ComboBox comboBox1;

    public void initialize() {

        initImage();
        initComboBox();
    }

    void initImage()
    {
        Image imageFolder = new Image(getClass().getResourceAsStream("/images/folderyellow.png"));
        Image imageAdmin = new Image(getClass().getResourceAsStream("/images/admin.png"));
        Image imageUser = new Image(getClass().getResourceAsStream("/images/user.png"));
        Image imageShare = new Image(getClass().getResourceAsStream("/images/share.png"));
        iconFolder.setImage(imageFolder);
        iconAdmin.setImage(imageAdmin);
        iconUser.setImage(imageUser);
        iconShare.setImage(imageShare);
    }

    void initComboBox()
    {
        comboBox0.setStyle("-fx-font-size: 12px;-fx-background-color: transparent;");
        comboBox1.setStyle("-fx-font-size: 12px;-fx-background-color: transparent;");
        comboBox0.getItems().addAll("Read/Write", "Read Only", "No Access");
        comboBox1.getItems().addAll("Read/Write", "Read Only", "No Access");
    }
}
