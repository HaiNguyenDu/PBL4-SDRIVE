package Controller;

import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ExecuteBackground {
    // Hiển thị dialog loading và xử lý logic
    public static void executeInBackground(String message, Runnable task) {
        // Tạo cửa sổ loading
        Stage loadingStage = new Stage();
        loadingStage.setTitle("Loading...");
        ProgressIndicator progressIndicator = new ProgressIndicator();
        Label loadingLabel = new Label(message);
        VBox loadingBox = new VBox(10, progressIndicator, loadingLabel);
        loadingBox.setPadding(new Insets(20));
        loadingBox.setStyle("-fx-alignment: center;");
        Scene loadingScene = new Scene(loadingBox, 200, 100);
        loadingStage.setScene(loadingScene);
        loadingStage.setAlwaysOnTop(true);
        loadingStage.show();

        // Chạy công việc trên một luồng khác
        Task<Void> backgroundTask = new Task<>() {
            @Override
            protected Void call() {
                task.run(); // Thực hiện công việc nặng
                return null;
            }

            @Override
            protected void succeeded() {
                // Đóng cửa sổ loading khi hoàn tất
                loadingStage.close();
            }

            @Override
            protected void failed() {
                // Đóng cửa sổ loading và in lỗi khi thất bại
                loadingStage.close();
                Throwable exception = getException();
                if (exception != null)
                    exception.printStackTrace();
            }
        };

        // Bắt đầu Task
        Thread taskThread = new Thread(backgroundTask);
        taskThread.setDaemon(true); // Đảm bảo Thread tắt khi ứng dụng đóng
        taskThread.start();
    }

}
