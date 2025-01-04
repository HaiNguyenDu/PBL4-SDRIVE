package Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import com.example.sgroupdrive.HelloApplication;

import BLL.MailActivate;
import BLL.SSHExample;
import DTO.File_Folder;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class LoginController {
    @FXML
    public ImageView imageViewBackgroundLeft;
    public ImageView imageViewBackgroundRight;
    public ImageView imageViewLogo;
    public Rectangle loginButton;
    public Rectangle registerButton;
    public Text loginText;
    public Text registerText;
    public TextField emailField;
    public PasswordField passwordField;
    public Text errorEmail;
    public Text errorPassword;

    public void initialize() {
        initImage();
        eventButton();
        eventField();
    }

    void initImage() {
        Image imageLeft = new Image(getClass().getResourceAsStream("/images/bluecong.png"));
        Image imageRight = new Image(getClass().getResourceAsStream("/images/camcong.png"));
        Image imageLogo = new Image(getClass().getResourceAsStream("/images/Logo.png"));
        imageViewBackgroundLeft.setImage(imageLeft);
        imageViewBackgroundRight.setImage(imageRight);
        imageViewLogo.setImage(imageLogo);
    }

    void eventField() {
        emailField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            String email = emailField.getText().trim();
            String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
            if (!newValue) {
                if (email.isEmpty())
                    errorEmail.setText("Username Khong dc de trong");
                else
                    errorEmail.setText("");
            }
        });
        passwordField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            String password = passwordField.getText().trim();
            if (!newValue) {
                if (password.isEmpty())
                    errorPassword.setText("Password khong dc de trong");
                else if (password.length() < 6)
                    errorPassword.setText("Password khong ngan hon 6 ky tu");
                else
                    errorPassword.setText("");
            }
        });
    }

    void eventButton() {
        // login
        loginText.setOnMouseEntered(event -> loginButton.setFill(Color.web("#4486b3")));
        loginText.setOnMouseExited(event -> loginButton.setFill(Color.WHITE));
        loginText.setOnMouseClicked(event -> {
            @SuppressWarnings("unused")
            String[] error = validate();
            @SuppressWarnings("unused")
            ArrayList<File_Folder> inFolder = new ArrayList<>();
            String email = emailField.getText().trim();
            String password = passwordField.getText().trim();
            String response = SSHExample.setAccount("pbl4.dut.vn", email, password);
            if (response.toLowerCase().equals("success")) {
                System.err.println("success");
                String result = MailActivate.init(email);
                if (result.toLowerCase().equals("success")) {
                    System.err.println("success");

                    Stage newStage = new Stage();
                    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("HomePage.fxml"));
                    try {
                        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                        Scene newScene = new Scene(fxmlLoader.load(), 1260, 720);
                        Stage loginscreen = new Stage();
                        loginscreen = (Stage) loginText.getScene().getWindow();
                        loginscreen.close();
                        newStage.setScene(newScene);
                        newStage.getIcons().add(new javafx.scene.image.Image(
                                Objects.requireNonNull(getClass().getResourceAsStream("/images/n.png"))));
                        newStage.setTitle("Home Page");
                        newStage.show();
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Dialog.showAlertDialog("Login Failed", result);
                }
            } else {
                Dialog.showAlertDialog("Login Failed", response);
            }

        });
        // register
        registerText.setOnMouseEntered(event -> registerButton.setFill(Color.web("#4486b3")));
        registerText.setOnMouseExited(event -> registerButton.setFill(Color.WHITE));
        // enterlogin
        passwordField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().toString().equals("ENTER")) {
                @SuppressWarnings("unused")
                String[] error = validate();
                @SuppressWarnings("unused")
                ArrayList<File_Folder> inFolder = new ArrayList<>();
                String email = emailField.getText().trim();
                String password = passwordField.getText().trim();
                String response = SSHExample.setAccount("pbl4.dut.vn", email, password);
                if (response.toLowerCase().equals("success")) {
                    System.err.println("success");
                    String result = MailActivate.init(email);
                    if (result.toLowerCase().equals("success")) {
                        System.err.println("success");

                        Stage newStage = new Stage();
                        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("HomePage.fxml"));
                        try {
                            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                            Scene newScene = new Scene(fxmlLoader.load(), 1260, 720);
                            Stage loginscreen = new Stage();
                            loginscreen = (Stage) loginText.getScene().getWindow();
                            loginscreen.close();
                            newStage.setScene(newScene);
                            newStage.setTitle("Home Page");

                            newStage.show();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Dialog.showAlertDialog("Login Failed", result);
                    }
                } else {
                    Dialog.showAlertDialog("Login Failed", response);
                }
            }
        });
    }

    String[] validate() {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();
        String[] error = { "", "" };
        // Kiểm tra username
        if (email.isEmpty())
            error[0] = "Username Khong dc de trong";
        if (password.isEmpty())
            error[1] = "Password khong dc de trong";
        else if (password.length() < 6)
            error[1] = "Password khong ngan hon 6 ky tu";

        return error;
    }

}
