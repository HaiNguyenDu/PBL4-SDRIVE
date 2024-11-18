package Controller;

import DAL.MenuItem;
import DTO.File_Folder;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NewController {
    @FXML
    public Button create;
    public Button cancel;
    public TextField input;
    private String type;
    private Stage state;
    private HomePageController homePageController;

    public void initialize()
    {
        AddEventButton();
    }
    public void setState(Stage state){
        this.state = state;
    }
    public void setType(String type){
               this.type = type;
    }
    public void setHomePageController(HomePageController homePageController)
    {
        this.homePageController = homePageController;
    }
    public void restartTableView(ArrayList<File_Folder> dArraylist)
    {
        //tao data moi o day va truyen vaoTableView
//        homePageController.TableView();
    }
    void AddEventButton (){
        create.setOnAction(e->{
            String name =input.getText();
            if(type=="File")
            {

            }
            else {

            }
        });
        cancel.setOnAction(e->{
            state.close();
        });
    }
}
