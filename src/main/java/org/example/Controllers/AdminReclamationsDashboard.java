package org.example.Controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import org.example.Entities.Reclamations;

import java.util.List;

public class AdminReclamationsDashboard {

    @FXML
    private TableView<Reclamations> reclamationsTable;

    @FXML
    private TableColumn<Reclamations, String> titleColumn;

    @FXML
    private TableColumn<Reclamations, String> contentColumn;

    @FXML
    private TableColumn<Reclamations, String> dateColumn;

    @FXML
    private TableColumn<Reclamations, String> validateColumn;

    @FXML
    private TableColumn<Reclamations, Void> actionsColumn;

    @FXML
    private TextField searchField;

    private final ReclamationsController reclamationsController = new ReclamationsController();

    @FXML
    public void initialize() {
        titleColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTitle()));

        contentColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getContenu()));

        dateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDateReclamation().toString()));

        validateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().isValidate() ? "Valid" : "Not Valid"));

        // Action buttons (Validate + Delete)
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button validateButton = new Button("Validate");
            private final Button deleteButton = new Button("Delete");

            {
                validateButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 8 12;");
                deleteButton.setStyle("-fx-background-color: #FF5722; -fx-text-fill: white; -fx-padding: 8 12;");

                validateButton.setOnAction(e -> {
                    Reclamations reclamation = getTableView().getItems().get(getIndex());
                    reclamation.setValidate(true);
                    reclamationsController.updateReclamation(reclamation);
                    loadReclamations(); // Refresh
                });

                deleteButton.setOnAction(e -> {
                    Reclamations reclamation = getTableView().getItems().get(getIndex());
                    reclamationsController.deleteReclamation(reclamation.getId());
                    loadReclamations(); // Refresh
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(10, validateButton, deleteButton);
                    setGraphic(buttons);
                }
            }
        });

        loadReclamations(); // Initial data load
    }

    @FXML
    public void loadReclamations() {
        List<Reclamations> reclamationsList = reclamationsController.getAllReclamations();
        ObservableList<Reclamations> observableList = FXCollections.observableArrayList(reclamationsList);
        reclamationsTable.setItems(observableList);
    }

    @FXML
    public void onSearchClicked() {
        String searchTerm = searchField.getText().trim();
        if (searchTerm.isEmpty()) {
            loadReclamations(); // Show all
        } else {
            List<Reclamations> filteredList = reclamationsController.findByTitle(searchTerm);
            reclamationsTable.setItems(FXCollections.observableArrayList(filteredList));
        }
    }
}
