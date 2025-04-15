package org.example.Services;

import org.example.Entities.Reclamations;
import org.example.Utilis.MyDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReclamationsService {

    private final Connection connection;

    public ReclamationsService() {
        this.connection = MyDB.getConnection();
    }

    // ✅ Add a new reclamation
    public void addReclamation(Reclamations reclamation) {
        String sql = "INSERT INTO reclamation (title, contenu, dateReclamation, validate) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, reclamation.getTitle());
            ps.setString(2, reclamation.getContenu());
            ps.setDate(3, new java.sql.Date(reclamation.getDateReclamation().getTime()));
            ps.setBoolean(4, reclamation.isValidate());

            ps.executeUpdate();
            System.out.println("✅ Reclamation added successfully!");
        } catch (SQLException e) {
            System.out.println("❌ Error adding reclamation: " + e.getMessage());
        }
    }

    // ✅ Retrieve all reclamations
    public List<Reclamations> getAllReclamations() {
        List<Reclamations> list = new ArrayList<>();
        String sql = "SELECT * FROM reclamation";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Reclamations r = new Reclamations();
                r.setId(rs.getInt("id"));
                r.setTitle(rs.getString("title"));
                r.setContenu(rs.getString("contenu"));
                r.setDateReclamation(rs.getDate("dateReclamation"));
                r.setValidate(rs.getBoolean("validate"));
                list.add(r);
            }
        } catch (SQLException e) {
            System.out.println("❌ Error retrieving reclamations: " + e.getMessage());
        }

        return list;
    }

    // ✅ Update a reclamation
    public void updateReclamation(Reclamations reclamation) {
        String sql = "UPDATE reclamation SET title=?, contenu=?, dateReclamation=?, validate=? WHERE id=?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, reclamation.getTitle());
            ps.setString(2, reclamation.getContenu());
            ps.setDate(3, new java.sql.Date(reclamation.getDateReclamation().getTime()));
            ps.setBoolean(4, reclamation.isValidate());
            ps.setInt(5, reclamation.getId());

            ps.executeUpdate();
            System.out.println("✅ Reclamation updated successfully!");
        } catch (SQLException e) {
            System.out.println("❌ Error updating reclamation: " + e.getMessage());
        }
    }

    // ✅ Delete a reclamation
    public void deleteReclamation(int id) {
        String sql = "DELETE FROM reclamation WHERE id=?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("✅ Reclamation deleted successfully!");
        } catch (SQLException e) {
            System.out.println("❌ Error deleting reclamation: " + e.getMessage());
        }
    }

    // ✅ Get a reclamation by ID
    public Reclamations getReclamationById(int id) {
        String sql = "SELECT * FROM reclamation WHERE id=?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Reclamations r = new Reclamations();
                r.setId(rs.getInt("id"));
                r.setTitle(rs.getString("title"));
                r.setContenu(rs.getString("contenu"));
                r.setDateReclamation(rs.getDate("dateReclamation"));
                r.setValidate(rs.getBoolean("validate"));
                return r;
            }
        } catch (SQLException e) {
            System.out.println("❌ Error retrieving reclamation by ID: " + e.getMessage());
        }
        return null;
    }

    // ✅ Find reclamations by title (case-insensitive)
    public List<Reclamations> findReclamationsByTitle(String title) {
        List<Reclamations> results = new ArrayList<>();
        String sql = "SELECT * FROM reclamation WHERE LOWER(title) LIKE ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "%" + title.toLowerCase() + "%");

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Reclamations r = new Reclamations();
                r.setId(rs.getInt("id"));
                r.setTitle(rs.getString("title"));
                r.setContenu(rs.getString("contenu"));
                r.setDateReclamation(rs.getDate("dateReclamation"));
                r.setValidate(rs.getBoolean("validate"));
                results.add(r);
            }
        } catch (SQLException e) {
            System.out.println("❌ Error searching reclamations by title: " + e.getMessage());
        }

        return results;
    }
}
