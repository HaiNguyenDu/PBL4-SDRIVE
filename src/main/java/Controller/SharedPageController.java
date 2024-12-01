package Controller;

import java.util.ArrayList;

import BLL.Mail_BLL;
import BLL.SSHExample;
import DTO.File_Folder;
import DTO.Mail;
import DTO.Mail;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class SharedPageController extends MainController {
    // Thêm biến cờ
    private volatile boolean isReloading = true;
    private Thread reloadPage;
    @FXML 
    private TableView tableView;
    
    // Thiết lập bảng TableView
    void TableView(ArrayList<Mail> dArrayList) {
        // Save the current selected index
        int selectedIndex = tableView.getSelectionModel().getSelectedIndex();

        // Configure columns if not already added
        if (tableView.getColumns().isEmpty()) {
            TableColumn<Mail, String> item_name = new TableColumn<>("Item Name");
            item_name.setCellValueFactory(new PropertyValueFactory<>("item_name"));
            item_name.setPrefWidth(315);

            TableColumn<Mail, String> username_send = new TableColumn<>("Sender");
            username_send.setCellValueFactory(new PropertyValueFactory<>("username_send"));
            username_send.setPrefWidth(315);

            TableColumn<Mail, String> date = new TableColumn<>("Date");
            date.setCellValueFactory(new PropertyValueFactory<>("date"));
            date.setPrefWidth(315);

            tableView.getColumns().addAll(item_name, date, username_send);
        }

        // Convert dArrayList to ObservableList and set it as the data for TableView
        ObservableList<Mail> observableFileList = FXCollections.observableArrayList(dArrayList);
        tableView.setItems(observableFileList);

        // Restore the previous selection
        if (selectedIndex >= 0 && selectedIndex < observableFileList.size()) {
            tableView.getSelectionModel().select(selectedIndex);
        }

        // Add stylesheet (optional, only if not added before)
        if (tableView.getStylesheets().isEmpty()) {
            // tableView.getStylesheets().add(getClass().getResource("/Styles/homepage.css").toExternalForm());
        }


    }

    ArrayList<Mail> loaddata() throws Exception {
        ArrayList<Mail> dArrayList = Mail_BLL.getSharedItem();
        return dArrayList;
    }

    void LoadPage() {
        while (isReloading) { // Kiểm tra biến cờ
            try {
                Platform.runLater(() -> {
                    try {
                        TableView(loaddata());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                // Luồng bị gián đoạn
                Thread.currentThread().interrupt(); // Đánh dấu lại trạng thái interrupt
                break; // Thoát khỏi vòng lặp
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void stopReloadThread() {
        isReloading = false; // Đặt cờ để dừng vòng lặp
        if (reloadPage != null) {
            reloadPage.interrupt(); // Ngắt luồng
        }
    }

    void startReloadThread() {
        stopReloadThread(); // Đảm bảo luồng cũ đã dừng
        isReloading = true; // Bật cờ
        reloadPage = new Thread(this::LoadPage);
        reloadPage.start();
    }

    public void onClose() {
        System.out.println("Shared Folder Page Closed");
    }

}
