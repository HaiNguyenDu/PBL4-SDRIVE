package Controller;

import com.example.sgroupdrive.HelloApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

public class HomePageController {

    // Dữ liệu mẫu
    public ArrayList<File_Folder> dArrayList ;

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
    public TextField searchField;
    public TableView<File_Folder> tableView;
    void loaddata(){
        try {
            this.dArrayList = SSHExample.FindFolder("C:\\Driver\\" +  ConnectWindowServer.user);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    // Hàm khởi tạo

    public HBox shareButton;
    public void initialize()
    {
        loaddata();
        initImages();
        textFiled();
        buttonevent();
        Filed();
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
        searchIMG.setImage(imageSearch);
        downloadIMG.setImage(imageDownload);
        shareIMG.setImage(imageShare);
        ourIMG.setImage(imageOur);
        fileIMG.setImage(imageFile);
        nearIMG.setImage(imageNear);
        trashIMG.setImage(imageTrash);
        sharedIMG.setImage(imageShared);
        bodyShareIMG.setImage(imageShare);
    }

    // Định nghĩa lớp File_Folder
    // public class File_Folder {
    //     private String Name;
    //     private String LastTimeWrite;

    //     // Constructor
    //     public File_Folder(String name, String LastTW) {
    //         this.Name = name;
    //         this.LastTimeWrite = LastTW;
    //     }

    //     // Getter và Setter
    //     public String getName() {
    //         return this.Name;
    //     }

    //     public void setName(String newName) {
    //         this.Name = newName;
    //     }

    //     public String getLastTimeWrite() {
    //         return this.LastTimeWrite;
    //     }

    //     public void setLastTimeWrite(String newLWT) {
    //         this.LastTimeWrite = newLWT;
    //     }
    // }

    // Thiết lập bảng TableView
    void Filed() {
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

        // Đặt chế độ chọn nhiều dòng
        tableView.getSelectionModel().setCellSelectionEnabled(false);
        tableView.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.MULTIPLE);

        // Chuyển đổi dArrayList thành ObservableList và đặt làm dữ liệu cho TableView
        ObservableList<File_Folder> observableFileList = FXCollections.observableArrayList(dArrayList);
        tableView.setItems(observableFileList);
        tableView.getStylesheets().add(getClass().getResource("/Styles/homepage.css").toExternalForm());
    }
    void buttonevent ()
    {
        shareButton.setOnMouseClicked(event ->{
            try {
                Stage newStage = new Stage();

                // Nội dung của màn hình mới
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("ShareScreen.fxml"));
                Scene newScene = new Scene(fxmlLoader.load(), 600, 450);
                newStage.setScene(newScene);
                newStage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
