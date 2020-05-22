package com.example.stcov;

public class User {
    String firstname,lastname,email;
    public User() {
    }
    public User(String nom ,String prenom,String role,String email){
        this.email = email;
        this.firstname = prenom;
        this.lastname = nom;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
