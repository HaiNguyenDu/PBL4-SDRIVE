package Controller;

import BLL.File_handle;
import BLL.Folder_handle;
import BLL.SSHExample;
import BLL.file_folder;
import DAL.ConnectWindowServer;
import DTO.File_Folder;
import DTO.Host;

import com.example.sgroupdrive.HelloApplication;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import javafx.scene.paint.Color; // Dùng JavaFX Color

public class HomePageController {
    // Khai báo các thành phần FXML
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
    public ImageView upLoadeFileIMG = new ImageView();
    public ImageView upLoadFolderIMG = new ImageView();
    public TextField searchField;
    public HBox shareButton;
    public Text addNew;
    public TableView<File_Folder> tableView;
    private Thread reloadPage;

    String Path = "C:\\SDriver\\" + ConnectWindowServer.user;

    // Thêm biến cờ
    private volatile boolean isReloading = true;

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
                Thread.sleep(3000);
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

    ArrayList<File_Folder> loaddata() throws Exception {
        ArrayList<File_Folder> dArrayList = SSHExample.FindFolder(Path);
        return dArrayList;
    }

    public void initialize() {

        initImages();
        textFiled();
        buttonevent();
        addEventAddNewButton();
        addEventRowTableViewPopUp();
        try {
            TableView(loaddata());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    void initImages() {
        Image imageSearch = new Image(getClass().getResourceAsStream("/images/search.png"));
        Image imageDownload = new Image(getClass().getResourceAsStream("/images/download.png"));
        Image imageShare = new Image(getClass().getResourceAsStream("/images/share.png"));
        Image imageOur = new Image(getClass().getResourceAsStream("/images/chung.png"));
        Image imageFile = new Image(getClass().getResourceAsStream("/images/file.png"));
        Image imageNear = new Image(getClass().getResourceAsStream("/images/near.png"));
        Image imageTrash = new Image(getClass().getResourceAsStream("/images/trash.png"));
        Image imageShared = new Image(getClass().getResourceAsStream("/images/shared.png"));
        Image imageUpLoadFile = new Image(getClass().getResourceAsStream("/images/upLoadFile.png"));
        Image imageUpLoadFolder = new Image(getClass().getResourceAsStream("/images/upLoadFolder.png"));
        searchIMG.setImage(imageSearch);
        downloadIMG.setImage(imageDownload);
        shareIMG.setImage(imageShare);
        ourIMG.setImage(imageOur);
        fileIMG.setImage(imageFile);
        nearIMG.setImage(imageNear);
        trashIMG.setImage(imageTrash);
        sharedIMG.setImage(imageShared);
        bodyShareIMG.setImage(imageShare);
        upLoadeFileIMG.setImage(imageUpLoadFile);
        upLoadFolderIMG.setImage(imageUpLoadFolder);
        reloadPage = new Thread(() -> LoadPage());
        reloadPage.start();
    }

    // Method to stop the old thread and start a new reload thread
    void restartReloadThread() {
        stopReloadThread(); // Stop the old thread if it's running

        // Start a new thread to reload the page
        reloadPage = new Thread(() -> LoadPage());
        reloadPage.start();
    }

    // Hiển thị dialog loading và xử lý logic
    private void executeInBackground(String message, Runnable task) {
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

    private void createNewFolder(String folderName) {
        executeInBackground("Creating folder...", () -> {
            Folder_handle.createNewFolder(Path.replace("C:", "\\\\" + Host.dnsServer), folderName);
            Platform.runLater(this::startReloadThread);
        });
    }

    private void createNewFile(String fileName) {
        executeInBackground("Creating file...", () -> {
            File_handle.createNewFile(Path.replace("C:", "\\\\" + Host.dnsServer), fileName);
            Platform.runLater(this::startReloadThread);
        });
    }

    // Thiết lập bảng TableView
    void TableView(ArrayList<File_Folder> dArrayList) {
        // Save the current selected index
        int selectedIndex = tableView.getSelectionModel().getSelectedIndex();

        // Configure columns if not already added
        if (tableView.getColumns().isEmpty()) {
            TableColumn<File_Folder, String> nameColumn = new TableColumn<>("Name");
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
            nameColumn.setPrefWidth(450);

            TableColumn<File_Folder, String> lastWriteTimeColumn = new TableColumn<>("Last Write Time");
            lastWriteTimeColumn.setCellValueFactory(new PropertyValueFactory<>("LastTimeWrite"));
            lastWriteTimeColumn.setPrefWidth(450);

            tableView.getColumns().addAll(nameColumn, lastWriteTimeColumn);
        }

        // Convert dArrayList to ObservableList and set it as the data for TableView
        ObservableList<File_Folder> observableFileList = FXCollections.observableArrayList(dArrayList);
        tableView.setItems(observableFileList);

        // Restore the previous selection
        if (selectedIndex >= 0 && selectedIndex < observableFileList.size()) {
            tableView.getSelectionModel().select(selectedIndex);
        }

        // Add stylesheet (optional, only if not added before)
        if (tableView.getStylesheets().isEmpty()) {
            tableView.getStylesheets().add(getClass().getResource("/Styles/homepage.css").toExternalForm());
        }
    }

    // them cho su kien cho cac button
    void buttonevent() {
        shareButton.setOnMouseClicked(event -> {
            try {
                File_Folder selectedItem = tableView.getSelectionModel().getSelectedItem();
                Stage newStage = new Stage();
                // Nội dung của màn hình mới
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("ShareScreen.fxml"));
                Scene newScene = new Scene(fxmlLoader.load(), 600, 450);
                ShareController shareController = fxmlLoader.getController();
                shareController.setPath(Path + "\\" + selectedItem.getName());
                shareController.setStage(newStage);
                shareController.setItemSelect(selectedItem);
                newStage.setScene(newScene);
                newStage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }

    void addEventRowTableViewPopUp() {
        // Tạo ContextMenu với các MenuItem cho vùng trống
        ContextMenu emptyAreaMenu = new ContextMenu();
        MenuItem newFile = new MenuItem("New File");
        MenuItem newFolder = new MenuItem("New Folder");

        emptyAreaMenu.getItems().addAll(newFile, newFolder);

        // Tạo ContextMenu cho vùng có dòng dữ liệu
        ContextMenu rowMenu = new ContextMenu();
        MenuItem renameItem = new MenuItem("Rename");
        MenuItem deleteItem = new MenuItem("Delete");

        rowMenu.getItems().addAll(renameItem, deleteItem);

        // Thiết lập TableView row factory
        tableView.setRowFactory(tv -> {
            TableRow<File_Folder> row = new TableRow<>();

            row.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.SECONDARY) { // Nếu click chuột phải
                    if (row.isEmpty()) {
                        // Click chuột phải vào vùng trống, hiển thị menu cho New File và New Folder
                        emptyAreaMenu.show(row, event.getScreenX(), event.getScreenY());
                    } else {
                        // Click chuột phải vào dòng có dữ liệu, hiển thị menu cho Rename và Delete
                        rowMenu.show(row, event.getScreenX(), event.getScreenY());
                    }
                }
            });

            return row;
        });

        // Tạo một cửa sổ mới (Stage) để hiển thị màn hình New
        Stage stage = new Stage();

        // Sự kiện cho "New File"
        newFile.setOnAction(event -> {
            // Mở dialog nhập liệu với tiêu đề là "New File"
            showInputDialog("New File");
        });

        // Sự kiện cho "New Folder"
        newFolder.setOnAction(event -> {
            // Mở dialog nhập liệu với tiêu đề là "New Folder"
            showInputDialog("New Folder");
        });

        // Sự kiện cho "Rename"
        renameItem.setOnAction(event -> {
            File_Folder selectedItem = tableView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                // Mở dialog đổi tên với tiêu đề là "Rename"
                showInputDialog("Rename", Path.replace("C:", "\\\\" + Host.dnsServer) + "\\" + selectedItem.getName());
            }
        });

        // Sự kiện cho "Delete"
        deleteItem.setOnAction(event -> {
            File_Folder selectedItem = tableView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                isReloading = false;
                // Xử lý việc xóa file hoặc thư mục đã chọn
                System.out.println("Deleting: " + selectedItem.getName());
                // Ví dụ: tableView.getItems().remove(selectedItem);
                file_folder.deletePath(Path.replace("C:", "\\\\" + Host.dnsServer) + "\\" + selectedItem.getName());
                startReloadThread();
            }
        });
    }

