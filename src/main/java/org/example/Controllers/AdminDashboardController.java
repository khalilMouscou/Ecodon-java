package org.example.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import org.example.Entities.User;

import java.io.IOException;

public class AdminDashboardController {
    @FXML private Label usernameLabel;
    @FXML private Label welcomeLabel;
    @FXML private StackPane contentPane;

    private User currentUser;

    public void setUser(User user) {
        this.currentUser = user;
        usernameLabel.setText("Welcome, " + user.getNom());
        welcomeLabel.setText("Hello " + user.getNom() + "! You are logged in as ADMIN");
    }

    private void loadPage(String fxmlPath) throws IOException {
        contentPane.getChildren().clear();
        contentPane.getChildren().add(
                FXMLLoader.load(getClass().getResource(fxmlPath))
        );
    }

    @FXML
    private void handleUsersButton(ActionEvent event) throws IOException {
        loadPage("/UserManag.fxml");
    }

    @FXML
    private void handleEventsButton(ActionEvent event) throws IOException {
        loadPage("/EventsManagement.fxml");
    }
    @FXML
    private void handleReclamationsButton(ActionEvent event) throws IOException {
        loadPage("/AdminReclamationsDashboard.fxml");
    }
}