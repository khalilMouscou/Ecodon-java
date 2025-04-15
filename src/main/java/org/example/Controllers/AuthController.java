package org.example.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.Entities.User;
import org.example.Services.UserService;

import java.io.IOException;
import java.net.URL;

public class AuthController {

    // Login fields
    @FXML private TextField loginEmailField;
    @FXML private PasswordField loginPasswordField;

    // Registration fields
    @FXML private TextField regNameField;
    @FXML private TextField regTelField;
    @FXML private TextField regAddressField;
    @FXML private TextField regEmailField;
    @FXML private PasswordField regPasswordField;
    @FXML private PasswordField regConfirmPasswordField;

    private final UserService userService = new UserService();

    // Navigation methods (keep these as-is)
    @FXML
    private void showLoginPage(ActionEvent event) throws IOException {
        loadPage("/Login.fxml", event);
    }

    @FXML
    private void showSignupPage(ActionEvent event) throws IOException {
        loadPage("/Signup.fxml", event);
    }

    // Business logic methods (renamed as you requested)
    @FXML
    private void register(ActionEvent event) {
        try {
            // Validate all fields
            if (!validateRegistration()) {
                return;
            }

            // Create new user with properly formatted roles
            User newUser = new User(
                    regNameField.getText().trim(),
                    regEmailField.getText().trim().toLowerCase(),
                    regPasswordField.getText(),
                    Integer.parseInt(regTelField.getText()),
                    regAddressField.getText().trim(),
                    "ROLE_USER"  // Default role for new registrations
            );

            // Add to database
            userService.addUser(newUser);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Registration successful!");

            // Redirect to login
            showLoginPage(event);

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Phone number must be numeric");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Registration Error", "Failed to register: " + e.getMessage());
        }
    }

    private boolean validateRegistration() {
        // Name validation
        if (regNameField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Full name is required");
            regNameField.requestFocus();
            return false;
        }
        if (regNameField.getText().trim().length() < 3) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Name must be at least 3 characters");
            regNameField.requestFocus();
            return false;
        }

        // Email validation
        String email = regEmailField.getText().trim();
        if (email.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Email is required");
            regEmailField.requestFocus();
            return false;
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Invalid email format (example@domain.com)");
            regEmailField.requestFocus();
            return false;
        }

        // Phone validation
        String phone = regTelField.getText().trim();
        if (phone.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Phone number is required");
            regTelField.requestFocus();
            return false;
        }
        if (!phone.matches("\\d{8,15}")) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Phone must be 8-15 digits");
            regTelField.requestFocus();
            return false;
        }

        // Password validation
        String password = regPasswordField.getText();
        if (password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Password is required");
            regPasswordField.requestFocus();
            return false;
        }
        if (password.length() < 8) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Password must be at least 8 characters");
            regPasswordField.requestFocus();
            return false;
        }
        if (!password.matches(".*[A-Z].*")) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Password must contain at least one uppercase letter");
            regPasswordField.requestFocus();
            return false;
        }
        if (!password.matches(".*[a-z].*")) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Password must contain at least one lowercase letter");
            regPasswordField.requestFocus();
            return false;
        }
        if (!password.matches(".*\\d.*")) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Password must contain at least one number");
            regPasswordField.requestFocus();
            return false;
        }

        // Password confirmation
        if (!regPasswordField.getText().equals(regConfirmPasswordField.getText())) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Passwords do not match");
            regConfirmPasswordField.requestFocus();
            return false;
        }

        return true;
    }

    @FXML
    private void login(ActionEvent event) {
        String email = loginEmailField.getText().trim();
        String password = loginPasswordField.getText().trim();

        try {
            // 1. Authenticate user
            User user = userService.login(email, password);
            if (user == null) {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid email or password");
                return;
            }

            // 2. Determine dashboard path
            String cleanRole = user.getRoles().replaceAll("[\\[\\]\"]", "");
            String dashboardPath = getDashboardPath(cleanRole);

            // 3. Verify FXML file exists
            System.out.println("Attempting to load: " + dashboardPath);
            URL fxmlUrl = getClass().getResource(dashboardPath);
            if (fxmlUrl == null) {
                throw new IOException("FXML file not found at: " + dashboardPath
                        + "\n• Check it exists in src/main/resources/"
                        + "\n• Verify exact filename (case-sensitive)");
            }

            // 4. Load the FXML and pass user data
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            // Pass user to the appropriate controller
            switch (cleanRole) {
                case "ROLE_ADMIN":
                    ((AdminDashboardController)loader.getController()).setUser(user);
                    break;
                case "ROLE_ENTREPRISE":
                    ((EntrepriseDashboardController)loader.getController()).setUser(user);
                    break;
                case "ROLE_ASSOCIATION":
                    ((AssociationDashboardController)loader.getController()).setUser(user);
                    break;
                default:
                    ((UserDashboardController)loader.getController()).setUser(user);
            }

            // 5. Switch to dashboard
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(cleanRole + " Dashboard");
            stage.show();

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error",
                    "Failed to load dashboard:\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    private String getDashboardPath(String cleanRole) {
        return switch (cleanRole) {
            case "ROLE_ADMIN" -> "/AdminDashboard.fxml";
            case "ROLE_ENTREPRISE" -> "/EntrepriseDashboard.fxml";
            case "ROLE_ASSOCIATION" -> "/AssociationDashboard.fxml";
            default -> "/UserDashboard.fxml";
        };
    }

    // Make sure your showAlert method matches this signature:
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Helper methods
    private void loadPage(String fxmlPath, ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

}