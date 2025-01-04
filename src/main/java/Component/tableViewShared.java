package Component;

import java.lang.reflect.Array;
import java.util.ArrayList;

import DTO.File_Folder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class tableViewShared {
    @FXML
    private TableView tableViewShared;
    public tableViewShared(){
        tableViewShared = new TableView();
    }

    public TableView getTableView() {
        return tableViewShared;
    }

        public ArrayList<File_Folder> loadData(String Path)throws Exception{
            return new ArrayList<>();
        }

    public void PushDataTableView(ArrayList<File_Folder> dArrayList)
    {

        int selectedIndex = tableViewShared.getSelectionModel().getSelectedIndex();

        // Configure columns if not already added
        if (tableViewShared.getColumns().isEmpty()) {
            TableColumn<File_Folder, String> nameColumn = new TableColumn<>("Name");
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
            nameColumn.setPrefWidth(450);

            TableColumn<File_Folder, String> lastWriteTimeColumn = new TableColumn<>("Last Write Time");
            lastWriteTimeColumn.setCellValueFactory(new PropertyValueFactory<>("LastTimeWrite"));
            lastWriteTimeColumn.setPrefWidth(450);

            tableViewShared.getColumns().addAll(nameColumn, lastWriteTimeColumn);
        }

        // Convert dArrayList to ObservableList and set it as the data for tableViewShared
        ObservableList<File_Folder> observableFileList = FXCollections.observableArrayList(dArrayList);
        tableViewShared.setItems(observableFileList);

        // Restore the previous selection
        if (selectedIndex >= 0 && selectedIndex < observableFileList.size()) {
            tableViewShared.getSelectionModel().select(selectedIndex);
        }

        // Add stylesheet (optional, only if not added before)
        if (tableViewShared.getStylesheets().isEmpty()) {
            tableViewShared.getStylesheets().add(getClass().getResource("/Styles/homepage.css").toExternalForm());
        }
    }
}
