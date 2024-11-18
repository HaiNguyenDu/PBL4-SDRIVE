package Controller;

import BLL.SSHExample;
import DAL.ConnectWindowServer;
import DAL.File_Folder;

import com.example.sgroupdrive.HelloApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.paint.Color;  // Dùng JavaFX Color


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
    public ImageView upLoadFolderIMG= new ImageView();
    public TextField searchField;
    public HBox shareButton;
    public Text addNew ;
    public TableView<File_Folder> tableView;

    ArrayList<File_Folder> loaddata() {
           ArrayList<File_Folder> dArrayList = new ArrayList<>(Arrays.asList(
                    new File_Folder("New folder (2)", "11/02/2024 05:09:21 PM"),
                    new File_Folder("New folder (3)", "12/02/2024 05:09:21 PM"),
                    new File_Folder("New folder", "04/02/2024 05:09:21 PM")));
            return dArrayList;
    }



    public void initialize()
    {

        initImages();
        textFiled();
        buttonevent();
        TableView(loaddata());
        addEventAddNewButton();
        addEventRowTableViewPopUp();
    }
    void initImages()
    {
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
    }
//     Thiết lập bảng TableView
    void TableView(ArrayList<File_Folder> dArrayList) {
    // Cấu hình cột cho tên file
    TableColumn<File_Folder, String> nameColumn = new TableColumn<>("Name");
    nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
    nameColumn.setPrefWidth(450);

        // Cấu hình cột cho thời gian chỉnh sửa cuối
        TableColumn<File_Folder, String> lastWriteTimeColumn = new TableColumn<>("Last Write Time");
        lastWriteTimeColumn.setCellValueFactory(new PropertyValueFactory<>("LastTimeWrite"));
        lastWriteTimeColumn.setPrefWidth(450);

        // Thêm cột vào bảng TableView
        tableView.getColumns().addAll(nameColumn, lastWriteTimeColumn);

        // Chuyển đổi dArrayList thành ObservableList và đặt làm dữ liệu cho TableView
        List<File_Folder> fileList = new ArrayList<>(dArrayList);
        ObservableList<File_Folder> observableFileList = FXCollections.observableArrayList(fileList);
        tableView.setItems(observableFileList);

        // Thêm stylesheet
        tableView.getStylesheets().add(getClass().getResource("/Styles/homepage.css").toExternalForm());

        // RowFactory không cần thiết trong trường hợp chỉ đổi màu khi chọn.
    }
    //

    //them cho su kien cho cac button
    void buttonevent ()
    {
        shareButton.setOnMouseClicked(event ->{
            try {
                File_Folder selectedItem = tableView.getSelectionModel().getSelectedItem();
                Stage newStage = new Stage();
                // Nội dung của màn hình mới
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("ShareScreen.fxml"));
                Scene newScene = new Scene(fxmlLoader.load(), 600, 450);
                ShareController shareController = fxmlLoader.getController();
                shareController.setPath("path");
                shareController.setStage(newStage);
                shareController.setItemSelect(selectedItem);
                newStage.setScene(newScene);
                newStage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });


    }
    //chuot phai vao cac dong
    void addEventRowTableViewPopUp(){
        ContextMenu contextMenu = new ContextMenu();
        MenuItem newFile = new MenuItem("New File");
        MenuItem deleteItem = new MenuItem("Delete");
        MenuItem newFolder = new MenuItem("New Folder");

        contextMenu.getItems().addAll(newFile, newFolder,deleteItem);
        tableView.setRowFactory(tv -> {
            TableRow<File_Folder> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.SECONDARY && !row.isEmpty()) {
                    contextMenu.show(row, event.getScreenX(), event.getScreenY());
                }
            });
            return row;
        });

        Stage stage = new Stage();

        //truyen type sang man hinh New la File roi qua ben Newcoontroller kia ma xu ly
        newFile.setOnAction(event -> {

            try {
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("New.fxml"));
                Scene scene = new Scene(fxmlLoader.load(),400,150);
                stage.setScene(scene);
                NewController newController = fxmlLoader.getController();
                newController.setType("File");
                newController.setState(stage);
                newController.setHomePageController(this);
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });

        deleteItem.setOnAction(event -> {
            File_Folder selectedItem = tableView.getSelectionModel().getSelectedItem();
            //them lenh xoa vao day
        });



        //truyen type sang man hinh New la Folder roi qua ben Newcoontroller kia ma xu ly
        newFolder.setOnAction(event -> {


            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("New.fxml"));
            try {
                Scene scene = new Scene(fxmlLoader.load(),400,150);
                stage.setScene(scene);
                NewController newController = fxmlLoader.getController();
                newController.setType("Folder");
                newController.setState(stage);
                newController.setHomePageController(this);
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });

    }
    //them su kien cho button add new
    void addEventAddNewButton()
    {
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

        upLoadFile.getChildren().addAll(upLoadeFileIMG,upLoadFileText);
        upLoadFolder.getChildren().addAll(upLoadFolderIMG,upLoadFolderText);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(5);  // Độ lệch bóng theo chiều X
        dropShadow.setOffsetY(5);  // Độ lệch bóng theo chiều Y
        dropShadow.setColor(Color.GRAY);  // Màu bóng
        dropShadow.setRadius(10);
        //popUpsub ne
        popUpSub.setStyle("-fx-background-color: white; " +     // Màu viền
                "-fx-border-width: 1; " +         // Độ dày viền
                "-fx-border-radius: 5; " +        // Bo góc viền
                "-fx-background-radius: 5;");
        popUpSub.setPadding(new Insets(10, 10, 20, 10));
        popUpSub.setEffect(dropShadow);
        popUpSub.getChildren().addAll(upLoadFile,upLoadFolder);


        //Popup
        popUp.getContent().add(popUpSub);

        //themsukien cho text Add New
        addNew.setOnMouseClicked(event ->{
            Stage primaryStage = new Stage();
            if (!popUp.isShowing()) {
                double x = addNew.localToScreen(addNew.getLayoutBounds()).getMinX();
                double y = addNew.localToScreen(addNew.getLayoutBounds()).getMinY();
                popUp.show(addNew.getScene().getWindow(), x-20, y+ 30 );
            } else {
                popUp.hide();
            }
        });
        //themsukien cho uploadFile
        upLoadFileText.setOnMouseClicked(event->{
            popUp.hide();
            FileChooser fileChooser = new FileChooser();

            // Thiết lập kiểu file cho phép chọn
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));

            // Mở hộp thoại chọn file và lấy file người dùng chọn
            File getFile = fileChooser.showOpenDialog(addNew.getScene().getWindow());

            if (getFile != null) {
                System.out.println("Đã chọn thư mục: " + getFile.getAbsolutePath());
            }
        });

        //sukienclickuploadFolder
        upLoadFolderText.setOnMouseClicked(event->{
            popUp.hide();
            DirectoryChooser directoryChooser = new DirectoryChooser();

            File selectedDirectory = directoryChooser.showDialog(addNew.getScene().getWindow());

            if (selectedDirectory != null) {
                System.out.println("Đã chọn thư mục: " + selectedDirectory.getAbsolutePath());
            }
        });
    }
    //thanhTimKiem
    void textFiled() {
        searchField = new TextField();
        searchField.setPromptText("Search");
    }
}