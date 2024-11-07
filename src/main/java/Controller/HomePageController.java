package Controller;

import java.util.ArrayList;
import java.util.Arrays;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class HomePageController {

    // Dữ liệu mẫu
    public ArrayList<fileInfor> dArrayList = new ArrayList<>(Arrays.asList(
            new fileInfor("New folder (2)", "11/02/2024 05:09:21 PM"),
            new fileInfor("New folder (3)", "12/02/2024 05:09:21 PM"),
            new fileInfor("New folder", "04/02/2024 05:09:21 PM")
    ));

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
    public TableView<fileInfor> tableView;

    // Hàm khởi tạo
    public void initialize() {
        initImages();
        Filed();
    }

    // Thiết lập hình ảnh cho ImageView
    void initImages() {
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

    // Định nghĩa lớp fileInfor
    public class fileInfor {
        private String Name;
        private String LastTimeWrite;

        // Constructor
        public fileInfor(String name, String LastTW) {
            this.Name = name;
            this.LastTimeWrite = LastTW;
        }

        // Getter và Setter
        public String getName() {
            return this.Name;
        }

        public void setName(String newName) {
            this.Name = newName;
        }

        public String getLastTimeWrite() {
            return this.LastTimeWrite;
        }

        public void setLastTimeWrite(String newLWT) {
            this.LastTimeWrite = newLWT;
        }
    }

    // Thiết lập bảng TableView
    void Filed() {
        // Cấu hình cột cho tên file
        TableColumn<fileInfor, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        nameColumn.setPrefWidth(450);

        // Cấu hình cột cho thời gian chỉnh sửa cuối
        TableColumn<fileInfor, String> lastWriteTimeColumn = new TableColumn<>("Last Write Time");
        lastWriteTimeColumn.setCellValueFactory(new PropertyValueFactory<>("LastTimeWrite"));
        lastWriteTimeColumn.setPrefWidth(450);

        // Thêm cột vào bảng TableView
        tableView.getColumns().addAll(nameColumn, lastWriteTimeColumn);

        // Đặt chế độ chọn nhiều dòng
        tableView.getSelectionModel().setCellSelectionEnabled(false);
        tableView.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.MULTIPLE);

        // Chuyển đổi dArrayList thành ObservableList và đặt làm dữ liệu cho TableView
        ObservableList<fileInfor> observableFileList = FXCollections.observableArrayList(dArrayList);
        tableView.setItems(observableFileList);
        tableView.getStylesheets().add(getClass().getResource("/Styles/homepage.css").toExternalForm());
    }
}
