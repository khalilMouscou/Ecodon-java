package org.example.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.Entities.User;
import org.example.Services.UserService;

import java.io.IOException;
import java.util.List;

public class UserManag {

    @FXML
    private VBox usersContainer;

    private final UserService userService = new UserService();

    @FXML
    private void initialize() {
        loadUsers();
    }

    private void loadUsers() {
        usersContainer.getChildren().clear();
        List<User> users = userService.getAllUsers();

        for (User user : users) {
            HBox userRow = new HBox(10);
            userRow.setStyle("-fx-padding: 10; -fx-border-color: #e0e0e0; -fx-border-width: 0 0 1 0;");

            // User data columns
            Label nameLabel = new Label(user.getNom());
            nameLabel.setMinWidth(150);

            Label emailLabel = new Label(user.getEmail());
            emailLabel.setMinWidth(200);

            Label phoneLabel = new Label(String.valueOf(user.getTel()));
            phoneLabel.setMinWidth(100);

            Label addressLabel = new Label(user.getAdress());
            addressLabel.setMinWidth(200);

            Label roleLabel = new Label(user.getRoles());
            roleLabel.setMinWidth(100);

            // Action buttons
            HBox actionBox = new HBox(5);
            actionBox.setMinWidth(150);

            Button editBtn = new Button("Edit");
            editBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
            editBtn.setOnAction(e -> handleEditUser(user));

            Button deleteBtn = new Button("Delete");
            deleteBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
            deleteBtn.setOnAction(e -> handleDeleteUser(user));

            actionBox.getChildren().addAll(editBtn, deleteBtn);

            userRow.getChildren().addAll(
                    nameLabel, emailLabel, phoneLabel,
                    addressLabel, roleLabel, actionBox
            );

            usersContainer.getChildren().add(userRow);
        }
    }


    private void handleDeleteUser(User user) {
        System.out.println("Deleting user: " + user.getNom());
        // Add your delete logic here
        userService.deleteUser(user.getId());
        loadUsers(); // Refresh the list
    }

    private void handleEditUser(User user) {
        try {
            // Load the EditUser FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditUser.fxml"));
            Parent root = loader.load();

            // Get the controller and pass the user to edit
            EditUserController controller = loader.getController();
            controller.setUserToEdit(user);

            // Create a new stage for the edit window
            Stage stage = new Stage();
            stage.setTitle("Edit User: " + user.getNom());
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            // Show error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load edit page");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void handleCreateUser() {
        try {
            // Load the CreateUser FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CreateUser.fxml"));
            Parent root = loader.load();

            // Get the controller
            CreateUserController controller = loader.getController();
            controller.setUserManagementController(this);

            // Create a new stage for the create window
            Stage stage = new Stage();
            stage.setTitle("Create New User");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not load create user page", Alert.AlertType.ERROR);
        }
    }

    // Add this to refresh the user list after creation
    public void refreshUserList() {
        loadUsers();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}