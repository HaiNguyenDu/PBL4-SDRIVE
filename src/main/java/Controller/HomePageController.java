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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.scene.paint.Color; // Dùng JavaFX Color

public class HomePageController {
    // Khai báo các thành phần FXML
    @FXML
    public Text username;
    public Text nickName;
    public Text pathText;
    public HBox pathViewHbox;
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
    public HBox downloadButton;
    public Text addNew;
    public TableView<File_Folder> tableView;
    private Thread reloadPage;
    

    String Path = "C:\\SDriver\\" + ConnectWindowServer.user;

    List<String> pathView = new ArrayList<>();
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
        addEventDoubleCLickRowTableView();
        try {
            TableView(loaddata());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        username.setText(ConnectWindowServer.user);
        nickName.setText(ConnectWindowServer.user.substring(0, 2).toUpperCase());
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

    private void createNewFolder(String folderName) {
        ExecuteBackground.executeInBackground("Creating folder...", () -> {
            Folder_handle.createNewFolder(Path.replace("C:", "\\\\" + Host.dnsServer), folderName);
            Platform.runLater(this::startReloadThread);
        });
    }

    private void createNewFile(String fileName) {
        ExecuteBackground.executeInBackground("Creating file...", () -> {
            File_handle.createNewFile(Path.replace("C:", "\\\\" + Host.dnsServer), fileName);
            Platform.runLater(this::startReloadThread);
        });
    }

    private void Rename(String Path, String fileName) {
        ExecuteBackground.executeInBackground("rename...", () -> {
            file_folder.rename(Path, fileName);
            Platform.runLater(this::startReloadThread);
        });
    }

    private void Delete(String Path) {
        ExecuteBackground.executeInBackground("delete...", () -> {
            file_folder.deletePath(Path);
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
        downloadButton.setOnMouseClicked(event -> {
            try {
                DirectoryChooser directoryChooser = new DirectoryChooser();
                File selectedDirectory = directoryChooser.showDialog(addNew.getScene().getWindow());
                File_Folder selectedItem = tableView.getSelectionModel().getSelectedItem();
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
        shareButton.setOnMouseClicked(event -> {
            try {
                File_Folder selectedItem = tableView.getSelectionModel().getSelectedItem();
                stopReloadThread(); // Dừng luồng reload khi mở ShareScreen

                Stage newStage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("ShareScreen.fxml"));
                Scene newScene = new Scene(fxmlLoader.load(), 600, 450);

                // Thiết lập Controller và truyền dữ liệu cần thiết
                ShareController shareController = fxmlLoader.getController();
                shareController.setPath(Path.replace("C:", "\\\\" + Host.dnsServer) + "\\" + selectedItem.getName());
                shareController.setStage(newStage);
                shareController.setItemSelect(selectedItem);

                // Gắn lắng nghe sự kiện khi cửa sổ ShareScreen đóng
                newStage.setOnHidden(e -> startReloadThread()); // Khởi động lại luồng reload

                newStage.setScene(newScene);
                newStage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        
        );

        recentButton.setOnMouseClicked(event -> {
            if(originalContent == null) {
                originalContent = new VBox(tableView);
            }
            try {
                FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("Recent1Page.fxml"));
                BorderPane recentPage = loader.load();

                Recent1Controller recentController = loader.getController();
                recentController.setHomePageController(this);
                try {
                    recentController.loadData();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                viewVBox.getChildren().clear();
                viewVBox.getChildren().add(recentPage);


                
            } catch (IOException e) { 
                e.printStackTrace();
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
                if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    File_Folder selectedItem = tableView.getSelectionModel().getSelectedItem();
                    if (file_folder
                            .isFile(Path.replace("C:", "\\\\" + Host.dnsServer) + "\\" + selectedItem.getName())) {

                    } else {
                        stopReloadThread();
                        Path = Path + "\\" + selectedItem.getName();
                        Platform.runLater(this::startReloadThread);
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
                Delete(Path.replace("C:", "\\\\" + Host.dnsServer) + "\\" + selectedItem.getName());
            }
        });
    }
    void addEventDoubleCLickRowTableView() {
        tableView.setRowFactory(tv ->{
            TableRow<File_Folder> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2&&!row.isEmpty()) {
                    File_Folder selectedItem = tableView.getSelectionModel().getSelectedItem();
                    pathView.add(selectedItem.getName());
                    Path+="\\" + selectedItem.getName();
                    try {
                        updatePathView();
                        newPath();
                        TableView(loaddata());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }


                }
            });
            return row;
        });
    }
    void newPath()
    {
        String path = "";
        for(String name: pathView)
        {
            path+="\\" + name;
        }
        Path = "C:\\SDriver\\" + ConnectWindowServer.user + path;
    }
    Text textPathView(String name)
    {
        Text newText = new Text(name);
        newText.getStyleClass().add("text-style");
        return newText;
    }
    void updatePathView()
    {
        pathViewHbox.getChildren().clear();
        Text homeText = textPathView("Home > ");
        homeText.setOnMouseClicked(event -> {
            pathView.clear();
            updatePathView();
            newPath();
        });
        pathViewHbox.getChildren().add(homeText);
        int i =0;
        for(String text : pathView)
        {
            int n = i;
            if(n==9)
            {
                Text newText = textPathView("...");
                newText.setOnMouseClicked((MouseEvent event) -> {
                    clickTextPathView(pathView.size()-2);
                    newPath();
                });
                pathViewHbox.getChildren().add(newText);
                break;
            }
            else {
                Text newText = textPathView(text + " > ");
                newText.setOnMouseClicked((MouseEvent event) -> {
                    clickTextPathView(n);
                    newPath();
                });
                pathViewHbox.getChildren().add(newText);
            }
            i++;

        }
    }

    void clickTextPathView(int n)
    {
        List<String> newPath = new ArrayList<>();
        for(int i =0;i<=n;i++)
        {
            newPath.add(pathView.get(i));
        }
        pathView = newPath;
        updatePathView();
    }
    // Phương thức để hiển thị dialog nhập liệu
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
                isReloading = false;
                System.out.println("Đã chọn thư mục: " + getFile.getAbsolutePath());
                uploadFile(getFile.getAbsolutePath(), Path.replace("C:", "\\\\" + Host.dnsServer));
            }
        });

        // sukienclickuploadFolder
        upLoadFolderText.setOnMouseClicked(event -> {
            popUp.hide();
            DirectoryChooser directoryChooser = new DirectoryChooser();

            File selectedDirectory = directoryChooser.showDialog(addNew.getScene().getWindow());

            if (selectedDirectory != null) {
                System.out.println("Đã chọn thư mục: " + selectedDirectory.getAbsolutePath());
                isReloading = false;
                uploadFolder(selectedDirectory.getAbsolutePath(), Path.replace("C:", "\\\\" + Host.dnsServer));
            }
        });
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

    // thanhTimKiem
    void textFiled() {
        searchField = new TextField();
        searchField.setPromptText("Search");
    }

    @FXML
    public VBox viewVBox;

    private VBox originalContent;

    @FXML
    public Text recentButton;

    @FXML
    public void closePage() {
        viewVBox.getChildren().clear();
        viewVBox.getChildren().add(originalContent);
    }

    // Xu ly su kien click nut Recent de dieu huong den trang Recent1Page
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void goToRecent1Page() throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("Recent1Page.fxml"));
            Parent root = loader.load();
            Recent1Controller recent1Controller = loader.getController();
            recent1Controller.loadData();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}