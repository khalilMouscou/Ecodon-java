package org.example.Services;

import org.example.Entities.User;
import org.example.Utilis.MyDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService {

    private final Connection connection;

    public UserService() {
        this.connection = MyDB.getConnection();
    }

    // ✅ Existing CRUD Operations (unchanged)
    public void addUser(User user) {
        String sql = "INSERT INTO user (nom, email, password, tel, adress, roles) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, user.getNom());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setInt(4, user.getTel());
            ps.setString(5, user.getAdress());

            // Format the role correctly for database storage
            String dbRoleFormat = "[\"" + user.getRoles() + "\"]";
            ps.setString(6, dbRoleFormat);

            ps.executeUpdate();
            System.out.println("✅ User inserted successfully!");
        } catch (SQLException e) {
            System.out.println("❌ Error inserting user: " + e.getMessage());
            e.printStackTrace(); // Add this to see full stack trace
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM user";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String dbRole = rs.getString("roles");
                String cleanRole = dbRole != null ?
                        dbRole.replace("[", "").replace("]", "") :
                        "ROLE_USER";

                User user = new User(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getInt("tel"),
                        rs.getString("adress"),
                        cleanRole
                );
                users.add(user);
            }
        } catch (SQLException e) {
            System.out.println("❌ Error fetching users: " + e.getMessage());
        }
        return users;
    }

    public void updateUser(User user) {
        String sql = "UPDATE user SET nom=?, email=?, password=?, tel=?, adress=?, roles=? WHERE id=?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, user.getNom());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setInt(4, user.getTel());
            ps.setString(5, user.getAdress());
            ps.setString(6, "[" + user.getRoles() + "]");
            ps.setInt(7, user.getId());

            ps.executeUpdate();
            System.out.println("✅ User updated successfully!");
        } catch (SQLException e) {
            System.out.println("❌ Error updating user: " + e.getMessage());
        }
    }

    public void deleteUser(int id) {
        String sql = "DELETE FROM user WHERE id=?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("✅ User deleted successfully!");
        } catch (SQLException e) {
            System.out.println("❌ Error deleting user: " + e.getMessage());
        }
    }

    // ✅ New Authentication Methods
    public User login(String email, String password) {
        String sql = "SELECT * FROM user WHERE email = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                // 1. Verify password (plain text comparison in this example)
                String storedPassword = rs.getString("password");
                if (!storedPassword.equals(password)) {
                    return null; // Password mismatch
                }

                // 2. Handle role parsing safely
                String dbRoles = rs.getString("roles");
                String cleanRoles = dbRoles.replaceAll("[\\[\\]\"]", ""); // Remove [" and "]

                return new User(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("email"),
                        storedPassword, // Return the stored (potentially hashed) password
                        rs.getInt("tel"),
                        rs.getString("adress"),
                        cleanRoles
                );
            }
        } catch (SQLException e) {
            System.err.println("Login error details:");
            e.printStackTrace();
        }
        return null;
    }

    public boolean signUp(User user) {
        // First check if email already exists
        if (getUserByEmail(user.getEmail()) != null) {
            System.out.println("❌ User with this email already exists");
            return false;
        }

        // Set default role if not provided
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles("ROLE_USER");
        }

        // Use existing addUser method
        addUser(user);
        return true;
    }

    // ✅ Helper method for authentication
    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM user WHERE email = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String dbRole = rs.getString("roles");
                String cleanRole = dbRole != null ?
                        dbRole.replace("[", "").replace("]", "") :
                        "ROLE_USER";

                return new User(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getInt("tel"),
                        rs.getString("adress"),
                        cleanRole
                );
            }
        } catch (SQLException e) {
            System.out.println("❌ Error fetching user by email: " + e.getMessage());
        }
        return null;
    }
}