package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class NewController {
    @FXML
    private TextField input;

    @FXML
    private Button create;

    @FXML
    private Button cancel;

    private String result;

    @FXML
    public void initialize() {
        // Xử lý sự kiện nút Create
        create.setOnAction(event -> {
            result = input.getText(); // Lấy giá trị người dùng nhập
            closeWindow(); // Đóng dialog
        });

        // Xử lý sự kiện nút Cancel
        cancel.setOnAction(event -> {
            result = null; // Đặt kết quả thành null nếu hủy
            closeWindow(); // Đóng dialog
        });
    }

    public String getResult() {
        return result;
    }

    private void closeWindow() {
        Stage stage = (Stage) create.getScene().getWindow();
        stage.close();
    }
}
