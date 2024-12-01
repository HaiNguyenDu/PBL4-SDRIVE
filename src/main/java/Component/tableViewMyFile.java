package Component;

import java.util.ArrayList;
import java.util.List;

import com.example.sgroupdrive.HelloApplication;

import BLL.File_handle;
import BLL.Folder_handle;
import BLL.SSHExample;
import BLL.file_folder;
import Controller.ExecuteBackground;
import Controller.HomePageController;
import Controller.NewController;
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
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class tableViewMyFile extends tableView{
   
    @FXML
    static TableView<File_Folder> tableViewMyFile = new TableView<>();
    HomePageController homePageController;

    List<String> pathView = new ArrayList<>();
    String Path = "C:\\SDriver\\" + ConnectWindowServer.user;
    private volatile boolean isReloading = true;


    public tableViewMyFile(HomePageController homePageController){
        this.homePageController = homePageController;
        try {
            PushDataTableView();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //them cac su kien
        addEventDoubleCLickRowTableView();
        addEventRowTableView();
    }

    public TableView getTableView() {
        return tableViewMyFile;
    }
     public void PushDataTableView() throws Exception
    {
        ArrayList<File_Folder> dArrayList = SSHExample.FindFolder(Path);
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

        // Convert dArrayList to ObservableList and set it as the data for tableViewShared
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

    void addEventRowTableView(){
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
                //doubleclick
                if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    File_Folder selectedItem = (File_Folder) tableViewMyFile.getSelectionModel().getSelectedItem();
                    if (file_folder.isFile(Path.replace("C:", "\\\\" + Host.dnsServer) + "\\" + selectedItem.getName())) {

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
    private void createNewFolder(String folderName) {
        ExecuteBackground.executeInBackground("Creating folder...", () -> {
            Folder_handle.createNewFolder(Path.replace("C:", "\\\\" + Host.dnsServer), folderName);
            // Platform.runLater(this::startReloadThread);
        });
    }

    private void createNewFile(String fileName) {
        ExecuteBackground.executeInBackground("Creating file...", () -> {
            File_handle.createNewFile(Path.replace("C:", "\\\\" + Host.dnsServer), fileName);
            // Platform.runLater(this::startReloadThread);
        });
    }

    private void Rename(String Path, String fileName) {
        ExecuteBackground.executeInBackground("rename...", () -> {
            file_folder.rename(Path, fileName);
            // Platform.runLater(this::startReloadThread);
        });
    }

    private void Delete(String Path) {
        ExecuteBackground.executeInBackground("delete...", () -> {
            file_folder.deletePath(Path);
            // Platform.runLater(this::startReloadThread);
        });
    }
    //add double click 
    void addEventDoubleCLickRowTableView() {
        tableViewMyFile.setRowFactory(tv ->{
            TableRow<File_Folder> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2&&!row.isEmpty()) {
                    File_Folder selectedItem = tableViewMyFile.getSelectionModel().getSelectedItem();
                    pathView.add(selectedItem.getName());
                    Path+="\\" + selectedItem.getName();
                    try {
                        updatePathView();
                        newPath();
                        PushDataTableView();
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
        homePageController.pathViewHbox.getChildren().clear();
        Text homeText = textPathView("Home > ");
        homeText.setOnMouseClicked(event -> {
            pathView.clear();
            updatePathView();
            newPath();
        });
        homePageController.pathViewHbox.getChildren().add(homeText);
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
                homePageController.pathViewHbox.getChildren().add(newText);
                break;
            }
            else {
                Text newText = textPathView(text + " > ");
                newText.setOnMouseClicked((MouseEvent event) -> {
                    clickTextPathView(n);
                    newPath();
                });
                homePageController.pathViewHbox.getChildren().add(newText);
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


}
