package Controller;

import BLL.File_handle;
import BLL.Folder_handle;
import BLL.MailActivate;
import BLL.Mail_BLL;
import BLL.SSHExample;
import BLL.file_folder;
import Component.tableViewMyFile;
import Component.tableViewShared;
import DAL.ConnectWindowServer;
import DTO.File_Folder;
import DTO.Host;
import DTO.Mail;

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
    public Popup popup;
    public Text username;
    public Text nickName;
    public Text pathText;
    public Text recentButton;
    public Text sharedButton;
    public Text _shareButton;
    public Text generalButton;
    public Text myItemButton;
    public Text countMail;
    public Text searchButton;
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
    public HBox upLoadFile;
    public HBox upLoadFolder;
    public VBox viewVBox;
    public Text addNew;
    public TableView<File_Folder> tableView;
    public List<String> pathView = new ArrayList<>();
    public static ArrayList<Mail> sharedList = new ArrayList<>();
    public static ArrayList<Mail> shareList = new ArrayList<>();
    public static String CopyPath = "";

    public String nowPage = "";
    public MainController mainController;
    public Thread listenMessageThread;

    private MainController currenController;

    String Path = "C:\\SDriver\\" + ConnectWindowServer.user;
    // Thêm biến cờ
    private volatile boolean isReloading = true;
    private volatile boolean isMail = true;

    public void initialize() {
        initImages();
        // textFiled();
        buttonevent();
        addEventAddNewButton();
        initTableView();
        sharedList = Mail_BLL.getSharedItem();
        shareList = Mail_BLL.getShareItem();
        listenMessageThread = new Thread(() -> listenerMessage());
        listenMessageThread.start();
        username.setText(ConnectWindowServer.user);
        nickName.setText(ConnectWindowServer.user.substring(0, 2).toUpperCase());

    }

    public void listenerMessage() {
        while (isMail) {
            System.out.println("lisquen");
            if (MailActivate.isNewMess) {
                System.out.println("New Message");
                ArrayList<String> newMArrayList = new ArrayList<>(MailActivate.newMessages);
                MailActivate.isNewMess = false;
                MailActivate.newMessages.clear();
                System.out.println(newMArrayList.get(0));
                for (String message : newMArrayList) {
                    System.out.println(message);
                    String[] processing = message.split("\\|");
                    System.out.println(processing[0] + processing[1] + processing[2] + processing[3] + processing[4] +
                            Boolean.parseBoolean(processing[5]) + processing[6]);
                    Mail mail = new Mail(processing[0], processing[1], processing[2], processing[3], processing[4],
                            Boolean.parseBoolean(processing[5]), processing[6]);
                    System.out.println(mail.getUsername_send() + mail.getUsername_receive() + mail.getItem_name()
                            + mail.getPath());
                    for (int i = 0; i < sharedList.size(); i++) {
                        if (sharedList.get(i).getUsername_send().equals(mail.getUsername_send())
                                && sharedList.get(i).getUsername_receive().equals(mail.getUsername_receive())
                                && sharedList.get(i).getItem_name().equals(mail.getItem_name())
                                && sharedList.get(i).getPath().equals(mail.getPath())) {
                            sharedList.remove(i);
                            break;
                        }
                    }
                    sharedList.add(mail);
                    System.out.println(message);
                }
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            countUnreadMail();
        }
    }

    void countUnreadMail() {
        int count = 0;
        for (Mail mail : sharedList) {
            if (!mail.getSeen()) {
                count++;
            }
        }
        if (count > 0) {
            countMail.setText(count + "");
        } else {
            countMail.setText("");
        }
    }

    void initImages() {
        Image imageSearch = new Image(getClass().getResourceAsStream("/images/search.png"));
        Image imageDownload = new Image(getClass().getResourceAsStream("/images/download.png"));
        Image imageShare = new Image(getClass().getResourceAsStream("/images/share.png"));
        Image imageOur = new Image(getClass().getResourceAsStream("/images/chung.png"));
        Image imageFile = new Image(getClass().getResourceAsStream("/images/file.png"));
        Image imageNear = new Image(getClass().getResourceAsStream("/images/near.png"));

        Image imageShared = new Image(getClass().getResourceAsStream("/images/shared.png"));
        Image imageUpLoadFile = new Image(getClass().getResourceAsStream("/images/upLoadFile.png"));
        Image imageUpLoadFolder = new Image(getClass().getResourceAsStream("/images/upLoadFolder.png"));
        searchIMG.setImage(imageSearch);
        downloadIMG.setImage(imageDownload);
        shareIMG.setImage(imageShare);
        ourIMG.setImage(imageOur);
        fileIMG.setImage(imageFile);
        nearIMG.setImage(imageNear);
        sharedIMG.setImage(imageShared);
        bodyShareIMG.setImage(imageShare);
        upLoadeFileIMG.setImage(imageUpLoadFile);
        upLoadFolderIMG.setImage(imageUpLoadFolder);
        // reloadPage = new Thread(() -> LoadPage());
        // reloadPage.start();
    }

    void initTableView() {
        if (MyItemController.reloadPage != null && MyItemController.reloadPage.isAlive()) {
            MyItemController.reloadPage.interrupt();
        }
        MyItemController myItemController = new MyItemController(this, "C:\\SDriver\\" + ConnectWindowServer.user);
        currenController = myItemController;
        nowPage = "HomePage";
        tableView = myItemController.getTableView();
        myItemController.addEventRowTableView();
        tableView.setPrefSize(950, 600);
        viewVBox.getChildren().clear();
        viewVBox.getChildren().add(tableView);
    }

    void addEventButton() {

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

        searchButton.setOnMouseClicked(event -> {
            String search = searchField.getText();
            System.out.println(search);
            if (search.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Please enter the search content");
                alert.showAndWait();
            } else {
                try {
                    String isAccess = file_folder.checkPathAccess(search.trim().replace("C:", "\\\\" + Host.dnsServer));
                    if (isAccess.equals("success")) {
                        nowPage = "MyItem";
                        System.out.println("C:" + search);
                        if (MyItemController.reloadPage != null && MyItemController.reloadPage.isAlive()) {
                            MyItemController.reloadPage.interrupt();
                        }
                        switchPage("MyItemPage.fxml",
                                new MyItemController(this, search.trim()));
                    } else {
                        Dialog.showAlertDialog("Fail", isAccess);
                    }

                } catch (Exception e1) {
                    e1.printStackTrace();
                }

            }
        });

        myItemButton.setOnMouseClicked(event -> {
            handleMyItemPage();
            setColorButton(1);
        });

        _shareButton.setOnMouseClicked(event -> {
            handleSharePage();
            setColorButton(2);
        });

        recentButton.setOnMouseClicked(event -> {
            setColorButton(4);
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

        sharedButton.setOnMouseClicked(event -> {
            handleSharedPage(this);
            setColorButton(3);
        });
        generalButton.setOnMouseClicked(event -> {
            setColorButton(0);
            if (currenController != null) {
                currenController.onClose();
            }
            GeneralPageController generalPageController = new GeneralPageController(this);
            currenController = generalPageController;
            try {
                generalPageController.setHomePageController(this);

                viewVBox.getChildren().clear();
                viewVBox.getChildren().add(generalPageController.getTableView());

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // doi mau khi click vao cac route
    void setColorButton(int index) {
        generalButton.setStyle("-fx-fill:#33333;");
        sharedButton.setStyle("-fx-fill:#33333;");
        recentButton.setStyle("-fx-fill:#33333;");
        _shareButton.setStyle("-fx-fill:#33333;");
        myItemButton.setStyle("-fx-fill:#33333;");
        switch (index) {
            case 0:
                generalButton.setStyle("-fx-fill:#06abec;");
                break;
            case 1:
                myItemButton.setStyle("-fx-fill:#06abec;");
                break;
            case 2:
                _shareButton.setStyle("-fx-fill:#06abec;");
                break;
            case 3:
                sharedButton.setStyle("-fx-fill:#06abec;");
                break;
            case 4:
                recentButton.setStyle("-fx-fill:#06abec;");
                break;
        }
    }

    // them su kien cho button add new
    void addEventAddNewButton() {
        popup = new Popup();
        VBox popUpSub = new VBox(10);
        upLoadFile = new HBox(5);
        upLoadFolder = new HBox(5);
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
        popup.getContent().add(popUpSub);

        // themsukien cho text Add New
        addNew.setOnMouseClicked(event -> {
            Stage primaryStage = new Stage();
            if (!popup.isShowing()) {
                double x = addNew.localToScreen(addNew.getLayoutBounds()).getMinX();
                double y = addNew.localToScreen(addNew.getLayoutBounds()).getMinY();
                popup.show(addNew.getScene().getWindow(), x - 20, y + 30);
            } else {
                popup.hide();
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

    // Phuong thuc chuyen doi trang
    public void switchPage(String path, MainController newController) {

        pathViewHbox.getChildren().clear();

        // MyItemController close = new MyItemController(this, "C:\\SDriver\\" +
        // ConnectWindowServer.user);
        // close.setDefaultPath();
        // MyItemController.Path = "C:\\SDriver\\" + ConnectWindowServer.user;
        MyItemController.pathView.clear();

        Text homeText = textPathView("Home / ");
        pathViewHbox.getChildren().add(homeText);
        currenController = newController;
        try {

            tableView = newController.getTableView();
            // Hiển thị trang mới trong viewVBox
            viewVBox.getChildren().clear();
            viewVBox.getChildren().setAll(tableView);

            // Gán controller hiện tại
            currenController = newController;
            pathView.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleSharedPage(HomePageController homePageController) {
        SharedPageController controller = new SharedPageController(this);
        switchPage("SharedPage.fxml", controller);

    }

    @FXML
    public void handleSharePage() {
        switchPage("SharePage.fxml", new SharePageController(this));
    }

    @FXML
    public void handleMyItemPage() {
        nowPage = "MyItem";
        if (MyItemController.reloadPage != null && MyItemController.reloadPage.isAlive()) {
            MyItemController.reloadPage.interrupt();
        }
        switchPage("MyItemPage.fxml", new MyItemController(this, "C:\\SDriver\\" + ConnectWindowServer.user));
    }

    @FXML
    public void handleRecentPage() throws Exception {
        nowPage = "Recent";
        Recent1Controller controller = new Recent1Controller();
        switchPage("Recent1Page.fxml", controller);
        controller.loadData();
    }

    Text textPathView(String name) {
        Text newText = new Text(name);
        newText.setStyle("-fx-fill:#333333;-fx-font-size:14;-fx-font-weight:bold;");
        return newText;
    }

}