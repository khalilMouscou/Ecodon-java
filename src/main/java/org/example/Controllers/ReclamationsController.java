package org.example.Controllers;

import org.example.Entities.Reclamations;
import org.example.Services.ReclamationsService;

import java.util.List;

public class ReclamationsController {
    private final ReclamationsService reclamationsService;

    public ReclamationsController() {
        this.reclamationsService = new ReclamationsService();
    }

    // Add a new reclamation
    public void addReclamation(Reclamations reclamation) {
        reclamationsService.addReclamation(reclamation);
    }

    // Get all reclamations
    public List<Reclamations> getAllReclamations() {
        return reclamationsService.getAllReclamations();
    }

    // Get reclamation by ID
    public Reclamations getReclamationById(int id) {
        return reclamationsService.getReclamationById(id);
    }

    // Update a reclamation
    public void updateReclamation(Reclamations updatedReclamation) {
        reclamationsService.updateReclamation(updatedReclamation);
    }

    // Delete a reclamation
    public void deleteReclamation(int id) {
        reclamationsService.deleteReclamation(id);
    }

    // Search reclamations by title
    public List<Reclamations> findByTitle(String title) {
        return reclamationsService.findReclamationsByTitle(title);
    }
}
