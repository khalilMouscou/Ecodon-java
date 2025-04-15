package org.example.Entities;

import java.util.Date;

public class Reclamations {
    private int id;
    private String title;  // New field
    private String contenu;
    private Date dateReclamation;
    private boolean validate;

    // Constructors
    public Reclamations() {
        this.dateReclamation = new Date(); // Default to current date/time
    }

    public Reclamations(String title, String contenu) {
        this.title = title;
        this.contenu = contenu;
        this.dateReclamation = new Date();
        this.validate = false;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContenu() { return contenu; }
    public void setContenu(String contenu) { this.contenu = contenu; }

    public Date getDateReclamation() { return dateReclamation; }
    public void setDateReclamation(Date dateReclamation) { this.dateReclamation = dateReclamation; }

    public boolean isValidate() { return validate; }
    public void setValidate(boolean validate) { this.validate = validate; }

    @Override
    public String toString() {
        return "Reclamations{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", contenu='" + contenu + '\'' +
                ", dateReclamation=" + dateReclamation +
                ", validate=" + validate +
                '}';
    }
}