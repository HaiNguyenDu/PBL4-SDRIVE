package Controller;

import java.beans.EventHandler;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Random;

import BLL.File_handle;
import BLL.Mail_BLL;
import BLL.file_folder;
import DAL.ConnectWindowServer;
import DAL.Mail_DAL;
import DTO.Host;
import DTO.Mail;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class Recent1Controller extends MainController {

    @FXML
    private VBox vboxContainer;

    @FXML
    private VBox vboxMail_HeaderDetail;

    @FXML
    private HBox hboxInvited;

    @FXML
    private VBox vboxInvited;

    @FXML
    public void loadData() throws Exception {
        try {
            ArrayList<Mail> list = HomePageController.sharedList;
            for (int i = 0; i < list.size(); i++) {
                System.out.println();
                String username_send = list.get(i).getUsername_send();
                String username_receive = list.get(i).getUsername_receive();
                String email = list.get(i).getUsername_send() + "@pbl4.dut.vn";
                String date = list.get(i).getDate();
                String item_name = list.get(i).getItem_name();
                String path = list.get(i).getPath();
                boolean read = list.get(i).getSeen();
                HBox row = createHBoxForRow(username_send, username_receive, email, date, item_name, path, read);
                vboxContainer.getChildren().add(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadDataAfterSetSeen() throws Exception {
        HomePageController.sharedList = Mail_BLL.getSharedItem();
        ArrayList<Mail> list = HomePageController.sharedList;
        vboxContainer.getChildren().clear();
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.size());
            String username_send = list.get(i).getUsername_send();
            String username_receive = list.get(i).getUsername_receive();
            String email = list.get(i).getUsername_send() + "@pbl4.dut.vn";
            String date = list.get(i).getDate();
            String item_name = list.get(i).getItem_name();
            String path = list.get(i).getPath();
            boolean read = list.get(i).getSeen();

            HBox row = createHBoxForRow(username_send, username_receive, email, date, item_name, path, read);
            vboxContainer.getChildren().add(row);
        }
    }

    @Override
    public void PushDataTableView() throws Exception {
    }

    private HomePageController homePageController;

    public void setHomePageController(HomePageController homePageController) {
        this.homePageController = homePageController;
    }

    @SuppressWarnings("static-access")
    private HBox createHBoxForRow(String username_send, String username_receive, String email, String date,
            String item_name, String path, boolean read) {
        HBox hbox = new HBox(10);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setMinSize(352, 94); // Kích thước tối thiểu
        hbox.setMaxSize(352, 94); // Kích thước tối đa
        hbox.setPrefSize(352, 94); // Kích thước mặc định

        hbox.setPadding(Insets.EMPTY); // Loại bỏ padding
        hbox.setSpacing(10); // Khoảng cách cố định giữa các phần tử
        // Tạo Pane chứa Circle và Label
        Pane pane = new Pane();
        pane.setPrefSize(111.0, 94.0);

        // Circle
        Color randomColor = getRandomColor();
        Circle circle = new Circle(56, 47, 33);
        circle.setFill(randomColor);
        circle.setStroke(javafx.scene.paint.Color.BLACK);

        // Label trong Circle
        Label initialsLabel = new Label(getInitials(username_send));
        initialsLabel.setTextFill(javafx.scene.paint.Color.WHITE);
        initialsLabel.setFont(new javafx.scene.text.Font("System Bold", 22));
        initialsLabel.setLayoutX(48);
        initialsLabel.setLayoutY(30);

        // Separator
        Separator separator = new Separator();
        separator.setLayoutX(-8);
        separator.setLayoutY(93);
        separator.setPrefSize(359, 10);

        // Thêm các thành phần vào Pane
        pane.getChildren().addAll(circle, initialsLabel, separator);

        // Tạo VBox chứa các Label
        VBox vbox = new VBox(5); // Khoảng cách giữa các Label
        vbox.setAlignment(Pos.CENTER_LEFT);
        vbox.setPrefSize(250, 94);

        // Label hiển thị tên
        Label nameLabel = new Label(username_send);
        nameLabel.getStyleClass().add("label-name");
        nameLabel.setMaxWidth(230); // Giới hạn chiều rộng
        nameLabel.setEllipsisString("..."); // Hiển thị dấu "..." nếu tràn

        // Label hiển thị email
        Label emailLabel = new Label(email);
        emailLabel.getStyleClass().add("label-email");
        emailLabel.setMaxWidth(230);
        emailLabel.setEllipsisString("...");
        emailLabel.setTooltip(new Tooltip(email)); // Hiển thị đầy đủ khi hover

        // Label hiển thị nội dung tóm tắt
        Label contentLabel = new Label("On " + date + " Hi Admin, I share my file to you right here.");
        contentLabel.getStyleClass().add("label-content");
        contentLabel.setMaxWidth(200);
        contentLabel.setEllipsisString("...");
        if (!read) {
            nameLabel.setStyle("-fx-font-weight: bold; -fx-text-fill:rgb(15, 163, 173);");
            emailLabel.setStyle("-fx-font-weight: bold; -fx-text-fill:rgb(15, 163, 173);");
            contentLabel.setStyle("-fx-font-weight: bold; -fx-text-fill:rgb(15, 163, 173);");
        }
        // Thêm các Label vào VBox
        vbox.getChildren().addAll(nameLabel, emailLabel, contentLabel);

        // Thêm Pane và VBox vào HBox
        hbox.getChildren().addAll(pane, vbox);

        // Ngăn không cho HBox tự dãn nở
        vbox.setVgrow(hbox, Priority.NEVER);

        // Khoảng cách cố định giữa các phần tử
        vboxContainer.setSpacing(5);
        // Loại bỏ padding
        vboxContainer.setPadding(Insets.EMPTY);

        hbox.setOnMouseClicked(event -> {
            if (!read) {
                try {
                    Mail_DAL.updateMail_Seen(
                            new Mail(username_send, username_receive, date, item_name, true, null, path));
                    loadDataAfterSetSeen();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            hbox.getStyleClass().add("selected-email");
            nameLabel.setStyle("-fx-font-weight: bold; -fx-text-fill:black;");
            emailLabel.setStyle("-fx-font-weight: normal; -fx-text-fill:black;");
            contentLabel.setStyle("-fx-font-weight: normal; -fx-text-fill:black;");
            for (Node node : vboxContainer.getChildren()) {
                if (node instanceof HBox) {
                    HBox otherHBox = (HBox) node;
                    if (otherHBox != hbox) {
                        otherHBox.getStyleClass().remove("selected-email");
                    }
                }
            }

            // Áp dụng các thay đổi khác sau khi chọn HBox
            hbox.getStylesheets().add(getClass().getResource("/Styles/recentpage.css").toExternalForm());

            HBox row = createMail_HeaderDetail(username_send, username_receive, email, date, randomColor);
            vboxMail_HeaderDetail.getChildren().clear();
            vboxMail_HeaderDetail.getChildren().add(row);

            Label label = new Label(username_send + " invited you to edit a folder");
            label.setAlignment(Pos.CENTER);
            label.setPrefSize(610.0, 35.0);
            label.setTextAlignment(TextAlignment.JUSTIFY);
            label.setWrapText(true);
            label.setFont(new javafx.scene.text.Font("System Bold", 24));
            hboxInvited.getChildren().clear();
            hboxInvited.getChildren().add(label);

            Label label_Here = new Label("Here's the folder that " + username_send + " shared with you.");
            label_Here.setAlignment(Pos.CENTER);
            label_Here.setPrefSize(610.0, 35.0);
            label_Here.setTextAlignment(TextAlignment.JUSTIFY);
            label_Here.setWrapText(true);
            label_Here.setFont(new javafx.scene.text.Font("System", 15));

            // Tạo HBox
            HBox hbox_Folder = new HBox();
            hbox_Folder.setAlignment(Pos.CENTER);
            hbox_Folder.setPrefSize(490.0, 100.0);

            // Tạo ImageView cho hình ảnh
            ImageView imageView = new ImageView();
            imageView.setFitHeight(65.0);
            imageView.setFitWidth(51.0);
            imageView.setPickOnBounds(true);
            imageView.setPreserveRatio(true);
            imageView.setImage(new Image(getClass().getResource("/images/folder.png").toExternalForm()));

            // Tạo Button
            Button button = new Button(item_name);
            button.setPrefSize(134.0, 66.0);
            button.setMnemonicParsing(false);
            button.setFont(Font.font("System Bold", 14));

            button.setOnAction(e -> {
                try {
                    String isAccess = file_folder.checkPathAccess("\\\\" + Host.dnsServer + path);
                    if (isAccess.equals("success")) {
                        if(file_folder.isFile("\\\\" + Host.dnsServer + path)){
                            File_handle.openFile("\\\\" + Host.dnsServer + path);
                        }
                        else{
                            homePageController.nowPage = "MyItem";
                            System.out.println("C:" + path);
                            if (MyItemController.reloadPage != null && MyItemController.reloadPage.isAlive()) {
                                MyItemController.reloadPage.interrupt();
                            }
                            homePageController.switchPage("MyItemPage.fxml",
                                    new MyItemController(homePageController, "C:" + path));
                        }
                       
                    } else {
                        Dialog.showAlertDialog("Fail", isAccess);
                    }
                   
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            });

            // Đặt margin cho Button
            HBox.setMargin(button, new Insets(0, 0, 0, 15));

            // Thêm ImageView và Button vào HBox
            hbox_Folder.getChildren().addAll(imageView, button);

            // Tạo Label
            Label label_This = new Label("This invite will only work for you and people with existing access.");
            label_This.setAlignment(Pos.CENTER);
            label_This.setPrefSize(610.0, 35.0);
            label_This.setTextAlignment(TextAlignment.JUSTIFY);
            label_This.setWrapText(true);
            label_This.setFont(new javafx.scene.text.Font("System", 12));

            // Thêm HBox và Label vào bố cục
            vboxInvited.getChildren().clear();
            vboxInvited.getChildren().addAll(label_Here, hbox_Folder, label_This);
        });

        return hbox;
    }

    private HBox createMail_HeaderDetail(String username_send, String username_receive, String email, String date,
            Color randomColor) {
        // Tạo HBox chính
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setPrefSize(598, 134);
        hbox.setStyle("-fx-stylesheets: '@../../../Styles/recentpage.css';");

        // Tạo Pane chứa Circle và Label
        Pane pane = new Pane();
        pane.setPrefSize(111, 94);

        Circle circle = new Circle(55, 67, 33);
        circle.setFill(randomColor);
        circle.setStroke(Color.BLACK);
        pane.getChildren().add(circle);

        Label circleLabel = new Label(getInitials(username_send));
        circleLabel.setTextFill(javafx.scene.paint.Color.WHITE);
        circleLabel.setFont(new javafx.scene.text.Font("System Bold", 22));
        circleLabel.setLayoutX(47);
        circleLabel.setLayoutY(51);
        pane.getChildren().add(circleLabel);

        pane.setCursor(Cursor.DEFAULT);

        // Tạo VBox chứa các Label
        VBox vbox = new VBox(5);
        vbox.setAlignment(Pos.CENTER_LEFT);
        vbox.setNodeOrientation(javafx.geometry.NodeOrientation.LEFT_TO_RIGHT);
        vbox.setPrefSize(489, 122);

        // Label đầu tiên
        Label nameLabel = new Label(username_send + " <" + username_send + "@pbl4.dut.vn>");
        nameLabel.setPrefSize(492, 27);
        nameLabel.setFont(new javafx.scene.text.Font("System Bold", 18));
        vbox.getChildren().add(nameLabel);

        // Label thứ hai
        Label toLabel = new Label("to: <" + username_receive + "@pbl4.dut.vn>");
        toLabel.setPrefSize(506, 18);
        toLabel.setFont(new javafx.scene.text.Font("System Bold", 12));
        vbox.getChildren().add(toLabel);

        // Label thứ ba
        Label dateLabel = new Label("date: " + date);
        dateLabel.setPrefSize(495, 18);
        dateLabel.setFont(new javafx.scene.text.Font("System Bold", 12));
        vbox.getChildren().add(dateLabel);

        VBox.setMargin(vbox, Insets.EMPTY);

        // Thêm Pane và VBox vào HBox
        hbox.getChildren().addAll(pane, vbox);

        return hbox;
    }

    // Hàm lấy chữ cái đầu từ tên
    private String getInitials(String name) {
        if (name != null && !name.isEmpty()) {
            return name.substring(0, 1).toUpperCase(); // Lấy chữ cái đầu tiên và chuyển sang chữ in hoa
        }
        return "U"; // Nếu tên rỗng hoặc null, trả về chuỗi rỗng
    }

    // Hàm lấy màu ngẫu nhiên cho mail
    private javafx.scene.paint.Color getRandomColor() {
        Random random = new Random();
        double red = random.nextDouble();
        double green = random.nextDouble();
        double blue = random.nextDouble();
        return javafx.scene.paint.Color.color(red, green, blue);
    }

    public void onClose() {
        System.out.println("Recent Page Closed");
    }

}
