package Controller;

import javafx.scene.control.TableView;

abstract public class MainController {
  
    // Lưu trữ thông tin về HomePageController
    protected HomePageController homePageController;
    static String path;
    // Thiết lập HomePageController
    public void setHomePageController(HomePageController homePageController) {
        this.homePageController = homePageController;
    }
    public void setPath(String newPath){}
    public String getPath(){
        return null;
     }
    public TableView getTableView(){
        return null;
    }
    // Xử lý logic khi trang bị góng
    public void onClose() {
        System.out.println("Closing: " + this.getClass().getSimpleName());
    }
    abstract public void PushDataTableView() throws Exception;
}
