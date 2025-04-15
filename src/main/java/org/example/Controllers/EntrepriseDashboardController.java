package org.example.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.example.Entities.User;

public class EntrepriseDashboardController {
    @FXML private Label entrepriseWelcomeLabel;
    private User user;

    public void setUser(User user) {
        this.user = user;
        entrepriseWelcomeLabel.setText("Enterprise: " + user.getNom());
        // Enterprise-specific initialization here
    }
}