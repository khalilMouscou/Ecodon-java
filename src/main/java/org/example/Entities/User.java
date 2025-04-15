package org.example.Entities;

import java.util.Objects;

public class User {
    private int id;
    private String nom;
    private String email;
    private String password;
    private int tel;
    private String adress;
    private String roles;

    public User() {}

    public User(String nom, String email, String password, int tel, String adress, String roles) {
        this.nom = nom;
        this.email = email;
        this.password = password;
        this.tel = tel;
        this.adress = adress;
        this.roles = roles;
    }

    public User(int id, String nom, String email, String password, int tel, String adress, String roles) {
        this(nom, email, password, tel, adress, roles);
        this.id = id;
    }

    // Getters and Setters
    public int getId() { return id; }
    public String getNom() { return nom; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public int getTel() { return tel; }
    public String getAdress() { return adress; }
    public String getRoles() { return roles; }

    public void setId(int id) { this.id = id; }
    public void setNom(String nom) { this.nom = nom; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setTel(int tel) { this.tel = tel; }
    public void setAdress(String adress) { this.adress = adress; }
    public void setRoles(String roles) { this.roles = roles; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return id == user.id &&
                tel == user.tel &&
                Objects.equals(nom, user.nom) &&
                Objects.equals(email, user.email) &&
                Objects.equals(password, user.password) &&
                Objects.equals(adress, user.adress) &&
                Objects.equals(roles, user.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nom, email, password, tel, adress, roles);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", email='" + email + '\'' +
                ", tel=" + tel +
                ", adress='" + adress + '\'' +
                ", roles='" + roles + '\'' +
                '}';
    }

}
