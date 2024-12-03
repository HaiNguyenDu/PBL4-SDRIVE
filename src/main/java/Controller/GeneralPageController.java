package Controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.sgroupdrive.HelloApplication;

import BLL.File_handle;
import BLL.Folder_handle;
import BLL.SSHExample;
import BLL.file_folder;
import DAL.ConnectWindowServer;
import DTO.File_Folder;
import DTO.Host;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GeneralPageController extends MainController {

    private Thread reloadPage;

    public void setHomePageController(HomePageController homePageController) {
        this.homePageController = homePageController;

    }

    public void initButtonEvent() {
        homePageController.upLoadFile.setOnMouseClicked(event -> {
            homePageController.popup.hide();
            FileChooser fileChooser = new FileChooser();

            // // Thiết lập kiểu file cho phép chọn
            // fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text
            // Files", "*.txt"));
            // fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image
            // Files", "*.png", "*.jpg", "*.gif"));

            // Mở hộp thoại chọn file và lấy file người dùng chọn
            File getFile = fileChooser.showOpenDialog(homePageController.addNew.getScene().getWindow());

            if (getFile != null) {
                isReloading = false;
                System.out.println("Đã chọn thư mục: " + getFile.getAbsolutePath());
                uploadFile(getFile.getAbsolutePath(), Path.replace("C:", "\\\\" + Host.dnsServer));
            }
        });

        homePageController.upLoadFolder.setOnMouseClicked(event -> {
            homePageController.popup.hide();
            DirectoryChooser directoryChooser = new DirectoryChooser();

            File selectedDirectory = directoryChooser.showDialog(homePageController.addNew.getScene().getWindow());

            if (selectedDirectory != null) {
                System.out.println("Đã chọn thư mục: " + selectedDirectory.getAbsolutePath());
                isReloading = false;
                uploadFolder(selectedDirectory.getAbsolutePath(), Path.replace("C:", "\\\\" + Host.dnsServer));
            }
        });

        this.homePageController.shareButton.setOnMouseClicked(event -> {
            try {
                File_Folder selectedItem = tableViewMyFile.getSelectionModel().getSelectedItem();
                stopReloadThread(); // Dừng luồng reload khi mở ShareScreen

                Stage newStage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("ShareScreen.fxml"));
                Scene newScene = new Scene(fxmlLoader.load(), 600, 450);

                // Thiết lập Controller và truyền dữ liệu cần thiết
                ShareScreenController shareController = fxmlLoader.getController();
                shareController.setPath(Path.replace("C:", "\\\\" + Host.dnsServer) + "\\" + selectedItem.getName());
                shareController.setStage(newStage);
                shareController.setItemSelect(selectedItem);

                // Gắn lắng nghe sự kiện khi cửa sổ ShareScreen đóng
                newStage.setOnHidden(e -> {
                    Platform.runLater(this::startReloadThread);
                });

                newStage.setScene(newScene);
                newStage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        this.homePageController.downloadButton.setOnMouseClicked(event -> {
            try {
                DirectoryChooser directoryChooser = new DirectoryChooser();
                File selectedDirectory = directoryChooser
                        .showDialog(this.homePageController.addNew.getScene().getWindow());
                File_Folder selectedItem = tableViewMyFile.getSelectionModel().getSelectedItem();
                if (selectedDirectory != null && selectedItem != null) {
                    stopReloadThread();
                    if (file_folder
                            .isFile(Path.replace("C:", "\\\\" + Host.dnsServer) + "\\" + selectedItem.getName())) {
                        uploadFile(Path.replace("C:", "\\\\" + Host.dnsServer) + "\\" + selectedItem.getName(),
                                selectedDirectory.getAbsolutePath());
                    } else {
                        uploadFolder(Path.replace("C:", "\\\\" + Host.dnsServer) + "\\" + selectedItem.getName(),
                                selectedDirectory.getAbsolutePath());
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        this.homePageController.shareButton.setOnMouseClicked(event -> {
            try {
                File_Folder selectedItem = tableViewMyFile.getSelectionModel().getSelectedItem();
                stopReloadThread(); // Dừng luồng reload khi mở ShareScreen
                Stage newStage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("ShareScreen.fxml"));
                Scene newScene = new Scene(fxmlLoader.load(), 600, 450);
                // Thiết lập Controller và truyền dữ liệu cần thiết
                ShareScreenController shareController = fxmlLoader.getController();
                shareController.setPath(Path.replace("C:", "\\\\" + Host.dnsServer) + "\\" + selectedItem.getName());
                shareController.setStage(newStage);
                shareController.setItemSelect(selectedItem);

                // Gắn lắng nghe sự kiện khi cửa sổ ShareScreen đóng
                Platform.runLater(this::startReloadThread);

                newStage.setScene(newScene);
                newStage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });

    }

    public void LoadPage() {
        while (isReloading) { // Kiểm tra biến cờ
            if (Thread.interrupted()) {
                break;
            }
            try {

                try {
                    PushDataTableView();
                } catch (Exception e) {
                    e.printStackTrace();
                    isReloading = false; // Dừng luồng nếu có lỗi
                }
                Thread.sleep(6000);
            } catch (Exception e) {
                e.printStackTrace();
                isReloading = false;
            }
        }
    }

    public void stopReloadThread() {
        isReloading = false;
        // if (reloadPage != null && reloadPage.isAlive()) {
        // reloadPage.interrupt();
        // }
    }

    public void startReloadThread() {
        stopReloadThread();
        try {
            if (reloadPage != null) {
                reloadPage.join(); // Đợi luồng cũ dừng hẳn
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        isReloading = true;
        reloadPage = new Thread(this::LoadPage);
        reloadPage.start();
    }

    private void uploadFile(String Path, String pos) {
        ExecuteBackground.executeInBackground("Uploading...", () -> {
            File_handle.upLoadFile(Path, pos);
            Platform.runLater(this::startReloadThread);
        });
    }

    private void uploadFolder(String Path, String pos) {
        ExecuteBackground.executeInBackground("Uploading...", () -> {
            try {
                Folder_handle.UploadDirectory(Path, pos);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Platform.runLater(this::startReloadThread);
        });
    }

    @FXML
    static TableView<File_Folder> tableViewMyFile = new TableView<>();
    HomePageController homePageController;

    List<String> pathView = new ArrayList<>();
    static String Path = "C:\\SDriver\\" + ConnectWindowServer.user;

    public void setPath(String newPath) {
        this.Path = newPath;
    }

    @Override
    public String getPath() {
        return this.Path;
    }

    private volatile boolean isReloading = true;

    public GeneralPageController(HomePageController homePageController) {
        this.homePageController = homePageController;
        try {
            PushDataTableView();
            initButtonEvent();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Platform.runLater(this::startReloadThread);
        addEventRowTableView();
    }

    public TableView<File_Folder> getTableView() {
        return tableViewMyFile;
    }

    public synchronized ArrayList<File_Folder> getFile_Folders() {
        return SSHExample.FindFolder(Path);
    }

    @Override
    public void PushDataTableView() throws Exception {
        System.out.println(Path);
        ArrayList<File_Folder> dArrayList = getFile_Folders();
        int selectedIndex = tableViewMyFile.getSelectionModel().getSelectedIndex();

        // Configure columns if not already added
        if (tableViewMyFile.getColumns().isEmpty()) {
            TableColumn<File_Folder, String> nameColumn = new TableColumn<>("Name");
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
            nameColumn.setPrefWidth(450);

            TableColumn<File_Folder, String> lastWriteTimeColumn = new TableColumn<>("Last Write Time");
            lastWriteTimeColumn.setCellValueFactory(new PropertyValueFactory<>("LastTimeWrite"));
            lastWriteTimeColumn.setPrefWidth(450);

            tableViewMyFile.getColumns().addAll(nameColumn, lastWriteTimeColumn);
        }

        // Convert dArrayList to ObservableList and set it as the data for
        // tableViewShared
        ObservableList<File_Folder> observableFileList = FXCollections.observableArrayList(dArrayList);
        tableViewMyFile.setItems(observableFileList);

        // Restore the previous selection
        if (selectedIndex >= 0 && selectedIndex < observableFileList.size()) {
            tableViewMyFile.getSelectionModel().select(selectedIndex);
        }

        // Add stylesheet (optional, only if not added before)
        if (tableViewMyFile.getStylesheets().isEmpty()) {
            tableViewMyFile.getStylesheets().add(getClass().getResource("/Styles/homepage.css").toExternalForm());
        }
    }

    public void addEventRowTableView() {
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
        tableViewMyFile.setRowFactory(tv -> {
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
                // doubleclick
                if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    File_Folder selectedItem = (File_Folder) tableViewMyFile.getSelectionModel().getSelectedItem();
                    if (file_folder
                            .isFile(Path.replace("C:", "\\\\" + Host.dnsServer) + "\\" + selectedItem.getName())) {

                    } else {

                    }
                }

            });

            return row;
        });
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
            File_Folder selectedItem = tableViewMyFile.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                // Mở dialog đổi tên với tiêu đề là "Rename"
                showInputDialog("Rename", Path.replace("C:", "\\\\" + Host.dnsServer) + "\\" + selectedItem.getName());
            }
        });

        // Sự kiện cho "Delete"
        deleteItem.setOnAction(event -> {
            File_Folder selectedItem = tableViewMyFile.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                isReloading = false;
                // Xử lý việc xóa file hoặc thư mục đã chọn
                System.out.println("Deleting: " + selectedItem.getName());
                Delete(Path.replace("C:", "\\\\" + Host.dnsServer) + "\\" + selectedItem.getName());
            }
        });
    }

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

    private void showInputDialog(String title, String selectedItem) {
        try {
            // Tải FXML và Controller
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("New.fxml"));
            Parent root = loader.load();

            // Lấy controller để truy cập dữ liệu
            NewController controller = loader.getController();

            // Tạo một Stage mới
            Stage dialogStage = new Stage();
            dialogStage.setTitle(title);
            dialogStage.initModality(Modality.APPLICATION_MODAL); // Chặn tương tác với cửa sổ khác
            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait(); // Hiển thị và chờ người dùng tương tác

            // Lấy kết quả từ controller
            String name = controller.getResult();
            if (name != null && title.equals("Rename")) {
                Rename(selectedItem, name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createNewFolder(String folderName) {
        ExecuteBackground.executeInBackground("Creating folder...", () -> {
            Folder_handle.createNewFolder(Path.replace("C:", "\\\\" + Host.dnsServer), folderName);
            Platform.runLater(this::startReloadThread);
        });
    }

    public void createNewFile(String fileName) {
        ExecuteBackground.executeInBackground("Creating file...", () -> {
            File_handle.createNewFile(Path.replace("C:", "\\\\" + Host.dnsServer), fileName);
            Platform.runLater(this::startReloadThread);
        });
    }

    public void Rename(String Path, String fileName) {
        ExecuteBackground.executeInBackground("rename...", () -> {
            file_folder.rename(Path, fileName);
            Platform.runLater(this::startReloadThread);
        });
    }

    public void Delete(String Path) {
        ExecuteBackground.executeInBackground("delete...", () -> {
            file_folder.deletePath(Path);
            Platform.runLater(this::startReloadThread);
        });
    }

    // add double click

    // Method to stop the old thread and start a new reload thread
    // // void restartReloadThread() {
    // stopReloadThread(); // Stop the old thread if it's running

    // // Start a new thread to reload the page
    // reloadPage = new Thread(() -> LoadPage());
    // reloadPage.start();
    // }
    @Override
    public void onClose() {
        System.out.println("General Page Closed");
        stopReloadThread();
    }
}
