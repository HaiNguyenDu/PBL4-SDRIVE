package Controller;

import java.util.ArrayList;
// import java.util.Iterator;
import java.util.List;

import BLL.File_handle;
import BLL.Folder_handle;
import BLL.SSHExample;
import BLL.file_folder;
import DAL.ConnectWindowServer;
import DTO.UserAccess;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ShareScreenController extends MainController {
    @FXML
    private ImageView iconFolder;
    @FXML
    private VBox mainVBoxId; // Direct link from FXML
    @FXML
    private Button add;
    @FXML
    private TextField userSearchField;
    @FXML
    private Text cancel;
    @FXML
    private ComboBox<String> userComboBox;
    @FXML
    private Text share;

    private Stage stage;
    private String path;
    private DTO.File_Folder itemSelect;

    private List<UserAccess> oldList;
    private List<UserAccess> newList;

    @FXML
    public void initialize() {
        add.setOnAction(event -> onAddButtonClick());
        share.setOnMouseClicked(event -> onShareButtonClick());
    }

    @Override
    public void PushDataTableView() {
    }

    private void addUser(String user) {
        ExecuteBackground.executeInBackground("Finding...", () -> {
            try {
                // Lấy danh sách người dùng phù hợp với bộ lọc
                @SuppressWarnings("unused")
                ArrayList<String> users = new ArrayList<>();
                if (user.equals("")) {
                    // Cập nhật giao diện trong JavaFX Application Thread
                    javafx.application.Platform.runLater(() -> {
                        userComboBox.getItems().clear();
                        try {
                            userComboBox.getItems().addAll(SSHExample.listDomainUsers());
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        userComboBox.show();
                    });
                } else {
                    // Cập nhật giao diện trong JavaFX Application Thread
                    javafx.application.Platform.runLater(() -> {
                        userComboBox.getItems().clear();
                        try {
                            userComboBox.getItems().addAll(SSHExample.listDomainUsersWithFilter(user));
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        userComboBox.show();
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void onAddButtonClick() {
        // Lấy tên người dùng từ userSearchField
        String newUsername = userSearchField.getText().trim();
        addUser(newUsername);
    }

    // Set selected item for sharing
    public void setItemSelect(DTO.File_Folder itemSelect) {
        this.itemSelect = itemSelect;
    }

    // Set Stage (Window)
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    // Set path to initialize access list
    public void setPath(String newPath) {
        path = newPath;
        System.out.println(this.path);

        // Fetch and remove duplicates from oldList
        oldList = SSHExample.getAccessList(newPath);
        oldList = removeDuplicates(oldList); // Ensure oldList is unique

        newList = new ArrayList<>();
        for (UserAccess userAccess : oldList) {
            newList.add(userAccess.clone());
        }

        // Clear previous components in VBox
        mainVBoxId.getChildren().clear();

        // Log unique entries in oldList
        for (UserAccess userAccess : oldList) {
            System.out.println(userAccess.toString());
        }

        // Create HBox for each UserAccess in newList
        for (UserAccess userAccess : newList) {
            HBox userBox = createUserAccessBox(userAccess);
            mainVBoxId.getChildren().add(userBox);
        }

        initImages();

        // Thêm sự kiện double-click cho ComboBox trong đây
        userComboBox.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) { // Kiểm tra double-click
                // Lấy mục được chọn trong ComboBox
                String selectedUser = (String) userComboBox.getSelectionModel().getSelectedItem();
                if (selectedUser != null) {
                    // Thực hiện hành động khi chọn user
                    System.out.println("User selected: " + selectedUser);
                    // add vào danh sách quyền
                    addNewUserAccess(selectedUser.split("\\s+")[0]);

                }
            }
        });
    }

    private void addNewUserAccess(String Username) {
        ExecuteBackground.executeInBackground("Adding...", () -> {
            // Kiểm tra nếu người dùng đã tồn tại trong danh sách
            String newUsername = "PBL4\\" + Username;
            System.out.println(newUsername);
            boolean userExists = false;
            for (UserAccess userAccess : newList) {
                if (userAccess.getUsername().equals(newUsername)) {
                    userExists = true;
                    break;
                }
            }
            if (userExists) {
                System.out.println("Người dùng đã tồn tại trong danh sách.");
                return;
            }

            // Tạo một UserAccess mới với quyền mặc định là "Readonly"
            UserAccess newUser = new UserAccess(newUsername, "Readonly");
            newList.add(newUser);

            // Cập nhật giao diện trong JavaFX Application Thread
            Platform.runLater(() -> {
                HBox userBox = createUserAccessBox(newUser);
                mainVBoxId.getChildren().add(userBox);
            });

        });
    }

    // Helper method to remove duplicates by username
    private List<UserAccess> removeDuplicates(List<UserAccess> list) {
        List<UserAccess> uniqueList = new ArrayList<>();
        for (UserAccess userAccess : list) {
            if (uniqueList.stream().noneMatch(u -> u.getUsername().equals(userAccess.getUsername()))) {
                uniqueList.add(userAccess);
            }
        }
        return uniqueList;
    }

    private void printList() {
        System.out.println("");
        for (UserAccess userAccess : newList) {
            System.out.println(userAccess.toString());
        }
    }

    private HBox createUserAccessBox(UserAccess userAccess) {
        // Main HBox
        HBox mainHBox = new HBox();
        mainHBox.setPrefHeight(30);
        mainHBox.setPrefWidth(450);

        // Left HBox for icon and username
        HBox leftBox = new HBox();
        leftBox.setPrefWidth(380);
        leftBox.setSpacing(10);

        // User icon
        ImageView userIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/user.png")));
        userIcon.setFitHeight(15);
        userIcon.setPreserveRatio(true);

        // Username text
        Text userName = new Text(userAccess.getUsername());
        HBox.setMargin(userName, new Insets(0, 0, 0, 10)); // Add left margin to the username

        leftBox.getChildren().addAll(userIcon, userName);

        // ComboBox for selecting access level
        ComboBox<String> accessComboBox = new ComboBox<>();
        accessComboBox.setPrefHeight(20);
        accessComboBox.setPrefWidth(100);
        accessComboBox.setStyle("-fx-background-color: transparent;");

        if (userAccess.getUsername().equals("PBL4\\" + ConnectWindowServer.user)
                || userAccess.getUsername().equals("PBL4\\Administrator"))
            accessComboBox.getItems().addAll("Fullcontrol");
        else {
            accessComboBox.getItems().addAll("Read/Write", "Readonly", "Deny");
        }

        accessComboBox.setValue(userAccess.getAccess()); // Set current access level as default
        // Add listener to ComboBox to update the access level in UserAccess
        accessComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            userAccess.setAccess(newVal);
            printList();
        });

        // Add components to the main HBox
        mainHBox.getChildren().addAll(leftBox, accessComboBox);

        return mainHBox;
    }

    private boolean check(UserAccess access) {
        for (UserAccess oldAccess : oldList) {
            // So sánh cả username và access
            if (oldAccess.getUsername().equals(access.getUsername()) &&
                    oldAccess.getAccess().equals(access.getAccess())) {
                if (!oldAccess.isIh()) {
                    return false;
                } else
                    return true;
                // Trùng cả username và access
            }
        }
        return false; // Không trùng
    }

    @FXML
    private void onShareButtonClick() {
        ExecuteBackground.executeInBackground("Sharing...", () -> {
            try {
                System.out.println("///////////////////////////////////////////////////////");
                System.out.println("");
                for (UserAccess userAccess : oldList) {
                    System.out.println(userAccess.toString());
                }
                System.out.println("///////////////////////////////////////////////////////");
                printList();
                System.out.println("///////////////////////////////////////////////////////");
                // Tạo danh sách tạm để lưu các phần tử cần giữ lại
                List<UserAccess> tempList = new ArrayList<>();
                for (UserAccess i : newList) {
                    // Nếu không trùng với phần tử nào trong oldList thì giữ lại
                    if (!check(i) && !i.getAccess().equals("Fullcontrol")) {
                        System.out.println("Giữ lại: " + i.getUsername() + " " + i.getAccess());
                        tempList.add(i);
                    } else {
                        System.out.println("Loại bỏ: " + i.getUsername() + " " + i.getAccess());
                    }
                }
                // Gán danh sách mới
                newList.clear();
                newList.addAll(tempList);
                // Update the access permissions based on newList
                printList();
                if (file_folder.isFile(path)) {
                    updateAccessPermissionsForFile();
                } else
                    updateAccessPermissionsForFolder();
                // Optionally, provide feedback to the user
                Platform.runLater(() -> {
                    System.out.println("Sharing completed successfully!");
                    stage.close(); // Close the window after sharing
                });
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> {
                    System.out.println("An error occurred while sharing: " + e.getMessage());
                });
            }
        });
    }

    // Update access permissions based on the newList
    private void updateAccessPermissionsForFolder() {
        // Logic to update permissions for all users in newList
        for (UserAccess userAccess : newList) {
            System.out.println(userAccess.getUsername() + ":" + userAccess.getAccess());
            switch (userAccess.getAccess()) {
                case "Read/Write":
                    Folder_handle.modifyUserPermissions(path, userAccess.getUsername().replace("PBL4\\", ""), "M");
                    break;
                case "Readonly":
                    Folder_handle.modifyUserPermissions(path, userAccess.getUsername().replace("PBL4\\", ""), "R");
                    break;
                case "Deny":
                    Folder_handle.modifyUserPermissions(path, userAccess.getUsername().replace("PBL4\\", ""), "D");
                    break;
                default:
                    break;
            }
            // Call your method to update access (e.g., SSHExample.updateUserAccess(path,
            // userAccess.getUsername(), userAccess.getAccess()));
        }
        System.out.println("Permissions updated successfully!");
    }

    // Update access permissions based on the newList
    private void updateAccessPermissionsForFile() {
        // Logic to update permissions for all users in newList
        for (UserAccess userAccess : newList) {
            switch (userAccess.getAccess()) {
                case "Read/Write":
                    File_handle.modifyUserFilePermissions(path, userAccess.getUsername().replace("PBL4\\", ""), "M");
                    break;
                case "Readonly":
                    File_handle.modifyUserFilePermissions(path, userAccess.getUsername().replace("PBL4\\", ""), "R");
                    break;
                case "Deny":
                    File_handle.modifyUserFilePermissions(path, userAccess.getUsername().replace("PBL4\\", ""), "D");
                    break;
                default:
                    break;
            }
            // Call your method to update access (e.g., SSHExample.updateUserAccess(path,
            // userAccess.getUsername(), userAccess.getAccess()));
        }
    }

    // Initialize folder icon
    private void initImages() {
        iconFolder.setImage(new Image(getClass().getResourceAsStream("/images/folderyellow.png")));
    }

    public void onClose() {
        System.out.println("Share Page Closed");
    }
}
