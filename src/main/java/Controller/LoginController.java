package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

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
    void initImage()
    {
        Image imageLeft = new Image(getClass().getResourceAsStream("/images/bluecong.png"));
        Image imageRight = new Image(getClass().getResourceAsStream("/images/camcong.png"));
        Image imageLogo = new Image(getClass().getResourceAsStream("/images/Logo.png"));
        imageViewBackgroundLeft.setImage(imageLeft);
        imageViewBackgroundRight.setImage(imageRight);
        imageViewLogo.setImage(imageLogo);
    }
    void eventField()
    {
        emailField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            String email = emailField.getText().trim();
            String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
            if (!newValue) {
                if (email.isEmpty()) errorEmail.setText( "Email Khong dc de trong");
                else if (!email.matches(emailRegex)) errorEmail.setText( " Sai ding dang email");
                else errorEmail.setText("");
            }
        });
        passwordField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            String password = passwordField.getText().trim();
            if (!newValue) {
                if (password.isEmpty()) errorPassword.setText("Password khong dc de trong");
                else if (password.length() < 6) errorPassword.setText("Password khong ngan hon 6 ky tu");
                else errorPassword.setText("");
            }
        });
    }
    void eventButton()
    {
        //login
        loginText.setOnMouseEntered(event ->   loginButton.setFill(Color.web("#4486b3")));
        loginText.setOnMouseExited(event ->   loginButton.setFill(Color.WHITE));
        loginText.setOnMouseClicked(event -> {
            String[] error = validate();
            if(error[0]!="") errorEmail.setText(error[0]);
            if(error[1]!="") errorPassword.setText(error[1]);
        });
        //register
        registerText.setOnMouseEntered(event ->   registerButton.setFill(Color.web("#4486b3")));
        registerText.setOnMouseExited(event ->   registerButton.setFill(Color.WHITE));
    }
    String[] validate(){
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        String[] error = {"",""};
        // Kiểm tra username
        if (email.isEmpty()) error[0]= "Email Khong dc de trong";
        else if (!email.matches(emailRegex)) error[0]= "Sai ding dang email";
        if (password.isEmpty()) error[1]= "Password khong dc de trong";
        else if (password.length() < 6) error[1]= "Password khong ngan hon 6 ky tu";

        return error;
    }

}
