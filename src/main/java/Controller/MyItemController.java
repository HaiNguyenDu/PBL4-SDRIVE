package Controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.core.appender.rolling.action.IfAccumulatedFileCount;

import com.example.sgroupdrive.HelloApplication;

import BLL.FileAccessTracker;
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
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MyItemController extends MainController {

    public static Thread reloadPage;

    public void setHomePageController(HomePageController homePageController) {
        this.homePageController = homePageController;
        initButtonEvent();
    }

    public void initButtonEvent() {
        homePageController.upLoadFile.setOnMouseClicked(event -> {
            homePageController.popup.hide();
            FileChooser fileChooser = new FileChooser();
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
                // stopReloadThread(); // Dừng luồng reload khi mở ShareScreen

                Stage newStage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("ShareScreen.fxml"));
                Scene newScene = new Scene(fxmlLoader.load(), 600, 450);

                // Thiết lập Controller và truyền dữ liệu cần thiết
                ShareScreenController shareController = fxmlLoader.getController();
                shareController.setPath(Path.replace("C:", "\\\\" + Host.dnsServer) + "\\" + selectedItem.getName());
                shareController.setStage(newStage);
                shareController.setItemSelect(selectedItem);

                // Gắn lắng nghe sự kiện khi cửa sổ ShareScreen đóng
                // newStage.setOnHidden(e -> {
                // Platform.runLater(this::startReloadThread);
                // });

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
                    // stopReloadThread();
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
            if (!MyItemController.originPath.equals("C:\\SDriver\\" + ConnectWindowServer.user)) {
                showAlertDialog("Error", "You can only share files in your own folder");
            } else {
                try {
                    File_Folder selectedItem = tableViewMyFile.getSelectionModel().getSelectedItem();
                    // if (MyItemController.reloadPage != null &&
                    // MyItemController.reloadPage.isAlive()) {
                    // MyItemController.reloadPage.interrupt();
                    // } // Dừng luồng reload khi mở ShareScreen
                    Stage newStage = new Stage();
                    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("ShareScreen.fxml"));
                    Scene newScene = new Scene(fxmlLoader.load(), 600, 450);
                    // Thiết lập Controller và truyền dữ liệu cần thiết
                    ShareScreenController shareController = fxmlLoader.getController();
                    shareController
                            .setPath(Path.replace("C:", "\\\\" + Host.dnsServer) + "\\" + selectedItem.getName());
                    shareController.setStage(newStage);
                    shareController.setItemSelect(selectedItem);

                    // Gắn lắng nghe sự kiện khi cửa sổ ShareScreen đóng
                    // Platform.runLater(this::startReloadThread);

                    newStage.setScene(newScene);
                    newStage.show();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        });

    }

    public void LoadPage() {
        while (!Thread.interrupted()) { // Kiểm tra biến cờ
            if (Thread.interrupted()) {
                break;
            }
            try {

                try {
                    PushDataTableView();
                    Thread.sleep(6000);
                } catch (Exception e) {
                    e.printStackTrace();
                    isReloading = false; // Dừng luồng nếu có lỗi
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                isReloading = false;
                break;
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
        isReloading = true;
        reloadPage = new Thread(this::LoadPage);
        reloadPage.start();
    }

    private void uploadFile(String Path, String pos) {
        ExecuteBackground.executeInBackground("Uploading...", () -> {
            File_handle.upLoadFile(Path, pos);
            try {
                PushDataTableView();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
    }

    private void uploadFolder(String Path, String pos) {
        ExecuteBackground.executeInBackground("Uploading...", () -> {
            try {
                Folder_handle.UploadDirectory(Path, pos);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                PushDataTableView();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
    }

    @FXML
    static TableView<File_Folder> tableViewMyFile = new TableView<>();
    HomePageController homePageController;

    static List<String> pathView = new ArrayList<>();
    static String Path;
    static String originPath;

    public void setDefaultPath() {
        this.Path = "C:\\SDriver\\" + ConnectWindowServer.user;
        pathView.clear();
    }

    public void setPath(String newPath) {
        this.Path = newPath;
    }

    @Override
    public String getPath() {
        return this.Path;
    }

    private volatile boolean isReloading = false;

    public MyItemController(HomePageController homePageController, String Path) {
        System.out.println(Path);
        MyItemController.Path = Path;
        MyItemController.originPath = Path;
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

    @Override
    public void PushDataTableView() throws Exception {
        System.out.println(Path);
        ArrayList<File_Folder> dArrayList = SSHExample.FindFolder(Path);
        int selectedIndex = tableViewMyFile.getSelectionModel().getSelectedIndex();

        // Configure columns if not already added
        if (tableViewMyFile.getColumns().isEmpty()) {
            TableColumn<File_Folder, String> nameColumn = new TableColumn<>("Name");
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
            nameColumn.setPrefWidth(472.5);

            TableColumn<File_Folder, String> lastWriteTimeColumn = new TableColumn<>("Last Write Time");
            lastWriteTimeColumn.setCellValueFactory(new PropertyValueFactory<>("LastTimeWrite"));
            lastWriteTimeColumn.setPrefWidth(472.5);
            tableViewMyFile.setPrefHeight(900);
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
        MenuItem parseItem2 = new MenuItem("Parse");

        emptyAreaMenu.getItems().addAll(newFile, newFolder, parseItem2);

        // Tạo ContextMenu cho vùng có dòng dữ liệu
        ContextMenu rowMenu = new ContextMenu();
        MenuItem renameItem = new MenuItem("Rename");
        MenuItem deleteItem = new MenuItem("Delete");
        MenuItem linkItem = new MenuItem("Link");
        MenuItem copyItem = new MenuItem("Copy");
        MenuItem parseItem = new MenuItem("Parse");
        MenuItem trackerItem = new MenuItem("Tracker");

        if (MyItemController.Path.trim().contains("C:\\SDriver\\" + ConnectWindowServer.user)) {
            rowMenu.getItems().addAll(renameItem, deleteItem, linkItem, copyItem, parseItem, trackerItem);
        } else {
            rowMenu.getItems().addAll(renameItem, deleteItem, linkItem, copyItem, parseItem);
        }

        // Thiết lập TableView row factory
        tableViewMyFile.setRowFactory(tv -> {
            TableRow<File_Folder> row = new TableRow<>();
            tv.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.SECONDARY) { // Nếu click chuột phải
                    File_Folder selectedItem = tableViewMyFile.getSelectionModel().getSelectedItem();
                    if (row.isEmpty() && selectedItem == null) {
                        // Click chuột phải vào vùng trống hoặc khi không có dữ liệu nào trên bảng, hiển
                        // thị menu cho New File và New Folder
                        emptyAreaMenu.show(row, event.getScreenX(), event.getScreenY());
                    }
                }
            });
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {

                    File_Folder selectedItem = tableViewMyFile.getSelectionModel().getSelectedItem();
                    if (file_folder.isFile(getPath().replace("C:", "\\\\" + Host.dnsServer) + "\\"
                            + selectedItem.getName())) {
                        File_handle.openFile(getPath().replace("C:", "\\\\" + Host.dnsServer) + "\\"
                                + selectedItem.getName());
                    } else {
                        pathView.add(selectedItem.getName());
                        try {
                            System.out.println(MyItemController.originPath.replace("C:\\", ""));
                            updatePathView(MyItemController.originPath.replace("C:\\", ""));
                            newPath(MyItemController.originPath.replace("C:\\", ""));
                            ExecuteBackground.executeInBackground("switching...", () -> {
                                try {
                                    PushDataTableView();
                                } catch (Exception e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            });
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                if (event.getButton() == MouseButton.SECONDARY) { // Nếu click chuột phải
                    if (tableViewMyFile.getItems().size() == 0 || row.isEmpty()) {
                        // Click chuột phải vào vùng trống hoặc khi không có dữ liệu nào trên bảng, hiển
                        // thị menu cho New File và New Folder
                        emptyAreaMenu.show(row, event.getScreenX(), event.getScreenY());
                    } else {
                        // Click chuột phải vào dòng có dữ liệu, hiển thị menu cho Rename và Delete
                        rowMenu.show(row, event.getScreenX(), event.getScreenY());
                    }
                }

                if (event.getButton() == MouseButton.PRIMARY && row.isEmpty()) {
                    rowMenu.hide();
                    emptyAreaMenu.hide();
                    tableViewMyFile.getSelectionModel().clearSelection();
                }

            });

            return row;
        });
        Stage stage = new Stage();
        stage.setTitle("New Stage");

        trackerItem.setOnAction(event -> {
            File_Folder selectedItem = tableViewMyFile.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                FileAccessTracker tracker = new FileAccessTracker();
                tracker.beginTracker(Path + "\\" + selectedItem.getName());
                System.out.println("Tracker");
                Stage textStage = new Stage();
                textStage.setTitle(Path + "\\" + selectedItem.getName());

                VBox vbox = new VBox();
                vbox.setPadding(new Insets(10));
                Text dynamicText = new Text();
                vbox.getChildren().add(dynamicText);

                Scene scene = new Scene(vbox, 300, 300);
                textStage.setScene(scene);
                textStage.show();

                Thread read = new Thread(() -> {
                    while (true) {
                        if (Thread.interrupted()) {
                            break;
                        }
                        dynamicText.setText(tracker.ListUser);
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            break;
                        }
                    }
                }); read.start();

                textStage.setOnCloseRequest(closeEvent -> {
                    // Handle the window close event
                    System.out.println("Dynamic Text window closed");
                    // Stop the thread updating the text
                    read.interrupt();
                    tracker.end();
                });
            }
        });

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

        // Sự kiện cho "Link"
        linkItem.setOnAction(event -> {
            File_Folder selectedItem = tableViewMyFile.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                // Mở dialog nhập liệu với tiêu đề là "Link"
                showCopyDialog(Path + "\\" + selectedItem.getName());
            }
        });

        copyItem.setOnAction(event -> {
            File_Folder selectedItem = tableViewMyFile.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                // Mở dialog nhập liệu với tiêu đề là "Copy"
                HomePageController.CopyPath = Path + "\\" + selectedItem.getName();
            }
        });

        parseItem.setOnAction(event -> {
            if (HomePageController.CopyPath.equals("")) {
                showAlertDialog("Error", "You have not copied any file or folder");
            } else if (HomePageController.CopyPath.equals(Path)) {
                showAlertDialog("Error", "You can't parse a file or folder in the same directory");
            } else {
                File_Folder selectedItem = tableViewMyFile.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    // Mở dialog nhập liệu với tiêu đề là "parse"
                    if (file_folder.isFile(HomePageController.CopyPath.replace("C:", "\\\\" + Host.dnsServer))) {
                        ExecuteBackground.executeInBackground("Parsing...", () -> {
                            File_handle.upLoadFile(HomePageController.CopyPath.replace("C:", "\\\\" + Host.dnsServer),
                                    Path.replace("C:", "\\\\" + Host.dnsServer) + "\\" + selectedItem.getName());
                            try {
                                PushDataTableView();
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        });
                    } else {
                        ExecuteBackground.executeInBackground("Parsing...", () -> {
                            try {
                                Folder_handle.UploadDirectory(
                                        HomePageController.CopyPath.replace("C:", "\\\\" + Host.dnsServer),
                                        Path.replace("C:", "\\\\" + Host.dnsServer) + "\\" + selectedItem.getName());
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            try {
                                PushDataTableView();
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        });
                    }
                } else {
                    if (file_folder.isFile(HomePageController.CopyPath.replace("C:", "\\\\" + Host.dnsServer))) {
                        ExecuteBackground.executeInBackground("Parsing...", () -> {
                            File_handle.upLoadFile(HomePageController.CopyPath.replace("C:", "\\\\" + Host.dnsServer),
                                    Path.replace("C:", "\\\\" + Host.dnsServer));
                            try {
                                PushDataTableView();
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        });
                    } else {
                        ExecuteBackground.executeInBackground("Parsing...", () -> {
                            try {
                                Folder_handle.UploadDirectory(
                                        HomePageController.CopyPath.replace("C:", "\\\\" + Host.dnsServer),
                                        Path.replace("C:", "\\\\" + Host.dnsServer));
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            try {
                                PushDataTableView();
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        });
                    }
                }
            }
        });

        parseItem2.setOnAction(event -> {
            if (HomePageController.CopyPath.equals("")) {
                showAlertDialog("Error", "You have not copied any file or folder");
            } else if (HomePageController.CopyPath.equals(Path)) {
                showAlertDialog("Error", "You can't parse a file or folder in the same directory");
            } else {
                File_Folder selectedItem = tableViewMyFile.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    // Mở dialog nhập liệu với tiêu đề là "parse"
                    if (file_folder.isFile(HomePageController.CopyPath.replace("C:", "\\\\" + Host.dnsServer))) {
                        ExecuteBackground.executeInBackground("Parsing...", () -> {
                            File_handle.upLoadFile(HomePageController.CopyPath.replace("C:", "\\\\" + Host.dnsServer),
                                    Path.replace("C:", "\\\\" + Host.dnsServer) + "\\" + selectedItem.getName());
                            try {
                                PushDataTableView();
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        });
                    } else {
                        ExecuteBackground.executeInBackground("Parsing...", () -> {
                            try {
                                Folder_handle.UploadDirectory(
                                        HomePageController.CopyPath.replace("C:", "\\\\" + Host.dnsServer),
                                        Path.replace("C:", "\\\\" + Host.dnsServer) + "\\" + selectedItem.getName());
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            try {
                                PushDataTableView();
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        });
                    }
                } else {
                    if (file_folder.isFile(HomePageController.CopyPath.replace("C:", "\\\\" + Host.dnsServer))) {
                        ExecuteBackground.executeInBackground("Parsing...", () -> {
                            File_handle.upLoadFile(HomePageController.CopyPath.replace("C:", "\\\\" + Host.dnsServer),
                                    Path.replace("C:", "\\\\" + Host.dnsServer));
                            try {
                                PushDataTableView();
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        });
                    } else {
                        ExecuteBackground.executeInBackground("Parsing...", () -> {
                            try {
                                Folder_handle.UploadDirectory(
                                        HomePageController.CopyPath.replace("C:", "\\\\" + Host.dnsServer),
                                        Path.replace("C:", "\\\\" + Host.dnsServer));
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            try {
                                PushDataTableView();
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        });
                    }
                }
            }
        });
    }

    private void showCopyDialog(String content) {
        Platform.runLater(() -> {
            // Create a new stage for the dialog
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Link");

            // Create a VBox to hold the TextField and Button
            VBox vbox = new VBox();
            vbox.setSpacing(10);
            vbox.setPadding(new Insets(10));

            // Create a TextField and set it to read-only
            TextField textField = new TextField(content);
            textField.setEditable(false);

            // Create a Button to copy the content of the TextField
            Button copyButton = new Button("Copy");
            copyButton.setOnAction(event -> {
                Clipboard clipboard = Clipboard.getSystemClipboard();
                ClipboardContent clipboardContent = new ClipboardContent();
                clipboardContent.putString(textField.getText());
                clipboard.setContent(clipboardContent);
            });

            // Add the TextField and Button to the VBox
            vbox.getChildren().addAll(textField, copyButton);

            // Create a Scene with the VBox and set it on the stage
            Scene scene = new Scene(vbox);
            dialogStage.setScene(scene);

            // Show the dialog
            dialogStage.showAndWait();
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

    private void showAlertDialog(String title, String message) {
        Platform.runLater(() -> {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                    javafx.scene.control.Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
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
            try {
                PushDataTableView();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
    }

    public void createNewFile(String fileName) {
        ExecuteBackground.executeInBackground("Creating file...", () -> {
            File_handle.createNewFile(Path.replace("C:", "\\\\" + Host.dnsServer), fileName);
            try {
                PushDataTableView();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
    }

    public void Rename(String Path, String fileName) {
        ExecuteBackground.executeInBackground("rename...", () -> {
            file_folder.rename(Path, fileName);
            
            try {
                PushDataTableView();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
    }

    public void Delete(String Path) {
        ExecuteBackground.executeInBackground("delete...", () -> {
            file_folder.deletePath(Path);
            try {
                PushDataTableView();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
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
        // isReloading = false;
        // try {
        // if (reloadPage != null || reloadPage.isAlive())
        // reloadPage.interrupt();
        // } catch (Exception e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        System.out.println("General Page Closed");
        // stopReloadThread();
    }

    void newPath(String newPath) {
        String path = "";
        for (String name : pathView) {
            path += "\\" + name;
        }
        String finalPath = "C:\\" + newPath + path;
        setPath(finalPath);
    }

    Text textPathView(String name) {
        Text newText = new Text(name);
        newText.setStyle("-fx-fill:#333333;-fx-font-size:14;-fx-font-weight:bold;");
        return newText;
    }

    void updatePathView(String newPath) {
        homePageController.pathViewHbox.getChildren().clear();
        Text homeText = textPathView("Home > ");
        homeText.setOnMouseClicked(event -> {
            pathView.clear();
            updatePathView(newPath);
            newPath(newPath);
            try {
                ExecuteBackground.executeInBackground("Switching...", () -> {
                    try {
                        PushDataTableView();
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                });
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
        homePageController.pathViewHbox.getChildren().add(homeText);
        int i = 0;
        for (String text : pathView) {
            int n = i;
            if (n == 9) {
                Text newText = textPathView("...");
                newText.setOnMouseClicked((MouseEvent event) -> {
                    clickTextPathView(pathView.size() - 2, newPath);
                    newPath(newPath);
                });
                homePageController.pathViewHbox.getChildren().add(newText);
                break;
            } else {
                Text newText = textPathView(text + " > ");
                newText.setOnMouseClicked((MouseEvent event) -> {
                    clickTextPathView(n, newPath);
                    newPath(newPath);
                    try {
                        ExecuteBackground.executeInBackground("Switching...", () -> {
                            try {
                                PushDataTableView();
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        });

                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                });
                homePageController.pathViewHbox.getChildren().add(newText);
            }
            i++;

        }
    }

    void clickTextPathView(int n, String newPath) {
        List<String> newPathView = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            newPathView.add(pathView.get(i));
        }
        pathView = newPathView;
        updatePathView(newPath);
    }
}
