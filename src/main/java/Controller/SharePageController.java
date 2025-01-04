package Controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.sgroupdrive.HelloApplication;

import BLL.File_handle;
import BLL.Folder_handle;
import BLL.Mail_BLL;
import BLL.SSHExample;
import BLL.file_folder;
import DAL.ConnectWindowServer;
import DTO.File_Folder;
import DTO.Host;
import DTO.Mail;
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
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SharePageController extends MainController {
    private Thread reloadPage;
    TableView<Mail> tableView = new TableView<>();
    ArrayList<Mail> dArrayList;

    public void setHomePageController(HomePageController homePageController) {
        this.homePageController = homePageController;
        initButtonEvent();
    }

    public void initButtonEvent() {
        homePageController.upLoadFile.setOnMouseClicked(event -> {
            homePageController.popup.hide();
            FileChooser fileChooser = new FileChooser();
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
                Mail selectedItem = tableView.getSelectionModel().getSelectedItem();
                stopReloadThread(); // Dừng luồng reload khi mở ShareScreen

                Stage newStage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("ShareScreen.fxml"));
                Scene newScene = new Scene(fxmlLoader.load(), 600, 450);

                // Thiết lập Controller và truyền dữ liệu cần thiết
                ShareScreenController shareController = fxmlLoader.getController();
                shareController
                        .setPath(Path.replace("C:", "\\\\" + Host.dnsServer) + "\\" + selectedItem.getItem_name());
                shareController.setStage(newStage);
                shareController.setItemSelect(selectedItem.parseToFile_Floder());

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
                Mail selectedItem = tableView.getSelectionModel().getSelectedItem();
                if (selectedDirectory != null && selectedItem != null) {
                    // stopReloadThread();
                    if (file_folder
                            .isFile(Path.replace("C:", "\\\\" + Host.dnsServer) + "\\" + selectedItem.getItem_name())) {
                        uploadFile(Path.replace("C:", "\\\\" + Host.dnsServer) + "\\" + selectedItem.getItem_name(),
                                selectedDirectory.getAbsolutePath());
                    } else {
                        uploadFolder(Path.replace("C:", "\\\\" + Host.dnsServer) + "\\" + selectedItem.getItem_name(),
                                selectedDirectory.getAbsolutePath());
                    }
                }
            } catch (Exception e) {
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
                Thread.sleep(20000);
            } catch (Exception e) {
                e.printStackTrace();
                isReloading = false;
            }
        }
    }

    public void stopReloadThread() {
        isReloading = false;
        if (reloadPage != null && reloadPage.isAlive()) {
            reloadPage.interrupt();
        }
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

    public SharePageController(HomePageController homePageController) {
        this.homePageController = homePageController;
        dArrayList = Mail_BLL.getShareItem();
        HomePageController.shareList = Mail_BLL.getShareItem();
        try {
            PushDataTableView();
            initButtonEvent();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Platform.runLater(this::startReloadThread);
        addEventRowTableView();
    }

    public TableView<Mail> getTableView() {
        return tableView;
    }

    @Override
    public void PushDataTableView() throws Exception {
        int selectedIndex = tableView.getSelectionModel().getSelectedIndex();

        // Configure columns if not already added
        if (tableView.getColumns().isEmpty()) {
            TableColumn<Mail, String> item_name = new TableColumn<>("Item Name");
            item_name.setCellValueFactory(new PropertyValueFactory<>("item_name"));
            item_name.setPrefWidth(315);

            TableColumn<Mail, String> username_receive = new TableColumn<>("Receiver Name");
            username_receive.setCellValueFactory(new PropertyValueFactory<>("username_receive"));
            username_receive.setPrefWidth(315);

            TableColumn<Mail, String> date = new TableColumn<>("Date");
            date.setCellValueFactory(new PropertyValueFactory<>("date"));
            date.setPrefWidth(315);
            tableView.setPrefHeight(900);
            tableView.getColumns().addAll(item_name, date, username_receive);
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
        tableView.setRowFactory(tv -> {
            TableRow<Mail> row = new TableRow<>();

            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {

                    Mail selectedItem = tableView.getSelectionModel().getSelectedItem();
                    if (selectedItem != null) {
                        try {
                            String isAccess = file_folder
                                    .checkPathAccess("\\\\" + Host.dnsServer + selectedItem.getPath());
                            if (isAccess.equals("success")) {
                                if (file_folder.isFile("\\\\" + Host.dnsServer + selectedItem.getPath())) {
                                    File_handle.openFile("\\\\" + Host.dnsServer + selectedItem.getPath());
                                } else {
                                    this.homePageController.nowPage = "MyItem";
                                    System.out.println("C:" + selectedItem.getPath());
                                    if (MyItemController.reloadPage != null && MyItemController.reloadPage.isAlive()) {
                                        MyItemController.reloadPage.interrupt();
                                    }
                                    this.homePageController.switchPage("MyItemPage.fxml",
                                            new MyItemController(this.homePageController,
                                                    "C:" + selectedItem.getPath()));
                                }

                            } else {
                                Dialog.showAlertDialog("Fail", isAccess);
                            }

                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }

                    }
                }

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
            Mail selectedItem = tableView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                // Mở dialog đổi tên với tiêu đề là "Rename"
                showInputDialog("Rename",
                        Path.replace("C:", "\\\\" + Host.dnsServer) + "\\" + selectedItem.getItem_name());
            }
        });

        // Sự kiện cho "Delete"
        deleteItem.setOnAction(event -> {
            Mail selectedItem = tableView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                isReloading = false;
                // Xử lý việc xóa file hoặc thư mục đã chọn
                System.out.println("Deleting: " + selectedItem.getItem_name());
                Delete(Path.replace("C:", "\\\\" + Host.dnsServer) + "\\" + selectedItem.getItem_name());
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
        // stopReloadThread();
    }
}
