package org.example.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.Entities.User;
import org.example.Entities.Reclamations;
import java.io.IOException;
import java.util.List;

public class UserDashboardController {
    @FXML private Label welcomeLabel;
    @FXML private ListView<Reclamations> reclamationsList;
    @FXML private TextField searchField;

    private User currentUser;
    private final ReclamationsController reclamationsController = new ReclamationsController();

    public void setUser(User user) {
        this.currentUser = user;
        welcomeLabel.setText("Welcome, " + user.getNom());
        loadReclamations();
    }

    @FXML
    public void initialize() {
        reclamationsList.setCellFactory(param -> new ListCell<>() {
            private final Label titleLabel = new Label();
            private final Label contentLabel = new Label();
            private final Label dateLabel = new Label();
            private final Label statusLabel = new Label();
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");
            private final HBox buttonsBox = new HBox(10, editButton, deleteButton);
            private final HBox contentBox = new HBox(20, titleLabel, contentLabel, dateLabel, statusLabel, buttonsBox);

            {
                contentBox.setStyle("-fx-padding: 10; -fx-border-color: #eee; -fx-border-width: 0 0 1 0;");
                editButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");

                editButton.setOnAction(e -> showEditDialog(getItem()));

                deleteButton.setOnAction(e -> {
                    Reclamations reclamation = getItem();
                    reclamationsController.deleteReclamation(reclamation.getId());
                    loadReclamations();
                });
            }

            @Override
            protected void updateItem(Reclamations reclamation, boolean empty) {
                super.updateItem(reclamation, empty);
                if (empty || reclamation == null) {
                    setGraphic(null);
                } else {
                    titleLabel.setText(reclamation.getTitle());
                    contentLabel.setText(reclamation.getContenu());
                    dateLabel.setText(reclamation.getDateReclamation().toString());
                    statusLabel.setText(reclamation.isValidate() ? "Valid" : "Pending");
                    setGraphic(contentBox);
                }
            }
        });
    }

    private void showEditDialog(Reclamations reclamation) {
        try {
            // Create a new stage for the popup
            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle("Edit Reclamation");

            // Create form elements
            Label titleLabel = new Label("Title:");
            TextField titleField = new TextField(reclamation.getTitle());
            titleField.setPrefWidth(300);

            Label contentLabel = new Label("Content:");
            TextArea contentArea = new TextArea(reclamation.getContenu());
            contentArea.setPrefWidth(300);
            contentArea.setPrefHeight(150);
            contentArea.setWrapText(true);

            Button saveButton = new Button("Save");
            saveButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
            saveButton.setOnAction(e -> {
                reclamation.setTitle(titleField.getText());
                reclamation.setContenu(contentArea.getText());
                reclamationsController.updateReclamation(reclamation);
                loadReclamations();
                dialog.close();
            });

            Button cancelButton = new Button("Cancel");
            cancelButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
            cancelButton.setOnAction(e -> dialog.close());

            HBox buttonBox = new HBox(10, saveButton, cancelButton);
            buttonBox.setAlignment(Pos.CENTER_RIGHT);

            VBox layout = new VBox(10, titleLabel, titleField, contentLabel, contentArea, buttonBox);
            layout.setStyle("-fx-padding: 20;");
            layout.setPrefWidth(350);

            Scene dialogScene = new Scene(layout);
            dialog.setScene(dialogScene);
            dialog.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open edit dialog: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadReclamations() {
        List<Reclamations> reclamations = reclamationsController.getAllReclamations();
        reclamationsList.getItems().setAll(reclamations);
    }

    @FXML
    public void handleSearch(ActionEvent event) {
        String searchTerm = searchField.getText().trim();
        if (searchTerm.isEmpty()) {
            loadReclamations();
        } else {
            List<Reclamations> filtered = reclamationsController.findByTitle(searchTerm);
            reclamationsList.getItems().setAll(filtered);
        }
    }

    // Keep your existing navigation methods unchanged
    public void handleReclamationsClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ReclamationsUser.fxml"));
            Parent reclamationRoot = loader.load();
            Scene scene = new Scene(reclamationRoot);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleMainClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserDashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}