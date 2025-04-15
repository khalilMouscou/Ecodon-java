package org.example.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.example.Entities.User;

public class AssociationDashboardController {
    @FXML private Label associationWelcomeLabel;
    private User user;

    public void setUser(User user) {
        this.user = user;
        associationWelcomeLabel.setText("Association: " + user.getNom());
        // Association-specific initialization here
    }
}