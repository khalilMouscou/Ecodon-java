package org.example.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.Entities.User;
import org.example.Services.UserService;
import java.util.regex.Pattern;

public class CreateUserController {

    @FXML private TextField nomField;
    @FXML private TextField emailField;
    @FXML private TextField passwordField;
    @FXML private TextField telField;
    @FXML private TextField adressField;
    @FXML private ComboBox<String> roleComboBox;

    private UserManag userManagementController;
    private final UserService userService = new UserService();

    // Validation patterns
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^[0-9]{8,15}$");
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$");

    public void setUserManagementController(UserManag controller) {
        this.userManagementController = controller;
    }

    @FXML
    private void initialize() {
        roleComboBox.getItems().addAll("association", "entreprise");
        roleComboBox.getSelectionModel().selectFirst();

        // Add input validation listeners
        telField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                telField.setText(oldVal);
            }
        });
    }

    @FXML
    private void handleSave() {
        try {
            // Validate all fields
            if (!validateForm()) {
                return;
            }

            User newUser = new User();
            newUser.setNom(nomField.getText().trim());
            newUser.setEmail(emailField.getText().trim().toLowerCase());
            newUser.setPassword(passwordField.getText());
            newUser.setTel(Integer.parseInt(telField.getText()));
            newUser.setAdress(adressField.getText().trim());
            newUser.setRoles("ROLE_" + roleComboBox.getValue().toUpperCase());

            userService.addUser(newUser);
            userManagementController.refreshUserList();
            nomField.getScene().getWindow().hide();

            showAlert("Success", "User created successfully!", Alert.AlertType.INFORMATION);

        } catch (Exception e) {
            showAlert("Error", "Failed to create user: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private boolean validateForm() {
        // Name validation
        if (nomField.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Name is required", Alert.AlertType.ERROR);
            nomField.requestFocus();
            return false;
        }

        // Email validation
        String email = emailField.getText().trim();
        if (email.isEmpty()) {
            showAlert("Validation Error", "Email is required", Alert.AlertType.ERROR);
            emailField.requestFocus();
            return false;
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            showAlert("Validation Error", "Invalid email format (example@domain.com)", Alert.AlertType.ERROR);
            emailField.requestFocus();
            return false;
        }

        // Password validation
        String password = passwordField.getText();
        if (password.isEmpty()) {
            showAlert("Validation Error", "Password is required", Alert.AlertType.ERROR);
            passwordField.requestFocus();
            return false;
        }
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            showAlert("Validation Error",
                    "Password must contain:\n- 8+ characters\n- 1 uppercase\n- 1 lowercase\n- 1 number",
                    Alert.AlertType.ERROR);
            passwordField.requestFocus();
            return false;
        }

        // Phone validation
        String phone = telField.getText();
        if (phone.isEmpty()) {
            showAlert("Validation Error", "Phone number is required", Alert.AlertType.ERROR);
            telField.requestFocus();
            return false;
        }
        if (!PHONE_PATTERN.matcher(phone).matches()) {
            showAlert("Validation Error", "Phone must be 8-15 digits", Alert.AlertType.ERROR);
            telField.requestFocus();
            return false;
        }

        return true;
    }

    @FXML
    private void handleCancel() {
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