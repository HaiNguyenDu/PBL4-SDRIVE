package Controller;

abstract public class MainController {
    // Lưu trữ thông tin về HomePageController
    protected HomePageController homePageController;

    // Thiết lập HomePageController
    public void setHomePageController(HomePageController homePageController) {
        this.homePageController = homePageController;
    }

    // Xử lý logic khi trang bị góng
    public void onClose() {
        System.out.println("Closing: " + this.getClass().getSimpleName());
    }
}