    // Phương thức để hiển thị dialog nhập liệu
    private void showInputDialog(String title, String selectedItem) {
        // Tạo TextInputDialog
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title); // Tiêu đề của dialog là tên MenuItem
        dialog.setHeaderText(null); // Không có header
        if (title.equals("Rename"))
            dialog.setContentText("Enter new name:");
        // Xử lý khi người dùng nhấn OK
        dialog.showAndWait().ifPresent(name -> {
            if (title.equals("Rename")) {
                file_folder.rename(Path.replace("C:", "\\\\" + Host.dnsServer) + "\\" + selectedItem, name);
            }
            // Reload dữ liệu sau khi công việc hoàn tất
            Platform.runLater(this::startReloadThread);
        });
    }

    // Phương thức để hiển thị dialog nhập liệu
    private void showInputDialog(String title) {
        // Tạo TextInputDialog
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title); // Tiêu đề của dialog là tên MenuItem
        dialog.setHeaderText(null); // Không có header
        if (title.equals("New File"))
            dialog.setContentText("Enter file name:"); // Nội dung yêu cầu người dùng nhập
        if (title.equals("New Folder"))
            dialog.setContentText("Enter folder name:");
        // Xử lý khi người dùng nhấn OK
        dialog.showAndWait().ifPresent(name -> {
            System.out.println("đang thực hiện..."); // Lấy giá trị người dùng nhập
            // Bạn có thể xử lý giá trị nhập vào tại đây, ví dụ: tạo file hoặc folder mới,
            // hoặc đổi tên
            isReloading = false;
            if (title.equals("New File"))
                createNewFile(name);
            if (title.equals("New Folder"))
                createNewFolder(name);
        });
    }

    // them su kien cho button add new
    void addEventAddNewButton() {
        Popup popUp = new Popup();
        VBox popUpSub = new VBox(10);
        HBox upLoadFile = new HBox(5);
        HBox upLoadFolder = new HBox(5);
        Text upLoadFileText = new Text();
        Text upLoadFolderText = new Text();

        upLoadeFileIMG.setFitHeight(15);
        upLoadeFileIMG.setPreserveRatio(true);
        upLoadFolderIMG.setFitHeight(15);
        upLoadFolderIMG.setPreserveRatio(true);

        upLoadFileText.setText("Up Load File");
        upLoadFolderText.setText("Up Load Folder");

        upLoadFile.getChildren().addAll(upLoadeFileIMG, upLoadFileText);
        upLoadFolder.getChildren().addAll(upLoadFolderIMG, upLoadFolderText);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(5); // Độ lệch bóng theo chiều X
        dropShadow.setOffsetY(5); // Độ lệch bóng theo chiều Y
        dropShadow.setColor(Color.GRAY); // Màu bóng
        dropShadow.setRadius(10);
        // popUpsub ne
        popUpSub.setStyle("-fx-background-color: white; " + // Màu viền
                "-fx-border-width: 1; " + // Độ dày viền
                "-fx-border-radius: 5; " + // Bo góc viền
                "-fx-background-radius: 5;");
        popUpSub.setPadding(new Insets(10, 10, 20, 10));
        popUpSub.setEffect(dropShadow);
        popUpSub.getChildren().addAll(upLoadFile, upLoadFolder);

        // Popup
        popUp.getContent().add(popUpSub);

        // themsukien cho text Add New
        addNew.setOnMouseClicked(event -> {
            Stage primaryStage = new Stage();
            if (!popUp.isShowing()) {
                double x = addNew.localToScreen(addNew.getLayoutBounds()).getMinX();
                double y = addNew.localToScreen(addNew.getLayoutBounds()).getMinY();
                popUp.show(addNew.getScene().getWindow(), x - 20, y + 30);
            } else {
                popUp.hide();
            }
        });
        // themsukien cho uploadFile
        upLoadFileText.setOnMouseClicked(event -> {
            popUp.hide();
            FileChooser fileChooser = new FileChooser();

            // // Thiết lập kiểu file cho phép chọn
            // fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text
            // Files", "*.txt"));
            // fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image
            // Files", "*.png", "*.jpg", "*.gif"));

            // Mở hộp thoại chọn file và lấy file người dùng chọn
            File getFile = fileChooser.showOpenDialog(addNew.getScene().getWindow());

            if (getFile != null) {
                System.out.println("Đã chọn thư mục: " + getFile.getAbsolutePath());
                File_handle.upLoadFile(getFile.getAbsolutePath(),
                        Path.replace("C:", "\\\\" + Host.dnsServer));
                try {
                    TableView(loaddata());
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

        // sukienclickuploadFolder
        upLoadFolderText.setOnMouseClicked(event -> {
            popUp.hide();
            DirectoryChooser directoryChooser = new DirectoryChooser();

            File selectedDirectory = directoryChooser.showDialog(addNew.getScene().getWindow());

            if (selectedDirectory != null) {
                System.out.println("Đã chọn thư mục: " + selectedDirectory.getAbsolutePath());
                try {
                    Folder_handle.UploadDirectory(selectedDirectory.getAbsolutePath(),
                            Path.replace("C:", "\\\\" + Host.dnsServer));
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                try {
                    TableView(loaddata());
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
    }

    // thanhTimKiem
    void textFiled() {
        searchField = new TextField();
        searchField.setPromptText("Search");
    }
}