package Controller;

import BLL.File_handle;
import BLL.Folder_handle;
import BLL.SSHExample;
import BLL.file_folder;
import Component.tableViewMyFile;
import Component.tableViewShared;
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

public class HomePageController extends MainController {
    // Khai báo các thành phần FXML
    @FXML
    public Text username;
    public Text nickName;
    public Text pathText;
    public Text recentButton;
    public Text sharedButton;
    public Text _shareButton;
    public Text generalButton;
    public Text myItemButton;
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
    public VBox viewVBox;
    public Text addNew;
    public TableView<File_Folder> tableView;
    // khoi tao cac man hinh
    // private tableViewMyFile PageMyFile;
    // private tableViewShared PageShared;
    private GeneralPageController generalPageController;

    public String nowPage = "";
    public MainController mainController = this;

    String Path = "C:\\SDriver\\" + ConnectWindowServer.user;
    // Thêm biến cờ
    private volatile boolean isReloading = true;

    // void LoadPage() {
    // while (isReloading) { // Kiểm tra biến cờ
    // try {
    // Platform.runLater(() -> {
    // try {
    // // PageMyFile.PushDataTableView();
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // });
    // Thread.sleep(5000);
    // } catch (InterruptedException e) {
    // // Luồng bị gián đoạn
    // Thread.currentThread().interrupt(); // Đánh dấu lại trạng thái interrupt
    // break; // Thoát khỏi vòng lặp
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // }
    // }

    // void stopReloadThread() {
    // isReloading = false; // Đặt cờ để dừng vòng lặp
    // if (reloadPage != null) {
    // reloadPage.interrupt(); // Ngắt luồng
    // }
    // }

    // void startReloadThread() {
    // stopReloadThread(); // Đảm bảo luồng cũ đã dừng
    // isReloading = true; // Bật cờ
    // reloadPage = new Thread(this::LoadPage);
    // reloadPage.start();
    // }

    public void initialize() {
        initImages();
        textFiled();
        buttonevent();
        addEventAddNewButton();
        initTableView();
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
        // reloadPage = new Thread(() -> LoadPage());
        // reloadPage.start();
    }

    void initTableView() {
        generalPageController = new GeneralPageController(this);
        currenController = generalPageController;
        nowPage = "HomePage";
        tableView = generalPageController.getTableView();
        tableView.setPrefSize(950, 600);
        viewVBox.getChildren().clear();
        viewVBox.getChildren().add(tableView);
    }

    public ArrayList<File_Folder> loadData() throws Exception {
        ArrayList<File_Folder> dArrayList = SSHExample.FindFolder(Path);
        return dArrayList;
    }
    // Method to stop the old thread and start a new reload thread
    // // void restartReloadThread() {
    // stopReloadThread(); // Stop the old thread if it's running

    // // Start a new thread to reload the page
    // reloadPage = new Thread(() -> LoadPage());
    // reloadPage.start();
    // }

    // Thiết lập bảng TableView

    // Them cho su kien cho cac button
    void buttonevent() {
        shareButton.setOnMouseClicked(event -> {
            try {
                File_Folder selectedItem = tableView.getSelectionModel().getSelectedItem();
                if (currenController != null && nowPage == "HomePage") {
                    GeneralPageController child = (GeneralPageController) currenController;
                    child.stopReloadThread();
                    ;
                }
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
                newStage.setOnHidden(e -> {
                    if (currenController != null && nowPage == "HomePage") {
                        GeneralPageController child = (GeneralPageController) currenController;
                        child.startReloadThread();
                    }
                });

                newStage.setScene(newScene);
                newStage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });

        myItemButton.setOnMouseClicked(event -> handleMyItemPage());

        _shareButton.setOnMouseClicked(event -> handleSharePage());

        recentButton.setOnMouseClicked(event -> {
            if (originalContent == null) {
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

        sharedButton.setOnMouseClicked(event -> handleSharedPage());
        generalButton.setOnMouseClicked(event -> {
            if (currenController != null) {
                currenController.onClose();
            }
            generalPageController = new GeneralPageController(this);
            ;
            currenController = generalPageController;
            try {
                generalPageController.setHomePageController(this);

                viewVBox.getChildren().clear();
                viewVBox.getChildren().add(tableView);

            } catch (Exception e) {
                e.printStackTrace();
            }
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

    }

    // thanhTimKiem
    void textFiled() {
        searchField = new TextField();
        searchField.setPromptText("Search");
    }

    private VBox originalContent;

    @FXML
    public void closePage() {
        viewVBox.getChildren().clear();
        viewVBox.getChildren().add(originalContent);
    }

    // MainController xử lý việc điều hướng trang
    private MainController currenController;

    // Phuong thuc chuyen doi trang
    public void switchPage(String path, MainController newController) {
        if (currenController != null) {
            currenController.onClose();
        }
        currenController = newController;
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource(path));
            // VBox newPage = loader.load();
            Parent root = loader.load();
            MainController controller = loader.getController();
            controller.setHomePageController(this);

            // Hiển thị trang mới trong viewVBox
            viewVBox.getChildren().clear();
            viewVBox.getChildren().setAll(root);

            // Gán controller hiện tại
            currenController = controller;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Các sự kiện khi nhấn vào nút trong SideBar
    @FXML
    public void handleSharedPage() {
        nowPage = "SharedPage";
        switchPage("SharedPage.fxml", new SharedPageController());
    }

    @FXML
    public void handleSharePage() {
        nowPage = "SharePage";
        switchPage("SharePage.fxml", new SharePageController());
    }

    @FXML
    public void handleMyItemPage() {
        nowPage = "MyItem";
        switchPage("MyItemPage.fxml", new SharePageController());
    }

    @FXML
    public void handleRecentPage() throws Exception {
        nowPage = "Recent";
        Recent1Controller controller = new Recent1Controller();
        switchPage("Recent1Page.fxml", controller);
        controller.loadData();
    }

    // @FXML
    // public void handleGeneralPage() {
    // nowPage = "HomePage";
    // switchPage("HomePage.fxml", new GeneralPageController(this));
    // }

}