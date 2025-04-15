package org.example.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.Entities.Reclamations;

import java.io.IOException;

public class ReclamationsUserController {

    @FXML
    private TextField titleField;

    @FXML
    private TextArea contentArea;

    @FXML
    private Label statusLabel;

    private final ReclamationsController reclamationsController = new ReclamationsController();

    @FXML
    private void handleAddReclamation() {
        String title = titleField.getText();
        String contenu = contentArea.getText();

        if (title.isEmpty() || contenu.isEmpty()) {
            statusLabel.setText("Veuillez remplir tous les champs !");
            statusLabel.setTextFill(javafx.scene.paint.Color.RED);
            return;
        }

        Reclamations reclamation = new Reclamations(title, contenu);
        reclamationsController.addReclamation(reclamation);

        // Reset form + message
        titleField.clear();
        contentArea.clear();
        statusLabel.setText("Réclamation envoyée avec succès !");
        statusLabel.setTextFill(javafx.scene.paint.Color.GREEN);
    }


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
