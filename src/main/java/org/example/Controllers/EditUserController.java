package org.example.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.Entities.User;
import org.example.Services.UserService;

public class EditUserController {

    @FXML private TextField nomField;
    @FXML private TextField emailField;
    @FXML private TextField telField;
    @FXML private TextField adressField;
    @FXML private ComboBox<String> roleComboBox;

    private User userToEdit;
    private final UserService userService = new UserService();

    public void setUserToEdit(User user) {
        this.userToEdit = user;
        populateFields();
    }

    private void populateFields() {
        if (userToEdit != null) {
            nomField.setText(userToEdit.getNom());
            emailField.setText(userToEdit.getEmail());
            telField.setText(String.valueOf(userToEdit.getTel()));
            adressField.setText(userToEdit.getAdress());

            // Setup roles combobox
            roleComboBox.getItems().addAll("ROLE_ADMIN", "ROLE_USER", "ROLE_MODERATOR");
            roleComboBox.setValue(userToEdit.getRoles());
        }
    }

    @FXML
    private void handleSave() {
        try {
            // Update the user object with new values
            userToEdit.setNom(nomField.getText());
            userToEdit.setEmail(emailField.getText());
            userToEdit.setTel(Integer.parseInt(telField.getText()));
            userToEdit.setAdress(adressField.getText());
            userToEdit.setRoles(roleComboBox.getValue());

            // Save to database
            userService.updateUser(userToEdit);

            // Close the window
            nomField.getScene().getWindow().hide();

        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Phone must be a number", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleCancel() {
        // Close the window without saving
        nomField.getScene().getWindow().hide();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}